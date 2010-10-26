package jogoshannon.server.persistent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import jogoshannon.shared.ConjuntoFrasesStub;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class ConjuntoFrases {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private volatile List<String> frases;
    
    @Persistent
    private Text descricao;
    
    public ConjuntoFrases () {
    }
    
    public ConjuntoFrases (ConjuntoFrasesStub stub) {
        setDescricao(stub.descricao);
        lazyGetFrases().addAll(stub.frases);
    }
    
    public Key getKey () {
        return key;
    }
    
    private synchronized List<String> lazyGetFrases () {
        if (frases == null) {
            frases = new ArrayList<String>();
        }
        return frases;
    }
    
    public List<String> getFrases () {
        return Collections.unmodifiableList(lazyGetFrases());
    }
    
    public boolean putFrase (String fra) {
        return lazyGetFrases().add(fra);
    }
    
    public boolean removeFrase (String fra) {
        return lazyGetFrases().remove(fra);
    }
    
    public String getDescricao () {
        if (descricao == null) {
            return null;
        } else {
            return descricao.getValue();
        }
    }
    
    public void setDescricao (String desc) {
        descricao = new Text(desc);
    }
    
    public ConjuntoFrasesStub toStub () {
        ConjuntoFrasesStub result = 
            new ConjuntoFrasesStub(
                    getKey().getId(), 
                    getDescricao(), 
                    getFrases());
        return result;
    }
    
    public static ConjuntoFrasesStub toStub (ConjuntoFrases alvo) {
        if (alvo == null) {
            return null;
        } else {
            return alvo.toStub();
        }
    }
    
}
