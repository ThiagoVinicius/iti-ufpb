package jogoshannon.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.http.HttpSession;

import jogoshannon.client.JuizSoletrando;
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

    private Usuario getUsuarioAtual(PersistenceManager pm) {

        HttpSession sessao = getThreadLocalRequest().getSession();
        Key chave = (Key) sessao.getAttribute("usuario");
        Usuario usuario = null;

        if (chave == null) {
            usuario = new Usuario();
            usuario = pm.makePersistent(usuario);
            chave = usuario.getKey();
            sessao.setAttribute("usuario", chave);
            logger.info("Criando NOVO usuario, id = {}; Sessao = {}", 
                    usuario.getKey(), sessao.getId());
        } else {
            usuario = pm.getObjectById(Usuario.class, chave);
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
        Usuario usuario = getUsuarioAtual(pm);
        pm.close();
        
        logger.info("Retornando id = {}", usuario.getIdSessao());
        
        return usuario.getIdSessao();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Frase[] getFrases() throws SessaoInvalidaException {

        logger.info("Executando: getFrases()");
        
        forcarSessaoValida();

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();
        Query consulta = pm.newQuery(FraseStore.class);

        List<FraseStore> resposta;
        try {
            resposta = (List<FraseStore>) consulta.execute();

            Frase resultado[] = new Frase[resposta.size()];
            for (int i = 0; i < resultado.length; ++i) {
                resultado[i] = resposta.get(i).getConteudo();
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

            List<Desafio> tmp = new LinkedList<Desafio>();
            for (int i = 0; i < contadores.length; ++i) {
                tmp.add(new Desafio(contadores[i].contagens));
            }
                
            Usuario usuario = getUsuarioAtual(pm);
            
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
        
        Key chave = KeyFactory.createKey(Usuario.class.getSimpleName(), id);
        try {

            Usuario usuario = pm.getObjectById(Usuario.class, chave);
            List<Desafio> desafios = usuario.getDesafios();
            Tentativas resultado[] = new Tentativas[desafios.size()];

            for (int i = 0; i < resultado.length; ++i) {
                Desafio des = desafios.get(i);
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
