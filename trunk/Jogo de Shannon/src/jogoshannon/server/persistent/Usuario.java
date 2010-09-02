package jogoshannon.server.persistent;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Usuario {

    @NotPersistent
    private static final Logger logger = LoggerFactory.getLogger(Usuario.class);
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent(mappedBy = "usuario")
    private volatile List<Desafio> desafios;
    
//    @Persistent (mappedBy="cobaias")
//    private Experimento experimento;

    public long getIdSessao() {
        return key.getId();
    }

    // public void setIdSessao(long idSessao) {
    // this.idSessao = idSessao;
    // }

    public Key getKey() {
        return key;
    }

    public synchronized List<Desafio> getDesafios() {
        if (desafios == null) {
            desafios = new ArrayList<Desafio>();
        }
        return desafios;
    }

    @Override
    public String toString() {
        return "(" + getIdSessao() + ") " + getDesafios();
    }
    
    public Cobaia toCobaia () {
        Cobaia result = new Cobaia();
        result.setIdSessao(this.getIdSessao());
        List<Rodada> res = result.getDesafios();
        for (Desafio d : desafios) {
            res.add(d.toRodada());
        }
        return result;
    }

}
