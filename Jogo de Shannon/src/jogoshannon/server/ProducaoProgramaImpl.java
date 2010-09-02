package jogoshannon.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jogoshannon.client.ProducaoPrograma;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.FraseStore;
import jogoshannon.shared.ConjuntoFrasesStub;
import jogoshannon.shared.Frase;

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
                resultado[i] = new ConjuntoFrasesStub(
                        cur.getKey().getId(), cur.getDescricao());
            }
            
            logger.info("Retornando {} conjuntos de frases.", resultado.length);
            
            return resultado;
        } finally {
            pm.close();
        }
        
    }
    
}
