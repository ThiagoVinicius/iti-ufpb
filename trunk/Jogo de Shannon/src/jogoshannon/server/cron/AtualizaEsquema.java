package jogoshannon.server.cron;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jogoshannon.server.GestorPersistencia;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.Experimento;
import jogoshannon.server.persistent.ExperimentoDefault;
import jogoshannon.server.persistent.FraseStore;
import jogoshannon.server.persistent.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtualizaEsquema extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(AtualizaEsquema.class);
    
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        logger.info("Executando script atualização de banco de dados");

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();

        Query consulta = pm.newQuery(FraseStore.class);

        List<FraseStore> fraseStore;
        List<Usuario> usuario;
        ConjuntoFrases novo = null;
        Experimento novoExp = null;
        try {

            logger.info("Localizando todas as frases");
            fraseStore = (List<FraseStore>) consulta.execute();
            while (fraseStore.size() > 0) {
                novo = new ConjuntoFrases();
                novo.setDescricao("Conjunto de frases que já existiam.");
                
                for (FraseStore i : fraseStore) {
                    novo.putFrase(i.getConteudo().getFrase());
                }
                pm.makePersistent(novo);
                
                novoExp = new Experimento();
                novoExp.setDescricao("Experimento que já existia.");
                novoExp.setFrases(novo);
                pm.makePersistent(novoExp);
                
                ExperimentoDefault.setKey(novoExp.getKey());
                
                pm.deletePersistentAll(fraseStore);
                pm.flush();
                fraseStore = (List<FraseStore>) consulta.execute();
            }
            
            novoExp = ExperimentoDefault.getDefault(pm);
            
            usuario = (List<Usuario>) pm.newQuery(Usuario.class).execute();
            while (usuario.size() > 0) {
                
                //pm.detachCopyAll(usuario);
                for (Usuario u : usuario) {
                    novoExp.addCobaia(u.toCobaia());
                }
                pm.deletePersistentAll(usuario);
                pm.flush();
                usuario = (List<Usuario>) pm.newQuery(Usuario.class).execute();
            }

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
