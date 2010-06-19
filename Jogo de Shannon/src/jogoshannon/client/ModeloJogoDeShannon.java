package jogoshannon.client;

import com.google.gwt.event.shared.HandlerManager;

import jogoshannon.client.event.JogoCompletoEvent;
import jogoshannon.shared.Frase;

public class ModeloJogoDeShannon {

	private ModeloFrase frases[];
	private int ponteiroFrase;
	
	public ModeloJogoDeShannon (Frase frases[]) {
		this.frases = new ModeloFrase [frases.length]; //frases.getFrases();
		for (int i = 0; i < frases.length; ++i) {
			this.frases[i] = new ModeloFrase(frases[i].getFrase());
		}
		ponteiroFrase = 0;
	}
	
	public void atualiza (char tentativa, HandlerManager eventos) {
		//jogo havia acabado antes
		if (acabou()) {
			eventos.fireEvent(new JogoCompletoEvent());
			return;
		}
		
		frases[ponteiroFrase].atualiza(tentativa, eventos);
		
		//frase acabou de acabar
		if (frases[ponteiroFrase].acabou()) {
			++ponteiroFrase;
		}
		
		//jogo acabou de acabar
		if (acabou()) {
			eventos.fireEvent(new JogoCompletoEvent());
		}
		
	}
	
	public boolean acabou () {
		return ponteiroFrase >= frases.length;
	}
	
}
