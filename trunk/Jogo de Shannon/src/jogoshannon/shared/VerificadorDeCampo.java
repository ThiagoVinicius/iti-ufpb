package jogoshannon.shared;

public class VerificadorDeCampo {
	
	public static boolean letraValida (char entrada) {
		entrada = Character.toUpperCase(entrada);
		boolean valida = entrada == ' ' || ('A' <= entrada && entrada <= 'Z');
		return valida;
	}

	public static char normalizaLetra (char entrada) {
		return Character.toUpperCase(entrada);
	}
	
}
