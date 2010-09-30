package jogoshannon.server.persistent;

import java.util.Arrays;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@Deprecated
@PersistenceCapable
public class Desafio {

    @SuppressWarnings("unused")
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @SuppressWarnings("unused")
    @Persistent
    private Usuario usuario;

    @SuppressWarnings("unused")
    @Persistent
    private FraseStore frase;

    @Persistent
    private int tentativas[];

    public Desafio(int numeroDesafios) {
        tentativas = new int[numeroDesafios];
    }

    public Desafio(int tentativas[]) {
        this.tentativas = tentativas;
    }

    public int[] getTentativas() {
        return tentativas;
    }

    public void setTentativas(int tentativas[]) {
        this.tentativas = tentativas;
    }

    public void somaTentativas(int somarCom[]) {
        for (int i = 0; i < somarCom.length; ++i) {
            tentativas[i] += somarCom[i];
        }
        // tocando na variável, dando a dica de que ela precisa ser salva.
        setTentativas(tentativas);
    }

    @Override
    public String toString() {
        return Arrays.toString(getTentativas());
    }
    
    public Rodada toRodada () {
        return new Rodada(tentativas);
    }
    
    
}
