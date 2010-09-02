package jogoshannon.shared;

import java.io.Serializable;

public class ConjuntoFrasesStub implements Serializable {
    
    private static final long serialVersionUID = 1309632903427567059L;

    public long id;
    
    public String descricao;

    public ConjuntoFrasesStub(long id, String descricao) {
        super();
        this.id = id;
        this.descricao = descricao;
    }

}
