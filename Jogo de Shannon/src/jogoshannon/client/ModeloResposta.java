package jogoshannon.client;

import java.util.List;

import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.Tentativas;

public class ModeloResposta {

    private int tabelaOriginal[][];
    private double tabelaDistribuida[][];
    private double entropiaMinima[];
    private double entropiaMaxima[];
    private int total;
    
    private static final int MANTER_AO_DISTRIBUIR = 3;
    
    public ModeloResposta(ExperimentoStub exp) {
        List<Integer> letras = exp.getMostrarLetras();
        tabelaOriginal = new int[28][letras.size()];
        tabelaDistribuida = new double[28][letras.size()];
        entropiaMaxima = new double[letras.size()];
        entropiaMinima = new double[letras.size()];
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

    public double[] getEntropiaMinima() {
        return entropiaMinima;
    }

    public double[] getEntropiaMaxima() {
        return entropiaMaxima;
    }
    
    private void distribuirUniforme () {
        for (int i = 0; i < entropiaMaxima.length; ++i) {
            
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

        for (int n = 0; n < entropiaMaxima.length; n++) {
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

            entropiaMaxima[n] = -entropiaMax;
            entropiaMinima[n] = entropiaMin;
        }
    }

    private static final double LOG_2 = Math.log(2);

    private static double log2(double x) {
        return Math.log(x) / LOG_2;
    }

}
