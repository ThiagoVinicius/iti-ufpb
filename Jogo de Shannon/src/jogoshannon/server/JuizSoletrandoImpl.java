package jogoshannon.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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
			Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Criando NOVO usuario, id = " + usuario.getKey()+"; "+
					"Sessao = " + sessao.getId());
		} else {
			usuario = (Usuario) pm.getObjectById(Usuario.class, chave);
			Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"RECUPERANDO usuário, id = " + usuario.getKey()+"; "+
					"Sessao = " + sessao.getId());
		}
		
		return usuario;

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
		
		destruirSessao();
		
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
	public Frase[] getFrases() throws SessaoInvalidaException {
		
		forcarSessaoValida();
		
		PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
		Query consulta =  pm.newQuery(FraseStore.class);
		
		List<FraseStore> resposta;
		try {
			resposta = (List<FraseStore>) consulta.execute();
			
			Frase resultado[] = new Frase[resposta.size()];
			for (int i = 0; i < resultado.length; ++i) {
				resultado[i] = resposta.get(i).getConteudo();
			}
			return resultado;
		}  finally {
			pm.close();
		}
	}

//	@Override
//	public int getTotalFrases() throws SessaoInvalidaException {
//		
//		forcarSessaoValida();
//		
//		return getTotalFrasesImpl();
//		
//	}
	
	@Override
	public void atualizaTentativas (Tentativas contadores[]) throws SessaoInvalidaException {
		
		forcarSessaoValida();
		
		PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			
			Usuario usuario = getUsuarioAtual(pm);
			List<Desafio> desafios = usuario.getDesafios();
			while (contadores.length > desafios.size()) {
				desafios.add(new Desafio(contadores[0].contagens.length));
			}
			
			for (int i = 0; i < desafios.size(); ++i) {
				Desafio atualizando = desafios.get(i);
				atualizando.somaTentativas(contadores[i].contagens);
			}
			
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			
			destruirSessao();
		}
	}
	
	@Override
	public Tentativas[] getResultados (long id) throws UsuarioNaoEncontradoException {
		PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
		Key chave = KeyFactory.createKey(Usuario.class.getSimpleName(), id);
		try {
			
			Usuario usuario = pm.getObjectById(Usuario.class, chave);
			List<Desafio> desafios = usuario.getDesafios();
			Tentativas resultado[] = new Tentativas[desafios.size()];
			
			for (int i = 0; i < resultado.length; ++i) {
				Desafio des = desafios.get(i);
				resultado[i] = new Tentativas(des.getTentativas());
			}
			
			return resultado;
			
		} catch (JDOObjectNotFoundException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Objeto nao encontrado para o id " + id);
			throw new UsuarioNaoEncontradoException();
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, 
					"Excecao inesperada.", e);
			throw new UsuarioNaoEncontradoException();
		}
		finally {
			pm.close();
		}

	}

	private void destruirSessao() {
		HttpSession session = getThreadLocalRequest().getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
	
}