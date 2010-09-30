package jogoshannon.server.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jogoshannon.server.GestorPersistencia;
import jogoshannon.server.persistent.Cobaia;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.Desafio;
import jogoshannon.server.persistent.Experimento;
import jogoshannon.server.persistent.Rodada;
import jogoshannon.server.persistent.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.labs.taskqueue.TaskOptions.Method;

public class UsuariosParaCobaias extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuariosParaCobaias.class);
    
    private static final int MAX = 10;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        logger.info("Executando script atualização de banco de dados");

        
        
        String expkey = req.getParameter("expkey");
        if (expkey == null) {
            logger.error("Experimento nao especificado.");
            resp.setStatus(403);
            return;
        }
        
        PersistenceManager pm = GestorPersistencia.get()
            .getPersistenceManager();
        
        int i = 0;
        try {
            
            Experimento exp = pm.getObjectById(Experimento.class, KeyFactory.stringToKey(expkey));
            ConjuntoFrases frases = exp.getFrases();
            List<Cobaia> novasCobaias = new ArrayList<Cobaia>();
            for (Usuario u : pm.getExtent(Usuario.class)) {
                
                logger.info("Copiando 1 usuario.");
                
                List<Rodada> tentativas = new ArrayList<Rodada>();
                for (Desafio d : u.getDesafios()) {
                    tentativas.add(new Rodada(d.getTentativas()));
                }
                
                Cobaia nova = new Cobaia();
                nova.setExperimento(exp);
                nova.setConjuntoFrases(frases);
                nova.getDesafios().addAll(tentativas);
                pm.makePersistent(nova);
                pm.deletePersistent(u);
                pm.flush();
                
                ++i;
                if (i >= MAX) {
                    break;
                }
            }
        } finally {
            pm.close();
        }
         
        logger.info("{} Usuarios copiados.", i);
        
        if (i >= MAX) {
            logger.info("Execução terminada pelo limite de operacoes, " +
            		"adicionando nova tarefa para terminar o serviço.");
            Queue queue = QueueFactory.getDefaultQueue();
            queue.add(TaskOptions.Builder.
                          param("expkey", expkey).
                          method(Method.GET).
                          url("/tasks/usuarios-para-cobaias"));
        } else {
            logger.info("Execução completa");
        }
            
        
    }
    
}
