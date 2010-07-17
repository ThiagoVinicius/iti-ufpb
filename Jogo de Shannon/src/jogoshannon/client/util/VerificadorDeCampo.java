package jogoshannon.client.util;

import com.google.gwt.dom.client.NativeEvent;

public class VerificadorDeCampo {

    public static boolean letraValida(char entrada) {
        entrada = Character.toUpperCase(entrada);
        boolean valida = entrada == ' ' || entrada == 'Ç'
                || ('A' <= entrada && entrada <= 'Z');
        return valida;
    }

    public static char normalizaLetra(char entrada) {
        if (entrada == ' ') {
            return '_';
        } else {
            return Character.toUpperCase(entrada);
        }
    }

    public static boolean teclaModificadora(NativeEvent ne) {

        if (ne.getAltKey() || ne.getCtrlKey() || ne.getMetaKey()) {
            return true;
        }

        return false;
    }

}
