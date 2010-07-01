package jogoshannon.shared;

import java.io.Serializable;

public class Tentativas implements Serializable {
	
	public int contagens[];
	
	private Tentativas () {
		this(Frase.QUANTIDADE_LETRAS.length);
	}
	
	public Tentativas (int contagemDesafios) {
		contagens = new int[contagemDesafios];
	}
	
}
