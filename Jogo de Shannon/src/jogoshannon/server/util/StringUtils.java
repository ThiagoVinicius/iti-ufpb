package jogoshannon.server.util;

public class StringUtils {

    private static final char acentos[][] = { 
        { 'a',     'á', 'à', 'ã', 'ä', 'â' },
        { 'e',     'é', 'è', 'ë', 'ê' }, 
        { 'i',     'í', 'ì', 'ï', 'î' },
        { 'o',     'ó', 'ò', 'õ', 'ö', 'ô' }, 
        { 'u',     'ú', 'ù', 'ü', 'û' },
        { 'A',     'Á', 'À', 'Ã', 'Ä', 'Â' },
        { 'E',     'É', 'È', 'Ë', 'Ê' },
        { 'I',     'Í', 'Ì', 'Ï', 'Î' },
        { 'O',     'Ó', 'Ò', 'Õ', 'Ö', 'Ô' },
        { 'U',     'Ú', 'Ù', 'Ü', 'Û' },
        { 'Ç',     'ç' },
        { 'Ç',     'Ç' },
        { 'n',     'ñ' },
        { 'N',     'Ñ' },
    };
    
    private static final char ACENTOS_OTIMIZADO[] = new char[Character.MAX_VALUE + 1];
    
    static {
        init();
    }
    
    private static void init() {
        for (int i = 0; i < ACENTOS_OTIMIZADO.length; ++i) {
            ACENTOS_OTIMIZADO[i] = (char) i;
        }
        for (int j = 0; j < acentos.length; j++) {
            for (int k = 1; k < acentos[j].length; k++) {
                ACENTOS_OTIMIZADO[acentos[j][k]] = acentos[j][0];
            }
        }
    }

    public static String desacentua(String aDesacentuar) {

        char[] temp = aDesacentuar.toCharArray();
        return new String(desacentua(temp));

    }

     public static char desacentua(char aDesacentuar) {
         return ACENTOS_OTIMIZADO[aDesacentuar];
     }

    public static char[] desacentua(char[] acentuada) {

        for (int i = 0; i < acentuada.length; i++) { 
            acentuada[i] = ACENTOS_OTIMIZADO[acentuada[i]];
        }

        return acentuada;
    }

    public static boolean eEspaco(char sera) { // o nome significa: 'será?!'
        if (sera == ' ')
            return true;
        else
            return false;
    }

    public static boolean eVogal(char sera) {
        sera = desacentua(sera); // acentos atrapalham
        
        return 
            sera == 'a' || sera == 'A' || 
            sera == 'e' || sera == 'E' ||
            sera == 'i' || sera == 'I' || 
            sera == 'o' || sera == 'O' ||
            sera == 'u' || sera == 'U';

    }

    public static boolean eConsoante(char sera) {
        
        return Character.isLetter(sera) && !eVogal(sera);

    }

    public static String retiraVogais(String aRetirar) {
        StringBuffer temp = new StringBuffer(aRetirar);

        for (int i = 0; i < temp.length(); i++) {
            if (eVogal(temp.charAt(i))) {
                temp.deleteCharAt(i);
                i--;
            }
        }

        aRetirar = temp.toString();
        return aRetirar;
    }

    public static String espelho(String ordemCorreta) {
        char[] temp = ordemCorreta.toCharArray();
        return new String(espelho(temp));
    }

    public static char[] espelho(char[] ordemCorreta) {

        char[] ordemInversa = new char[ordemCorreta.length];
        for (int i = 0; i < ordemCorreta.length; i++)
            ordemInversa[i] = ordemCorreta[ordemCorreta.length - 1 - i];

        return ordemInversa;
    }

    public static int contaEspacos(String aContar) {
        int contador = 0;
        char[] temp = aContar.toCharArray();
        for (int i = 0; i < temp.length; i++)
            if (eEspaco(temp[i]))
                contador++;
        return contador;
    }

    public static int contaVogais(String aContar) {
        int contador = 0;
        char[] temp = aContar.toCharArray();
        for (int i = 0; i < temp.length; i++)
            if (eVogal(temp[i]))
                contador++;
        return contador;
    }

    public static int contaConsoantes(String aContar) {
        int contador = 0;
        char[] temp = aContar.toCharArray();
        for (int i = 0; i < temp.length; i++)
            if (eConsoante(temp[i]))
                contador++;
        return contador;
    }
    
    public static String normalizar (String opr) {
        StringBuilder result = new StringBuilder();
        
        boolean branco = true;
        for (char i : desacentua(opr.toCharArray())) {
            if (Character.isLetter(i)) {
                result.append(Character.toUpperCase(i));
                branco = false;
            } else if (branco == false) {
                result.append('_'); // ^^ 
                branco = true;
            }
        }
        
        return result.toString();
    }

}
