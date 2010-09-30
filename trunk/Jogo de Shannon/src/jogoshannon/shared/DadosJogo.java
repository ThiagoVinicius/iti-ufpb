package jogoshannon.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DadosJogo implements Serializable {

    private static final long serialVersionUID = -6819540404372323371L;

    public List<Integer> exibirLetras;
    public ConjuntoFrasesStub conjuntoFrases;
    public long idUsuario;

    private DadosJogo() {
    }

    public DadosJogo(List<Integer> exibirLetras,
            ConjuntoFrasesStub conjuntoFrases, long idUsuario) {
        this.exibirLetras = new ArrayList<Integer>(exibirLetras);
        this.idUsuario = idUsuario;
        this.conjuntoFrases = conjuntoFrases;
    }

    public List<String> getFrases() {
        return conjuntoFrases.frases;
    }

}
