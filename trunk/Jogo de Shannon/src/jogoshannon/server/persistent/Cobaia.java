package jogoshannon.server.persistent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.listener.StoreCallback;

import jogoshannon.server.GestorPersistencia;
import jogoshannon.shared.CobaiaStub;
import jogoshannon.shared.Tentativas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable(detachable="true")
public class Cobaia implements StoreCallback, Comparable<Cobaia> {

    @NotPersistent
    private static final Logger logger = LoggerFactory.getLogger(Cobaia.class);
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent(serialized = "true")
    private RegistroRodadas desafios;
    
    @Persistent
    private Date lastModified;
    
    @Persistent
    private Key experimento;
    
    @Persistent
    private Key conjuntoFrases;
    
    public Cobaia () {
    }
    
    public Cobaia (CobaiaStub copiar) {
        experimento = KeyFactory.createKey(Experimento.class.getSimpleName(), 
                copiar.getExperimentoId());
        conjuntoFrases = KeyFactory.createKey(ConjuntoFrases.class.getSimpleName(), 
                copiar.getConjuntoFrasesId());
        List<Rodada> rodadas = getDesafiosImpl();
        for (Tentativas t : copiar.getDesafios()) {
            rodadas.add(new Rodada(t));
        }
    }
    
    
    public long getIdSessao() {
        return key.getId();
    }

    public void setIdSessao(long idSessao) {
        key = KeyFactory.createKey(Cobaia.class.getSimpleName(), idSessao);
    }

    public Key getKey() {
        return key;
    }
    
    private synchronized List<Rodada> getDesafiosImpl () {
        if (desafios == null) {
            desafios = new RegistroRodadas();
        }
        return desafios.getRodadas();
    }
    
    public synchronized List<Rodada> getDesafios() {
        return Collections.unmodifiableList(getDesafiosImpl());
    }
    
    public synchronized void setDesafios (Collection<Rodada> novo) {
        List<Rodada> dados = getDesafiosImpl();
        desafios = desafios.copiaRasa();
        dados.clear();
        dados.addAll(novo);
    }
    
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getLastModified() {
        return lastModified;
    }
    
    public void setExperimento (Experimento exp) {
        experimento = exp.getKey();
    }
    
    public void setConjuntoFrases (ConjuntoFrases frases) {
        conjuntoFrases = frases.getKey();
    }
    
    public void setConjuntoFrasesKey (Key frases) {
        conjuntoFrases = frases;
    }
    
    public Key getConjuntoFrasesKey () {
        return conjuntoFrases;
    }
    
    public ConjuntoFrases getFrases() {
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        ConjuntoFrases result = null;
        try {
            result = pm.getObjectById(ConjuntoFrases.class, conjuntoFrases);
        } finally {
            pm.close();
        }
        return result;
    }
    
    public ConjuntoFrases getFrases(PersistenceManager pm) {
        return pm.getObjectById(ConjuntoFrases.class, conjuntoFrases);
    }


    @Override
    public String toString() {
        return "(" + getIdSessao() + ") " + getDesafios();
    }
    
    @Override
    public void jdoPreStore() {
        logger.debug("Usuario [{}] sendo armazenado. Atualizando timestamp.", key);
        if (desafios != null && desafios.getRodadas().isEmpty()) {
            desafios = null;
        }
        setLastModified(new Date());
    }

    @Override
    public int compareTo(Cobaia o) {
        if (o == null) {
            return -1;
        } else if (this.lastModified == o.lastModified && lastModified == null) {
            return 0;
        } else if (this.lastModified != null && o.lastModified == null) {
            return -1;
        } else if (this.lastModified == null && o.lastModified != null) {
            return 1;
        } else if (this.lastModified.before(o.lastModified)) {
            return -1;
        } else if (this.lastModified.after(o.lastModified)) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public CobaiaStub toStub () {
        CobaiaStub resultado = new CobaiaStub();
        
        resultado.setConjuntoFrasesId(conjuntoFrases.getId());
        List<Tentativas> nDesafios = new ArrayList<Tentativas>();
        for (Rodada r : getDesafios()) {
            nDesafios.add(r.toStub());
        }
        resultado.setDesafios(nDesafios);
        resultado.setExperimentoId(experimento.getId());
        resultado.setId(key.getId());
        
        return resultado;
    }
    
}
