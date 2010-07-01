package jogoshannon.shared;

import java.io.Serializable;

public class Frase implements Serializable {
	
	private static final long serialVersionUID = -7515671084652941703L;

	public static final int QUANTIDADE_LETRAS[] = {
		3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 50, 100, 150, 200
	};
	
	private String frase;
	
	public Frase () {
	}
	
	public Frase (String frase) {
		this.frase = frase;
	}
	
	public void setFrase (String frase) {
		this.frase = frase;
	}
	
	public String getFrase () {
		return frase;
	}
	
}
