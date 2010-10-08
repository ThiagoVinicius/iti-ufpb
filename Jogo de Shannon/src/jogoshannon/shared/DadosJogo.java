package jogoshannon.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DadosJogo implements Serializable {

    private static final long serialVersionUID = -367858356529248905L;
    
    public List<Integer> exibirLetras;
    public ConjuntoFrasesStub conjuntoFrases;
    public long idUsuario;
    public long idExperimento;

    private DadosJogo() {
    }

    public DadosJogo(List<Integer> exibirLetras,
            ConjuntoFrasesStub conjuntoFrases, long idUsuario, 
            long idExperimento) {
        this.exibirLetras = new ArrayList<Integer>(exibirLetras);
        this.idUsuario = idUsuario;
        this.conjuntoFrases = conjuntoFrases;
        this.idExperimento = idExperimento;
    }

    public List<String> getFrases() {
        return conjuntoFrases.frases;
    }

}
