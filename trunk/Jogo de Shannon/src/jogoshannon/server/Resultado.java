package jogoshannon.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jogoshannon.server.persistent.Cobaia;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.Experimento;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class Resultado extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        PersistenceManager pm = null;
        
        String idString = req.getParameter("id");
        
        out.println("<html>");
        out.println("<head><title>Resultados para id = "+idString+"</title></head>");
        out.println("<body>");

        try {
            Key id = KeyFactory.createKey(Cobaia.class.getSimpleName(),
                    new Long(req.getParameter("id")));

            pm = GestorPersistencia.get().getPersistenceManager();
            Cobaia fulano = pm.getObjectById(Cobaia.class, id);
            ConjuntoFrases frases = fulano.getFrases(pm);
            Experimento experimento = fulano.getExperimento(pm);
            

            out.println("<h2>Dados de usuário:</h2><br/>"); 
            out.println("<h3>ID</h3>"+idString+"<br/>");
            out.printf("<h3>Experimento</h3>(ID = %s)<br/>", 
                    String.valueOf(experimento.getKey().getId()));
            out.println("<h3>Conjunto de frases utilizado</h3><br/>");
            out.println("&nbsp; - Descição: "+frases.getDescricao().replaceAll("\n", "<br/>"));
            out.println("&nbsp; - Frases utilizadas: "+frases.getFrases());
            out.println("<h3>Tentativas[frase]</h3>" + fulano.getDesafios());

        } finally {
            if (pm != null) {
                pm.close();
            }
        }
        
        out.println("</body></html>");

    }

}
