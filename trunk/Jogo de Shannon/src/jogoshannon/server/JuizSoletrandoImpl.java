package jogoshannon.server;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.http.HttpSession;

import jogoshannon.client.JuizSoletrando;
import jogoshannon.shared.Frase;
import jogoshannon.shared.SessaoInvalidaException;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class JuizSoletrandoImpl extends RemoteServiceServlet implements JuizSoletrando {
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	private Usuario getUsuarioAtual (PersistenceManager pm) {

		HttpSession sessao = getThreadLocalRequest().getSession();
		Key chave = (Key) sessao.getAttribute("usuario");
		Usuario usuario = null;
			
		if (chave == null) {
			usuario = new Usuario();
			usuario = pm.makePersistent(usuario);
			chave = usuario.getKey();
			sessao.setAttribute("usuario", chave);
		} else {
			usuario = (Usuario) pm.getObjectById(Usuario.class, chave);
		}
		
		return usuario;

	}
	
	private int getTotalFrasesImpl () {
		
		HttpSession sessao = getThreadLocalRequest().getSession(false);
		Integer total = (Integer) sessao.getAttribute("total_frases");
		
		if (total == null) {
			
			PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
			
			try {
				Query consulta =  pm.newQuery(FraseStore.class);
				List<FraseStore> resposta = (List<FraseStore>) consulta.execute();
				total = resposta.size();
				sessao.setAttribute("total_frases", total);
			} catch (Exception e) {
				return 0;
			} finally {
				pm.close();
			}
			
		}
		
		return total;

	}
	
	private void forcarSessaoValida () throws SessaoInvalidaException {
		 if (!checarSessaoValida() || !temSessao()) {
			 throw new SessaoInvalidaException();
		 }
	}
	
	private boolean checarSessaoValida () {
		String idSessaoBrowser = getThreadLocalRequest().getParameter("id_sessao");
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
			if(!idSessaoServidor.equals(idSessaoBrowser)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean temSessao () {
		return getThreadLocalRequest().getSession(false) != null;
	}
	
	@Override
	public long getId() {
		
		boolean valida = checarSessaoValida();
		if (!valida) {
			destruirSessao();
		}
		
		PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
		Usuario usuario = getUsuarioAtual(pm);
		pm.close();
		
		return usuario.getIdSessao();
	}
	
//	private long maisMaisContador () {
//		PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
//		ContadorIds contador = null;
//		try {
//			contador = pm.getObjectById(ContadorIds.class, ContadorIds.CHAVE);
//		} catch (Exception e) {
//			//primeira vez que estamos rodando.
//			contador = new ContadorIds();
//			contador.set(0L);
//			pm.makePersistent(contador);
//		} finally {
//			contador.set(contador.get() + 1);
//			pm.close();
//		}
//		
//		return contador.get();
//	}

	@Override
	public Frase getFrase(int id) throws SessaoInvalidaException {
		
		forcarSessaoValida();
		
		PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
		Query consulta =  pm.newQuery(FraseStore.class);
		
		List<FraseStore> resposta;
		try {
			resposta = (List<FraseStore>) consulta.execute();
			return resposta.get(id).getConteudo();
		}  finally {
			pm.close();
		}
	}

	@Override
	public int getTotalFrases() throws SessaoInvalidaException {
		
		forcarSessaoValida();
		
		return getTotalFrasesImpl();
		
	}
	
	@Override
	public void atualizaTentativas (int fraseId, int contadores[]) throws SessaoInvalidaException {
		
		forcarSessaoValida();
		
		Logger.getLogger(JuizSoletrandoImpl.class.getName()).log(Level.INFO,
			"Dados recebidos: fraseId = " + fraseId + ", contadores[] = " + 
			Arrays.toString(contadores));
		
		PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			
			Usuario usuario = getUsuarioAtual(pm);
			List<Desafio> desafios = usuario.getDesafios();
			while (fraseId >= desafios.size()) {
				desafios.add(new Desafio(contadores.length));
			}
			
			Desafio paraAtualizar = desafios.get(fraseId);
			paraAtualizar.somaTentativas(contadores);
			
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@Override
	public void destruirSessao() {
		HttpSession session = getThreadLocalRequest().getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
	
}
