package jogoshannon.server.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
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
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        PersistenceManager pm = GestorPersistencia.get()
            .getPersistenceManager();
        
        boolean foi = false;
        try {
            
            Experimento exp = pm.getObjectById(Experimento.class, KeyFactory.stringToKey(expkey));
            ConjuntoFrases frases = exp.getFrases();
            Iterator<Desafio> desafios = pm.getExtent(Desafio.class).iterator();
            if (desafios.hasNext()) {
                Desafio atual = desafios.next();
                Key pai = atual.getKey().getParent();
                
                Query consultaIrmaos = pm.newQuery(Desafio.class);
                consultaIrmaos.setFilter(":p1.contains(key)");
                consultaIrmaos.setOrdering("key asc");
                List<Desafio> irmaos = (List<Desafio>) 
                    consultaIrmaos.execute(findChildren(pai));
                
                
                logger.info("Encontrados {} registros.", irmaos.size());
                List<Rodada> tentativas = new ArrayList<Rodada>();
                for (Desafio d : irmaos) {
                    logger.info("Copiando registro id = {}", d.getKey());
                    tentativas.add(new Rodada(d.getTentativas()));
                }
                
                Cobaia nova = new Cobaia();
                nova.setExperimento(exp);
                nova.setConjuntoFrases(frases);
                nova.getDesafios().addAll(tentativas);
                exp.addCobaia(nova);
                pm.makePersistent(nova);
                pm.deletePersistentAll(irmaos);
                pm.flush();
                foi = true;
            }
            
        } finally {
            pm.close();
        }
         
        logger.info("{} Usuarios copiados.", foi);
        
        if (foi) {
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
    
    private List<Key> findChildren (Key dad) {
        com.google.appengine.api.datastore.Query query = 
            new com.google.appengine.api.datastore.Query("Desafio", dad);
        query.setKeysOnly();
        //query.addSort("key");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery toRun = datastore.prepare(query);
        ArrayList<Key> result = new ArrayList<Key>();
        for (Entity i : toRun.asIterable()) {
            result.add(i.getKey());
        }
        return result;
    }
    
}
