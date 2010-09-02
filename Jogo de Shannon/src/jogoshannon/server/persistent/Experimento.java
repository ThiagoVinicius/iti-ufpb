package jogoshannon.server.persistent;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import jogoshannon.server.GestorPersistencia;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Experimento {
    
    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Text descricao;
    
    @Persistent
    private volatile SortedSet<Cobaia> cobaias;
    
    @Persistent
    private Key frases;

    public long getId() {
        return key.getId();
    }
    
    public Key getKey() {
        return key;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = new Text(descricao);
    }

    public String getDescricao() {
        return descricao.getValue();
    }

    private synchronized SortedSet<Cobaia> lazyGetCobaias() {
        if (cobaias == null) {
            cobaias = new TreeSet<Cobaia>();
        }
        return cobaias;
    }
    
    public SortedSet<Cobaia> getCobaias() {
        return Collections.unmodifiableSortedSet(lazyGetCobaias());
    }
    
    public boolean addCobaia (Cobaia cob) {
        return lazyGetCobaias().add(cob);
    }
    
    public boolean removeCobaia (Cobaia cob) {
        return lazyGetCobaias().remove(cob);
    }

    public void setFrases(ConjuntoFrases frases) {
        this.frases = frases.getKey();
    }

    public ConjuntoFrases getFrases() {
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        ConjuntoFrases result = pm.getObjectById(ConjuntoFrases.class, frases);
        pm.close();
        return result;
    }
    
}
