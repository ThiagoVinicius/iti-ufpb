package jogoshannon.server;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.http.HttpSession;

import jogoshannon.client.JuizSoletrando;
import jogoshannon.server.persistent.Cobaia;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.Desafio;
import jogoshannon.server.persistent.Experimento;
import jogoshannon.server.persistent.ExperimentoDefault;
import jogoshannon.server.persistent.FraseStore;
import jogoshannon.server.persistent.Rodada;
import jogoshannon.server.persistent.Usuario;
import jogoshannon.shared.Frase;
import jogoshannon.shared.SessaoInvalidaException;
import jogoshannon.shared.Tentativas;
import jogoshannon.shared.UsuarioNaoEncontradoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class JuizSoletrandoImpl extends RemoteServiceServlet implements
        JuizSoletrando {
    
    private static final Logger logger = 
        LoggerFactory.getLogger(JuizSoletrandoImpl.class);

    private Cobaia getUsuarioAtual(PersistenceManager pm) {

        HttpSession sessao = getThreadLocalRequest().getSession();
        Key chave = (Key) sessao.getAttribute("usuario");
        Cobaia usuario = null;

        if (chave == null) {
            usuario = new Cobaia();
            usuario = pm.makePersistent(usuario);
            chave = usuario.getKey();
            sessao.setAttribute("usuario", chave);
            logger.info("Criando NOVO usuario, id = {}; Sessao = {}", 
                    usuario.getKey(), sessao.getId());
        } else {
            usuario = pm.getObjectById(Cobaia.class, chave);
            logger.info("RECUPERANDO usuario, id = {}; Sessao = {}", 
                    usuario.getKey(), sessao.getId());
        }

        return usuario;

    }

    private void forcarSessaoValida() throws SessaoInvalidaException {
        if (!checarSessaoValida() || !temSessao()) {
            logger.info("A sessao era inválida, mas isso não era permitido " +
            		"neste contexto.");
            throw new SessaoInvalidaException();
        }
    }

    private boolean checarSessaoValida() {
        
        logger.info("Checando validade da sessão.");
        
        String idSessaoBrowser = getThreadLocalRequest().getParameter(
                "id_sessao");
        HttpSession sessaoServidor = getThreadLocalRequest().getSession(false);
        String idSessaoServidor = null;

        if (sessaoServidor != null) {
            idSessaoServidor = sessaoServidor.getId();
        }

        if (idSessaoBrowser != null) {
            if (!idSessaoBrowser.equals(idSessaoServidor)) {
                return false;
            }
        } else if (idSessaoServidor != null) {
            if (!idSessaoServidor.equals(idSessaoBrowser)) {
                return false;
            }
        }

        return true;
    }

    private boolean temSessao() {
        return getThreadLocalRequest().getSession(false) != null;
    }

    @Override
    public long getId() {
        
        logger.info("Executando: getId()");
        
        boolean sessaoValida = checarSessaoValida();
        
        if (sessaoValida) {
            logger.debug("Solicitação de ID já estava associada a uma sessão " +
            		"valida. Ignorando sessão antiga e criando uma nova.");
        }
        
        destruirSessao();

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();
        Cobaia usuario = getUsuarioAtual(pm);
        pm.close();
        
        logger.info("Retornando id = {}", usuario.getIdSessao());
        
        return usuario.getIdSessao();
    }

    @Override
    public Frase[] getFrases(Long idExperimento) throws SessaoInvalidaException {

        logger.info("Executando: getFrases()");
        
        forcarSessaoValida();

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();

        Experimento exp;
        List<String> frases;
        try {
            //resposta = (List<FraseStore>) consulta.execute();
            exp = ExperimentoDefault.getDefault(pm);
            frases = exp.getFrases().getFrases();

            Frase resultado[] = new Frase[frases.size()];
            for (int i = 0; i < resultado.length; ++i) {
                resultado[i] = new Frase(frases.get(i));
            }
            
            logger.info("Retornando {} frases.", resultado.length);
            
            return resultado;
        } finally {
            pm.close();
        }
    }

    @Override
    public void atualizaTentativas(Tentativas contadores[])
            throws SessaoInvalidaException {

        logger.info("Executando: atualizaTentativas(Tentativas[])");
        
        forcarSessaoValida();

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            List<Rodada> tmp = new LinkedList<Rodada>();
            for (int i = 0; i < contadores.length; ++i) {
                tmp.add(new Rodada(contadores[i].contagens));
            }
                
            Cobaia usuario = getUsuarioAtual(pm);
            
            if (!usuario.getDesafios().isEmpty()) {
                logger.error("As tentativas deste usuario já haviam sido " +
                		"cadastradas. Esta sessão já deveria ter expirado.");
                throw new SessaoInvalidaException("Este usuário já possuia " +
                		"tentativas cadastradas.");
            }
            
            usuario.getDesafios().addAll(tmp);

            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }

            destruirSessao();
        }
    }

    @Override
    public Tentativas[] getResultados(long id)
            throws UsuarioNaoEncontradoException {
        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();
        
        logger.info("Executando: getResultados(long)");
        
        Key chave = KeyFactory.createKey(Cobaia.class.getSimpleName(), id);
        try {

            Cobaia usuario = pm.getObjectById(Cobaia.class, chave);
            List<Rodada> desafios = usuario.getDesafios();
            Tentativas resultado[] = new Tentativas[desafios.size()];

            for (int i = 0; i < resultado.length; ++i) {
                Rodada des = desafios.get(i);
                resultado[i] = new Tentativas(des.getTentativas());
            }
            
            logger.info("Retornando {} tentativas para o usuário {}", 
                    resultado.length, id); 
            
            return resultado;

        } catch (JDOObjectNotFoundException e) {
            logger.info("Usuário de id '{}' não foi encontrado.", id);
            throw new UsuarioNaoEncontradoException();
        } catch (Exception e) {
            logger.error("Excecao inesperada.", e);
            throw new UsuarioNaoEncontradoException();
        } finally {
            pm.close();
        }

    }

    private void destruirSessao() {
        HttpSession session = getThreadLocalRequest().getSession(false);
        if (session != null) {
            logger.info("Destruindo sessão: {}", session.getId());
            session.invalidate();
        }
    }

}
