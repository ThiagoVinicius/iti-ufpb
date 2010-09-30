package jogoshannon.server.cron;

import java.io.IOException;
import java.io.PrintWriter;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jogoshannon.server.GestorPersistencia;
import jogoshannon.server.persistent.FraseStore;
import jogoshannon.server.persistent.Usuario;

@SuppressWarnings("serial")
public class LimparTabelas extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LimparTabelas.class);
    
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        logger.info("Executando script limpeza de banco de dados");

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();

        try {

            //fraseStore = (List<FraseStore>) consulta.execute();
            while (pm.newQuery(FraseStore.class).deletePersistentAll() > 0);
            while (pm.newQuery(Usuario.class).deletePersistentAll() > 0);

            logger.info("Executado com sucesso");

        } catch (Exception e) {
            logger.error("Execucao falhou", e);
        } finally {
            pm.close();
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.print("<html><body></body></html>");
    }

}
