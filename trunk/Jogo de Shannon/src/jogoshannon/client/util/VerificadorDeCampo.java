package jogoshannon.client.util;


public class VerificadorDeCampo {
	
	public static boolean letraValida (char entrada) {
		entrada = Character.toUpperCase(entrada);
		boolean valida = entrada == ' ' || entrada == 'Ç' || ('A' <= entrada && entrada <= 'Z');
		return valida;
	}

	public static char normalizaLetra (char entrada) {
		if (entrada == ' ') {
			return '_';
		} else {
			return Character.toUpperCase(entrada);
		}
	}
	
//	public static boolean devoProcessar (KeyPressEvent e) {
//		NativeEvent ne = e.getNativeEvent();
//		int keycode = ne.getKeyCode();
//		
//		if (e.isAltKeyDown() || e.isControlKeyDown() || e.isMetaKeyDown()) {
//			return false;
//		}
//		
//		return 65 <= keycode && keycode <= 90; 
//		
//		if (e.getCharCode() < 32) {
//			return false;
//		} 
//		
//		//teclas de funcao no firefox
//		if (e.getCharCode() >= 112 && e.getCharCode() <= 123) {
//			return false;
//		}
//		
//		GWT.log(""+ne.getKeyCode());
//		
//		return true;
//	}
	
}
