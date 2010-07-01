package jogoshannon.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerManager;

import jogoshannon.client.event.JogoCompletoEvent;
import jogoshannon.shared.Frase;
import jogoshannon.shared.Tentativas;

public class ModeloJogoDeShannon {

	private List<ModeloFrase> frases;
	private int ponteiroFrase;
	
	public ModeloJogoDeShannon (Frase frasesIniciais[]) {
		this.frases = new ArrayList<ModeloFrase>(); //frases.getFrases();
		for (int i = 0; i < frasesIniciais.length; ++i) {
			this.frases.add(new ModeloFrase(frasesIniciais[i].getFrase()));
		}
		ponteiroFrase = 0;
	}
	
	public void atualiza (char tentativa, HandlerManager eventos) {
		//jogo havia acabado antes
		if (acabou()) {
			eventos.fireEvent(new JogoCompletoEvent());
			return;
		}
		
		ModeloFrase atual = frases.get(ponteiroFrase);
		
		atual.atualiza(tentativa, eventos);
		
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
			return null;
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
