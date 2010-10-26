package jogoshannon.server;

import java.util.List;
import java.util.UUID;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import jogoshannon.client.remote.ProducaoPrograma;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.Experimento;
import jogoshannon.server.persistent.Obra;
import jogoshannon.shared.ConjuntoFrasesStub;
import jogoshannon.shared.ExperimentoStub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProducaoProgramaImpl extends RemoteServiceServlet implements ProducaoPrograma {

    private static final Logger logger = LoggerFactory.getLogger(ProducaoProgramaImpl.class);
    
    @Override
    public ConjuntoFrasesStub[] getConjuntosFrases() {
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        
        Query consulta = pm.newQuery(ConjuntoFrases.class);

        List<ConjuntoFrases> resposta;
        try {
            resposta = (List<ConjuntoFrases>) consulta.execute();

            ConjuntoFrasesStub resultado[] = new ConjuntoFrasesStub[resposta.size()];
            for (int i = 0; i < resultado.length; ++i) {
                ConjuntoFrases cur = resposta.get(i);
                resultado[i] = cur.toStub();
            }
            
            logger.info("Retornando {} conjuntos de frases.", resultado.length);
            
            return resultado;
        } finally {
            pm.close();
        }
        
    }

    @Override
    public long putExperimento(ExperimentoStub exp) {
        
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        try {
            Experimento gravar = new Experimento(exp);
            pm.makePersistent(gravar);
            return gravar.getId();
        } finally {
            pm.close();
        }
        
    }

    @Override
    public String getUploadUrl(String titulo, String autor, String descricao) {
        logger.info("Executando getUploadUrl()");
        UUID uuid = UUID.randomUUID();
        String result = "/admin/upload/obra/"+uuid.toString();
        Obra obra = new Obra(titulo, autor, descricao, result);
        obra.setCharLen(-1L);
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        try {
            logger.info("Armazenando informacoes do upload: {}", obra);
            pm.makePersistent(obra);
            logger.info("Retornando url: {}", result);
            return result;
        } finally {
            pm.close();
        }
    }
    
    /**
     * A checagem é feita à nível de url. Se o usuário não for admin, a 
     * invocação deste método será abortada com um erro 403.
     */
    @Override
    public void checkAdmin() {
    }
    
}
