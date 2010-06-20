package jogoshannon.shared;

public class VerificadorDeCampo {
	
	public static boolean letraValida (char entrada) {
		entrada = Character.toUpperCase(entrada);
		boolean valida = entrada == ' ' || entrada == 'Ç' || ('A' <= entrada && entrada <= 'Z');
		return valida;
	}

	public static char normalizaLetra (char entrada) {
//		if (entrada == ' ') {
//			return '_';
//		} else {
			return Character.toUpperCase(entrada);
//		}
	}
	
}
