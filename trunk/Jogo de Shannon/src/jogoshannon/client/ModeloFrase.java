package jogoshannon.client;

import java.util.List;

import jogoshannon.client.event.FraseCompletaEvent;
import jogoshannon.client.event.TentativaEvent;
import jogoshannon.shared.DadosJogo;
import jogoshannon.shared.Tentativas;

import com.google.gwt.event.shared.SimpleEventBus;

public class ModeloFrase {

    private String frase;
    private int ponteiroLetra;
    private int contagemDesafios;
    private Tentativas tentativas;
    private List<Integer> mostrarLetras;

    public ModeloFrase(String frase, DadosJogo jogo) {
        super();
        this.frase = frase;
        this.ponteiroLetra = 0;
        this.mostrarLetras = jogo.exibirLetras;
        this.contagemDesafios = this.mostrarLetras.size();
        this.tentativas = new Tentativas(contagemDesafios);
    }

    public boolean atualiza(char tentativa, SimpleEventBus eventos) {

        boolean result = false;

        if (acabou()) {
            eventos.fireEvent(new FraseCompletaEvent());
            return false; // nao ha o que fazer.
        }

        ++tentativas.contagens[ponteiroLetra];
        int indice = mostrarLetras.get(ponteiroLetra);
        char esperado = frase.charAt(indice);

        if (esperado == tentativa) {
            ++ponteiroLetra;
            eventos.fireEvent(new TentativaEvent(true, tentativa));
            result = true;
        } else {
            eventos.fireEvent(new TentativaEvent(false, tentativa));
        }

        if (acabou()) {
            eventos.fireEvent(new FraseCompletaEvent());
        }

        return result;

    }

    public String getFraseParcial() {
        int indice = mostrarLetras.get(ponteiroLetra);
        return frase.substring(0, indice);
    }

    public boolean acabou() {
        return ponteiroLetra >= contagemDesafios;
    }

    public Tentativas getTentativas() {
        return tentativas;
    }

}
