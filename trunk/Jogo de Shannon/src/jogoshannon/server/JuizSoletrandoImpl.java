package jogoshannon.server;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import jogoshannon.client.JuizSoletrando;
import jogoshannon.shared.Frase;

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
	
	@Override
	public long getId() {
		
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
	public Frase getFrase(int id) {
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
	public int getTotalFrases() {
		
		PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
		Query consulta =  pm.newQuery(FraseStore.class);
		
		List<FraseStore> resposta;
		try {
			resposta = (List<FraseStore>) consulta.execute();
			return resposta.size();
		} catch (Exception e) {
			return 0;
		} finally {
			pm.close();
		}
	}
	
	@Override
	public void atualizaTentativas (int fraseId, int contadores[]) {
		
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
	
}
