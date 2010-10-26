package jogoshannon.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExperimentoStub implements Serializable {

    private static final long serialVersionUID = 6198623605640057286L;

    private long id;
    
    private String descricao;
    
    private ConjuntoFrasesStub frases;
    
    private List<Integer> mostrarLetras;
    
    private List<Long> idCobaias;

    public long getId() {
        return id;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setFrases(ConjuntoFrasesStub frases) {
        this.frases = frases;
    }
    
    public synchronized List<Integer> getMostrarLetras () {
        if (mostrarLetras == null) {
            mostrarLetras = new ArrayList<Integer>();
        }
        return mostrarLetras;
    }

    public ConjuntoFrasesStub getFrases() {
        return frases;
    }

    public void setId(long id) {
        this.id = id; 
    }

    public List<Long> getIdCobaias() {
        return idCobaias;
    }
    
    public void setIdCobaias (List<Long> ids) {
        idCobaias = ids;
    }

}
