package jogoshannon.server.cron;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jogoshannon.server.GestorPersistencia;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.Experimento;
import jogoshannon.server.persistent.ExperimentoDefault;
import jogoshannon.server.persistent.FraseStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.labs.taskqueue.TaskOptions.Method;

public class AtualizaEsquema extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(AtualizaEsquema.class);
    
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        logger.info("Executando script atualização de banco de dados");

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();

        ConjuntoFrases novo = null;
        Experimento novoExp = null;
        try {

            logger.info("Localizando todas as frases");
            novo = new ConjuntoFrases();
            novo.setDescricao("Conjunto de frases que já existiam.");

            for (FraseStore i : pm.getExtent(FraseStore.class)) {
                novo.putFrase(i.getConteudo().getFrase());
            }
            pm.makePersistent(novo);
            
            logger.info("{} Frases antigas copiadas para um novo conjunto de frases", novo.getFrases().size());
            logger.info("Criando experimento para o novo conjunto de frases");
            

            novoExp = new Experimento();
            novoExp.setDescricao("Experimento que já existia.");
            novoExp.setFrases(novo);
            novoExp.getMostrarLetras().addAll(Arrays.asList(new Integer [] 
               { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 50, 100, 150, 200 }));
            pm.makePersistent(novoExp);
            
            logger.info("Novo experimento criado.");

            pm.flush();
            
            logger.info("Excluindo frases antigas.");
            //while (pm.newQuery(FraseStore.class).deletePersistentAll() > 0);
            logger.info("Feito.");
            
            Queue queue = QueueFactory.getDefaultQueue();
            logger.info("Disparando tarefa para copiar Usuarios antigos.");
            queue.add(TaskOptions.Builder.
                          param("expkey", KeyFactory.keyToString(novoExp.getKey())).
                          method(Method.GET).
                          url("/tasks/usuarios-para-cobaias"));
            
            logger.info("Criando experimento aletório.");
            Experimento expAleatorio = new Experimento();
            expAleatorio.setDescricao("Experimento aleatório");
            expAleatorio.getMostrarLetras().addAll(Arrays.asList(new Integer [] {5, 10, 15, 20, 25, 50, 100, 200}));
            expAleatorio.setFrases(null);
            expAleatorio.setContagemFrases(2);
            pm.makePersistent(expAleatorio);
            ExperimentoDefault.setKey(expAleatorio.getKey());
            logger.info("Criado. Marcando experimento aleatório como experimento padrão.");

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
