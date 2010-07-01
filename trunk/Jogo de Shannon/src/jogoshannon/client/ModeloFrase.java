package jogoshannon.client;

import com.google.gwt.event.shared.HandlerManager;

import jogoshannon.client.event.FraseCompletaEvent;
import jogoshannon.client.event.TentativaEvent;
import jogoshannon.shared.Frase;
import jogoshannon.shared.Tentativas;

public class ModeloFrase {
	
	private String frase;
	private int ponteiroLetra;
	private int contagemDesafios;
	private Tentativas tentativas;
	
	public ModeloFrase(String frase) {
		super();
		this.frase = frase;
		this.ponteiroLetra = 0;
		this.contagemDesafios = Frase.QUANTIDADE_LETRAS.length;
		this.tentativas = new Tentativas(contagemDesafios);
	}

	public void atualiza (char tentativa, HandlerManager eventos) {
		
		if (acabou()) {
			eventos.fireEvent(new FraseCompletaEvent());
			return; //nao ha o que fazer.
		}
		
		++tentativas.contagens[ponteiroLetra];
		int indice = Frase.QUANTIDADE_LETRAS[ponteiroLetra];
		char esperado = frase.charAt(indice);
		
		
		if (esperado == tentativa) {
			++ponteiroLetra;
			eventos.fireEvent(new TentativaEvent(true, tentativa));
		} else {
			eventos.fireEvent(new TentativaEvent(false, tentativa));
		}
		
		if (acabou()) {
			eventos.fireEvent(new FraseCompletaEvent());
		}
		
	}
	
	public String getFraseParcial () {
		int indice = Frase.QUANTIDADE_LETRAS[ponteiroLetra];
		return frase.substring(0, indice);
	}
	
	public boolean acabou () {
		return ponteiroLetra >= contagemDesafios;
	}
	
	public Tentativas getTentativas () {
		return tentativas;
	}
	
}
