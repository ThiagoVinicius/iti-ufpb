package jogoshannon.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Usuario {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent(mappedBy = "usuario")
    private volatile List<Desafio> desafios;

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

}
