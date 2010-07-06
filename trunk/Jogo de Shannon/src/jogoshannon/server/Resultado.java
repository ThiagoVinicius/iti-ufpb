package jogoshannon.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Resultado extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		
		out.println("<html>");
	    out.println("<head><title>Hello World</title></title>");
	    out.println("<body>");
	    
	    PersistenceManager pm = null;
	    
	    try {
	    	Key id = KeyFactory.createKey(Usuario.class.getSimpleName(),
	    		 		new Long( req.getParameter("id")));
	    
	    	pm = GestorPersistencia.get().getPersistenceManager();
	    	Usuario fulano = pm.getObjectById(Usuario.class, id);
	    	
	    	out.println("Dados do usuário: " + fulano.toString());
	    	
	    } catch (Exception e) {
	    	out.println("Houve um erro enquanto sua requisição era processada.");
	    } finally {
	    	if (pm != null) {
	    		pm.close();
	    	}
	    }
	    
	    
	    out.println("</body></html>");
		
	}
	

	
}
