package jogoshannon.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jogoshannon.client.event.JogoCompletoEvent;
import jogoshannon.shared.DadosJogo;
import jogoshannon.shared.Tentativas;

import com.google.gwt.event.shared.SimpleEventBus;

public class ModeloJogoDeShannon {

    private List<ModeloFrase> frases;
    private int ponteiroFrase;
    private Set<Character> letrasUsadas;
    private DadosJogo jogo;

    public ModeloJogoDeShannon(DadosJogo jogo) {
        this.frases = new ArrayList<ModeloFrase>(); // frases.getFrases();
        this.jogo = jogo;
        for (String i : jogo.getFrases()) {
            this.frases.add(new ModeloFrase(i, jogo));
        }
        ponteiroFrase = 0;
        letrasUsadas = new HashSet<Character>();
    }

    public void atualiza(char tentativa, SimpleEventBus eventos) {
        // jogo havia acabado antes
        if (acabou()) {
            eventos.fireEvent(new JogoCompletoEvent());
            return;
        }

        // Letra ja havia sido tentada. Nada a fazer
        if (!letrasUsadas.add(tentativa)) {
            return;
        }

        ModeloFrase atual = frases.get(ponteiroFrase);
        boolean acertou = atual.atualiza(tentativa, eventos);

        if (acertou) {
            letrasUsadas.clear();
        }

        // frase acabou de acabar
        if (atual.acabou()) {
            ++ponteiroFrase;
        }

        // jogo acabou de acabar
        if (acabou()) {
            eventos.fireEvent(new JogoCompletoEvent());
        }

    }

    public boolean acabou() {
        return ponteiroFrase >= frases.size();
    }

    public String getFraseParcial() {
        if (acabou()) {
            return "";
        } else {
            return frases.get(ponteiroFrase).getFraseParcial();
        }
    }

    public void adicionaFrase(String novaFrase) {
        frases.add(new ModeloFrase(novaFrase, jogo));
    }

    public Tentativas getTentativas(int fraseId) {
        return frases.get(fraseId).getTentativas();
    }

    public Tentativas[] getTodasTentativas() {
        Tentativas resultado[] = new Tentativas[frases.size()];
        for (int i = 0; i < resultado.length; ++i) {
            resultado[i] = frases.get(i).getTentativas();
        }

        return resultado;
    }
    
    public String strWriteTentativas () {
        StringBuilder sb = new StringBuilder();
        sb.append(frases.size());
        sb.append(" ");
        for (int i = 0; i < frases.size(); ++i) {
            Tentativas tenta = frases.get(i).getTentativas();
            sb.append(tenta.contagens.length);
            sb.append(" ");
            for (int j : tenta.contagens) {
                sb.append(j);
                sb.append(" ");
            }
        }
        return sb.toString();
    }

}
