package jogoshannon.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.http.HttpSession;

import jogoshannon.client.remote.JuizSoletrando;
import jogoshannon.server.persistent.Cobaia;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.Experimento;
import jogoshannon.server.persistent.ExperimentoDefault;
import jogoshannon.server.persistent.Rodada;
import jogoshannon.shared.DadosJogo;
import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.SessaoInvalidaException;
import jogoshannon.shared.Tentativas;
import jogoshannon.shared.UsuarioNaoEncontradoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class JuizSoletrandoImpl extends RemoteServiceServlet implements
        JuizSoletrando {
    
    private static final Logger logger = 
        LoggerFactory.getLogger(JuizSoletrandoImpl.class);

    private Cobaia getUsuarioAtual(Experimento exp, PersistenceManager pm) throws IOException {

        HttpSession sessao = getThreadLocalRequest().getSession();
        Key chave = (Key) sessao.getAttribute("usuario");
        Cobaia usuario = null;

        if (chave == null) {
            usuario = GestorExperimentos.novaCobaia(exp, pm);
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
    
    private void setExperimentoAtual (Experimento exp) {
        HttpSession sessao = getThreadLocalRequest().getSession();
        sessao.setAttribute("experimento", exp.getKey());
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

    public DadosJogo getFrases(Long idExperimento) throws SessaoInvalidaException {

        logger.info("Executando: getFrases()");
        
        boolean sessaoValida = checarSessaoValida();
        
        if (sessaoValida) {
            logger.debug("Solicitação de ID já estava associada a uma sessão " +
                    "valida. Ignorando sessão antiga e criando uma nova.");
        }
        
        destruirSessao();

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();

        Experimento exp;
        Cobaia usuario;
        try {
            //resposta = (List<FraseStore>) consulta.execute();
            exp = getExperimento(idExperimento, pm);
            usuario = getUsuarioAtual(exp, pm);
            exp.addCobaia(usuario);
            setExperimentoAtual(exp);
            
            List<Integer> exibirLetras = exp.getMostrarLetras();
            ConjuntoFrases frases = usuario.getFrases(pm);
            
            pm.close();
            logger.info("Retornando {} frases.", frases.getFrases().size());
            
            return new DadosJogo(exibirLetras, frases.toStub(), 
                    usuario.getIdSessao(), exp.getId());
        } catch (IOException e) {
            logger.warn("", e);
            //FIXME
            throw new RuntimeException(e);
        } finally {
            if (pm.isClosed() == false) {
                pm.close();
            }
        }
    }

    @Override
    public void atualizaTentativas(Tentativas contadores[])
            throws SessaoInvalidaException {

        logger.info("Executando: atualizaTentativas(Tentativas[])");
        
        forcarSessaoValida();

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();
        
        //FIXME Colocar para funcionar novamente a transacao
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            List<Rodada> tmp = new LinkedList<Rodada>();
            for (int i = 0; i < contadores.length; ++i) {
                tmp.add(new Rodada(contadores[i].contagens));
            }
            
            Cobaia usuario = getUsuarioAtual(null, pm);
            
            if (!usuario.getDesafios().isEmpty()) {
                logger.error("As tentativas deste usuario já haviam sido " +
                		"cadastradas. Esta sessão já deveria ter expirado.");
                throw new SessaoInvalidaException("Este usuário já possuia " +
                		"tentativas cadastradas.");
            }
            
            usuario.setDesafios(tmp);

            tx.commit();
        } catch (IOException e) {
            logger.error("Esta exceção não deveria acontecer após êxito " +
            		"na invocação de forcarSessaoValiada()", e);
            throw new SessaoInvalidaException(e);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();

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
        Cobaia usuario = null;
        try {
            usuario = pm.getObjectById(Cobaia.class, chave);
            
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

    private Experimento getExperimento (Long id, PersistenceManager pm) {
        if (id == 0L) {
            try {
                return ExperimentoDefault.getDefault(pm);
            } catch (IOException e) {
                logger.warn("Erro ao carregar experimento padrão: ", e);
                throw new RuntimeException(e);
            }
        } else {
            Experimento exp = pm.getObjectById(Experimento.class, id);
            return exp;
        }
        
    }
    
    @Override
    public ExperimentoStub getExperimentoStub(Long id) {
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        try {
            return getExperimento(id, pm).toStub();
        } finally {
            pm.close();
        }
    }
    
    @Override
    public boolean souAdmin() {
        UserService userService = UserServiceFactory.getUserService();
        boolean result = userService.isUserLoggedIn() && userService.isUserAdmin();
        return result;
    }
    

    @SuppressWarnings("unchecked")
    @Override
    public ExperimentoStub[] getExperimentos() {
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        
        Query consulta = pm.newQuery(Experimento.class);

        List<Experimento> resposta;
        try {
            resposta = (List<Experimento>) consulta.execute();

            ExperimentoStub resultado[] = new ExperimentoStub[resposta.size()];
            for (int i = 0; i < resultado.length; ++i) {
                Experimento cur = resposta.get(i);
                resultado[i] = cur.toStub();
            }
            
            logger.info("Retornando {} experimentos.", resultado.length);
            
            return resultado;
        } finally {
            pm.close();
        }

    }
    
}
