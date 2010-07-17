package jogoshannon.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jogoshannon.client.event.JogoCompletoEvent;
import jogoshannon.shared.Frase;
import jogoshannon.shared.Tentativas;

import com.google.gwt.event.shared.HandlerManager;

public class ModeloJogoDeShannon {

	private List<ModeloFrase> frases;
	private int ponteiroFrase;
	private Set<Character> letrasUsadas;
	
	public ModeloJogoDeShannon (Frase frasesIniciais[]) {
		this.frases = new ArrayList<ModeloFrase>(); //frases.getFrases();
		for (int i = 0; i < frasesIniciais.length; ++i) {
			this.frases.add(new ModeloFrase(frasesIniciais[i].getFrase()));
		}
		ponteiroFrase = 0;
		letrasUsadas = new HashSet<Character>();
	}
	
	public void atualiza (char tentativa, HandlerManager eventos) {
		//jogo havia acabado antes
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
		
		//frase acabou de acabar
		if (atual.acabou()) {
			++ponteiroFrase;
		}
		
		//jogo acabou de acabar
		if (acabou()) {
			eventos.fireEvent(new JogoCompletoEvent());
		}
		
	}
	
	public boolean acabou () {
		return ponteiroFrase >= frases.size();
	}
	
	public String getFraseParcial () {
		if (acabou()) {
			return "";
		} else {
			return frases.get(ponteiroFrase).getFraseParcial();
		}
	}
	
	public void adicionaFrase (Frase novaFrase) {
		frases.add(new ModeloFrase(novaFrase.getFrase()));
	}
	
	public Tentativas getTentativas (int fraseId) {
		return frases.get(fraseId).getTentativas();
	}
	
	public Tentativas[] getTodasTentativas () {
		Tentativas resultado[] = new Tentativas[frases.size()];
		for (int i = 0; i < resultado.length; ++i) {
			resultado[i] = frases.get(i).getTentativas();
		}
		
		return resultado;
	}

}
