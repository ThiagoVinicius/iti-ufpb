package jogoshannon.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import jogoshannon.client.JuizSoletrando;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class JuizSoletrandoImpl extends RemoteServiceServlet implements JuizSoletrando {
	
	@Override
	public void init() throws ServletException {
		super.init();
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public long getId() {
		
		HttpSession sessao = getThreadLocalRequest().getSession();
		Long idSessao = (Long) sessao.getAttribute("id_sessao");
		if (idSessao == null) {
			idSessao = maisMaisContador();
			sessao.setAttribute("id_sessao", idSessao);
		}
		
		return idSessao;
	}
	
	private long maisMaisContador () {
		PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
		ContadorIds contador = null;
		try {
			contador = pm.getObjectById(ContadorIds.class, ContadorIds.CHAVE);
		} catch (Exception e) {
			//primeira vez que estamos rodando.
			contador = new ContadorIds();
			contador.set(0L);
			pm.makePersistent(contador);
		} finally {
			contador.set(contador.get() + 1);
			pm.close();
		}
		
		return contador.get();
	}
	
}
