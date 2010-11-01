package jogoshannon.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConjuntoFrasesStub implements Serializable {
    
    private static final long serialVersionUID = 1309632903427567059L;

    public long id;
    
    public String descricao;
    
    public List<String> frases;

    @SuppressWarnings("unused")
    private ConjuntoFrasesStub () {
    }
    
    public ConjuntoFrasesStub(long id, String descricao, List<String> frases) {
        super();
        this.id = id;
        this.descricao = descricao;
        this.frases = new ArrayList<String>(frases);
    }

}
