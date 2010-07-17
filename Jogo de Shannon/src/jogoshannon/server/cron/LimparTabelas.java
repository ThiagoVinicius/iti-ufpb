package jogoshannon.server.cron;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jogoshannon.server.FraseStore;
import jogoshannon.server.GestorPersistencia;
import jogoshannon.server.Usuario;

@SuppressWarnings("serial")
public class LimparTabelas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Logger.getLogger(LimparTabelas.class.getName()).log(Level.WARNING,
                "Executando script limpeza de banco de dados");

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();

        Query consulta = pm.newQuery(FraseStore.class);

        List<FraseStore> fraseStore;
        List<Usuario> usuario;
        try {

            fraseStore = (List<FraseStore>) consulta.execute();
            while (fraseStore.size() > 0) {
                pm.deletePersistentAll(fraseStore);
                pm.flush();
                fraseStore = (List<FraseStore>) consulta.execute();
            }

            usuario = (List<Usuario>) pm.newQuery(Usuario.class).execute();
            while (usuario.size() > 0) {
                pm.deletePersistentAll(usuario);
                pm.flush();
                usuario = (List<Usuario>) pm.newQuery(Usuario.class).execute();
            }

            Logger.getLogger(LimparTabelas.class.getName()).log(Level.WARNING,
                    "Executado com sucesso");

        } catch (Exception e) {
            Logger.getLogger(LimparTabelas.class.getName()).log(Level.WARNING,
                    "Execucao falhou", e);
        } finally {
            pm.close();
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.print("<html><body></body></html>");
    }

}
