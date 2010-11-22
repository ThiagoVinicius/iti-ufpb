package jogoshannon.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.Tentativas;

public class ModeloResposta {

    private int tabelaOriginal[][];
    private double tabelaDistribuida[][];
    private Integer letras[];
    private Map<Integer, Double> entropiaMinima;
    private Map<Integer, Double> entropiaMaxima;
    private int total;
    
    private static final int MANTER_AO_DISTRIBUIR = 3;
    
    private static final double ENTROPIA_MIN_COMPUTADOR[] = {
            3.1600259355719564, 2.441825336632946, 2.062062873794058,
            1.7386289034350682, 1.4875990540830597, 1.333504854675903 };
    private static final double ENTROPIA_MAX_COMPUTADOR[] = {
            3.9644886965772352, 3.3758368895217634, 3.0531281084633104,
            2.734754534623812, 2.4777192559070302, 2.3089651529879633 };
    
    private static Map<Integer, Double> ENTROPIA_MAX_COMPUTADOR_MAPA;
    private static Map<Integer, Double> ENTROPIA_MIN_COMPUTADOR_MAPA;
    
    static {
        init();
    }
    
    private static void init() {
        ENTROPIA_MAX_COMPUTADOR_MAPA = new HashMap<Integer, Double>();
        ENTROPIA_MIN_COMPUTADOR_MAPA = new HashMap<Integer, Double>();
        for (int i = 0; i < ENTROPIA_MAX_COMPUTADOR.length; ++i) {
            ENTROPIA_MAX_COMPUTADOR_MAPA.put(i, ENTROPIA_MAX_COMPUTADOR[i]);
        }
        for (int i = 0; i < ENTROPIA_MIN_COMPUTADOR.length; ++i) {
            ENTROPIA_MIN_COMPUTADOR_MAPA.put(i, ENTROPIA_MIN_COMPUTADOR[i]);
        }
        ENTROPIA_MAX_COMPUTADOR_MAPA = Collections.unmodifiableMap(ENTROPIA_MAX_COMPUTADOR_MAPA);
        ENTROPIA_MIN_COMPUTADOR_MAPA = Collections.unmodifiableMap(ENTROPIA_MIN_COMPUTADOR_MAPA);
    }
    
    public ModeloResposta(ExperimentoStub exp) {
        letras = exp.getMostrarLetras().toArray(new Integer[0]);
        tabelaOriginal = new int[28][letras.length];
        tabelaDistribuida = new double[28][letras.length];
        entropiaMaxima = new HashMap<Integer, Double>();
        entropiaMinima = new HashMap<Integer, Double>();
    }

    public void adiciona(Tentativas[] tentativas) {
        total += tentativas.length;
        int[] contages;
        int valor;

        for (int i = 0; i < tentativas.length; i++) {
            contages = tentativas[i].contagens;

            for (int j = 0; j < contages.length; j++) {
                valor = Math.min(contages[j], 28);

                tabelaOriginal[valor - 1][j]++;
            }
        }
    }
    
    public void remove(Tentativas[] tentativas) {
        total -= tentativas.length;
        int[] contages;
        int valor;

        for (int i = 0; i < tentativas.length; i++) {
            contages = tentativas[i].contagens;

            for (int j = 0; j < contages.length; j++) {
                valor = Math.min(contages[j], 28);

                tabelaOriginal[valor - 1][j]--;
            }
        }    
    }

    public int getLinhaCount() {
        return tabelaOriginal.length;
    }

    public double[] getLinha(int linha) {
        return tabelaDistribuida[linha];
    }

    public Map<Integer, Double> getEntropiaMinima() {
        return entropiaMinima;
    }

    public Map<Integer, Double> getEntropiaMaxima() {
        return entropiaMaxima;
    }
    
    public static Map<Integer, Double> getEntropiaMinimaComputador() {
        return ENTROPIA_MIN_COMPUTADOR_MAPA;
    }
    
    public static Map<Integer, Double> getEntropiaMaximaComputador() {
        return ENTROPIA_MAX_COMPUTADOR_MAPA;
    }
    
    private void distribuirUniforme () {
        for (int i = 0; i < letras.length; ++i) {
            
            for (int j = 0 ; j < MANTER_AO_DISTRIBUIR; ++j) { 
                //copiando esses sem alterações
                tabelaDistribuida[j][i] = tabelaOriginal[j][i];
            }
            
            double total = 0.0d;
            int ultimoNaoZero = -1;
            for (int j = MANTER_AO_DISTRIBUIR; j < tabelaOriginal.length; ++j) {
                int valorOriginal = tabelaOriginal[j][i];
                total += valorOriginal;
                if (valorOriginal != 0) {
                    ultimoNaoZero = j;
                }
            }
            if (ultimoNaoZero != -1) { //somamos valores
                double divisor = ultimoNaoZero - MANTER_AO_DISTRIBUIR + 1;
                double valor = total/divisor;
                for (int j = MANTER_AO_DISTRIBUIR; j <= ultimoNaoZero; ++j) {
                    tabelaDistribuida[j][i] = valor;
                }
            }
        }
        
    }
    
    private void copiarSemDistribuir () {
        for (int i = 0; i < tabelaOriginal.length; ++i) {
            for (int j = 0; j < tabelaOriginal[i].length; ++j) {
                tabelaDistribuida[i][j] = tabelaOriginal[i][j];
            }
        }
    }
    
    public void calculaEntropia(boolean distribuir) {
        
        if (distribuir) {
            distribuirUniforme();
        } else {
            copiarSemDistribuir();
        }
        
        double entropiaMax;
        double entropiaMin;
        final double denominador = this.total;

        for (int n = 0; n < letras.length; n++) {
            entropiaMax = entropiaMin = 0;

            double atualInt;
            double atual;
            double proximo;
            int i;
            for (i = 0; i < tabelaDistribuida.length - 1; i++) {
                atualInt = tabelaDistribuida[i][n];
                atual = tabelaDistribuida[i][n] / denominador;
                proximo = tabelaDistribuida[i + 1][n] / denominador;

                entropiaMin += (i + 1) * (atual - proximo) * log2(i + 1);
                if (atualInt != 0.0d) {
                    entropiaMax += atual * log2(atual);
                }
            }

            atualInt = tabelaDistribuida[i][n];
            atual = tabelaDistribuida[i][n] / denominador;

            entropiaMin += (i + 1) * (atual) * log2(i + 1);
            if (atualInt != 0.0d) {
                entropiaMax += atual * log2(atual);
            }

            entropiaMaxima.put(letras[n], -entropiaMax);
            entropiaMinima.put(letras[n], entropiaMin);
        }
    }

    private static final double LOG_2 = Math.log(2);

    private static double log2(double x) {
        return Math.log(x) / LOG_2;
    }

}
