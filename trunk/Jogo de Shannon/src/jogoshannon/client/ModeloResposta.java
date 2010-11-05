package jogoshannon.client;

import java.util.List;

import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.Tentativas;

public class ModeloResposta {

    private int tabela[][];
    private double entropiaMinima[];
    private double entropiaMaxima[];
    private int total;

    public ModeloResposta(ExperimentoStub exp) {
        List<Integer> letras = exp.getMostrarLetras();
        tabela = new int[28][letras.size()];
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

                tabela[valor - 1][j]++;
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

                tabela[valor - 1][j]--;
            }
        }    
    }

    public int getLinhaCount() {
        return tabela.length;
    }

    public int[] getLinha(int linha) {
        return tabela[linha];
    }

    public double[] getEntropiaMinima() {
        return entropiaMinima;
    }

    public double[] getEntropiaMaxima() {
        return entropiaMaxima;
    }
    
    public void calculaEntropia() {
        double entropiaMax;
        double entropiaMin;
        final double denominador = this.total;

        for (int n = 0; n < entropiaMaxima.length; n++) {
            entropiaMax = entropiaMin = 0;

            int atualInt;
            double atual;
            double proximo;
            int i;
            for (i = 0; i < tabela.length - 1; i++) {
                atualInt = tabela[i][n];
                atual = tabela[i][n] / denominador;
                proximo = tabela[i + 1][n] / denominador;

                entropiaMin += (i + 1) * (atual - proximo) * log2(i + 1);
                if (atualInt != 0) {
                    entropiaMax += atual * log2(atual);
                }
            }

            atualInt = tabela[i][n];
            atual = tabela[i][n] / denominador;

            entropiaMin += (i + 1) * (atual) * log2(i + 1);
            if (atualInt != 0) {
                entropiaMax += atual * log2(atual);
            }

            entropiaMaxima[n] = -entropiaMax;
            entropiaMinima[n] = entropiaMin;
        }
    }

    private static final double LOG_2 = Math.log(2);

    private double log2(double x) {
        return Math.log(x) / LOG_2;
    }

}
