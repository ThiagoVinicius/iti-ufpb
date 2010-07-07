package jogoshannon.shared;

import java.io.Serializable;

public class Tentativas implements Serializable {
	
	public int contagens[];
	
	private Tentativas () {
		this(Frase.QUANTIDADE_LETRAS.length);
	}
	
	public Tentativas (int dados[]) {
		this(dados.length);
		
		//equivalente a:
		//contagens = dados.clone();
		for (int i = 0; i < contagens.length; ++i) {
			contagens[i] = dados[i];
		}
	}
	
	public Tentativas (int contagemDesafios) {
		contagens = new int[contagemDesafios];
	}
	
}