package jogoshannon.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import jogoshannon.shared.Frase;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class FraseStore {

    @SuppressWarnings("unused")
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    // @Persistent(mappedBy = "frase")
    // private Set<Desafio> desafios;

    @Persistent
    private String conteudo;

    public FraseStore(Frase frase) {
        conteudo = frase.getFrase();
    }

    public Frase getConteudo() {
        return new Frase(conteudo);
    }

    public void setConteudo(Frase conteudo) {
        this.conteudo = conteudo.getFrase();
    }

}
