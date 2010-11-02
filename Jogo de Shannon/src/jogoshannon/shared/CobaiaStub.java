package jogoshannon.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CobaiaStub implements Serializable {

    private static final long serialVersionUID = 1511590637648579025L;
    
    private long id;
    private List<Tentativas> desafios;
    private long experimentoId;
    private long conjuntoFrasesId;
    
    public CobaiaStub () {
    }

    public CobaiaStub(long id, List<Tentativas> desafios, long experimentoId,
            long conjuntoFrasesId) {
        this.id = id;
        this.desafios = new ArrayList<Tentativas>(desafios);
        this.experimentoId = experimentoId;
        this.conjuntoFrasesId = conjuntoFrasesId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Tentativas> getDesafios() {
        return desafios;
    }

    public void setDesafios(List<Tentativas> desafios) {
        this.desafios = desafios;
    }

    public long getExperimentoId() {
        return experimentoId;
    }

    public void setExperimentoId(long experimentoId) {
        this.experimentoId = experimentoId;
    }

    public long getConjuntoFrasesId() {
        return conjuntoFrasesId;
    }

    public void setConjuntoFrasesId(long conjuntoFrasesId) {
        this.conjuntoFrasesId = conjuntoFrasesId;
    }
    
}
