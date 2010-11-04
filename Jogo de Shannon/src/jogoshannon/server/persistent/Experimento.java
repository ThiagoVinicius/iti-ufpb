package jogoshannon.server.persistent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import jogoshannon.server.GestorPersistencia;
import jogoshannon.shared.ExperimentoStub;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(detachable="true")
public class Experimento {
    
    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Text descricao;
    
    @Persistent
    private volatile Set<Key> cobaias;
    
    @Persistent
    private Key frases;
    
    @Persistent
    private int contagemFrases;
    
    @Persistent
    private List<Integer> mostrarLetras;
    
    public Experimento () {
    }
    
    public Experimento (ExperimentoStub stub) {
        setDescricao(stub.getDescricao());
        setFrases(new ConjuntoFrases(stub.getFrases()));
    }

    public long getId() {
        return key.getId();
    }
    
    public Key getKey() {
        return key;
    }
    
    public void setDescricao(String descricao) {
        if (descricao != null) {
            this.descricao = new Text(descricao);
        } else {
            this.descricao = null;
        }
    }

    public String getDescricao() {
        if (descricao != null) {
            return descricao.getValue();
        } else { 
            return null;
        }
    }

    private synchronized Set<Key> lazyGetCobaias() {
        if (cobaias == null) {
            cobaias = new HashSet<Key>();
        }
        return cobaias;
    }
    
    @SuppressWarnings("unchecked")
    public List<Cobaia> getCobaias(PersistenceManager pm) {
        //TODO testar este método
        Query q = pm.newQuery(Cobaia.class);
        q.setFilter("experimento == minhaKey");
        q.declareParameters("String minhaKey");
        q.setOrdering("lastModified asc");
        
        return (List<Cobaia>) q.execute(getKey().toString());
    }
    
    public List<Long> getIdCobaias () {
        List<Long> result = new ArrayList<Long>();
        for (Key k : lazyGetCobaias()) {
            result.add(k.getId());
        }
        return result;
    }
    
    public boolean addCobaia (Cobaia cob) {
        boolean result = lazyGetCobaias().add(cob.getKey());
        if (result == true) {
            cob.setExperimento(this);
        }
        return result;
    }
    
    public boolean addCobaiaKey (Key cob) {
        return lazyGetCobaias().add(cob);
    }
    
    public boolean removeCobaia (Cobaia cob) {
        return lazyGetCobaias().remove(cob.getKey());
    }

    public void setFrases(ConjuntoFrases frases) {
        if (frases == null) {
            this.frases = null;
        } else {
            this.frases = frases.getKey();
            contagemFrases = frases.getFrases().size();
        }
    }
    
    private synchronized List<Integer> getMostrarLetrasImpl () {
        if (mostrarLetras == null) {
            mostrarLetras = new ArrayList<Integer>();
        }
        return mostrarLetras;
    }
    
    public synchronized List<Integer> getMostrarLetras () {
        return Collections.unmodifiableList(getMostrarLetrasImpl());
    }
    
    public synchronized void setMostrarLetras (Collection<Integer> novo) {
        List<Integer> alvo = getMostrarLetrasImpl();
        alvo.clear();
        alvo.addAll(novo);
    }
    
    public Key getFrasesKey () {
        return frases;
    }

    public ConjuntoFrases getFrases() {
        
        if (frases == null) {
            return null;
        }
        
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        ConjuntoFrases result = null;
        try {
            result = pm.getObjectById(ConjuntoFrases.class, frases);
        } finally {
            pm.close();
        }
        return result;
    }
    
    public ConjuntoFrases getFrases(PersistenceManager pm) {
        return pm.getObjectById(ConjuntoFrases.class, frases);
    }

    public ExperimentoStub toStub() {
        ExperimentoStub result = new ExperimentoStub();
        result.setDescricao(getDescricao());
        result.setFrases(ConjuntoFrases.toStub(getFrases()));
        result.setId(getId());
        result.setIdCobaias(getIdCobaias());
        result.getMostrarLetras().addAll(getMostrarLetras());
        return result;
    }

    public void setContagemFrases(int contagemFrases) {
        if (frases == null) {
            this.contagemFrases = contagemFrases;
        }
    }

    public int getContagemFrases() {
        return contagemFrases;
    }

}
