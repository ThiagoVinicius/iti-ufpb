
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;

import outros.LeituraEscrita;

import decoder.ArithDecoderStream;
import decoder.PPMBinDecoder;

import encoder.ArithEncoderStream;
import encoder.PPMCEncoder;

public final class PPM_Main {

    private PPM_Main() {
    }
    static PrintWriter arquivoSalvar;
    static FileWriter writer;

    public static void main(String[] args) throws Exception {

        //String[] nomes = {"Ecg1_dec", "Ecg2_dec", "Ecg3_dec", "Ecg4_dec", "Emg_dec", "Emg2_dec", "Claudid3_dec", "ecg105_dec", "1hz_senoide_dec", "3canais_dec"};

        String[] nomes = { "faroestecaboclo" };
        gravatxt("Arquivo" + ";;" + "Contexto" + ";;" + "RC", "Resultados\\Tabela.csv");

        System.out.println("\ncomprimindo ... descomprimindo");

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j <= 9; j++) {

                File original = new File(nomes[i] + ".txt");//arquivo de entrada tem que ter tamanho par
                File comprimido = new File("Resultados\\" + nomes[i] + "_comprimido.txt");
                File descomprimido = new File("Resultados\\" + nomes[i] + "_descomprimido.txt");



                //System.out.println("\ncomprimindo " + nomes[i] + " para k = " + j);
                //bits da amostra
                //ele pega os n bits menos significativos
                int bits = 8;
                //tamanho do contexto
                int k = j;
                boolean gray = true;
                comprimir(original, comprimido, k, bits, gray);

                //System.out.println("descomprimindo");
                descomprimir(comprimido, descomprimido);

                if (comparar(original, descomprimido) == false) {
                    System.out.println("Diferente: " + nomes[i] + " k = " + j);
                }

                //RC(razao de compressao)
                //a RC leva em conta o numero de bits do sinal de entrada!!!
                //lembre disso na hora de calcular a RC do golomb-rice
                double bitsOrig = original.length() / 2 * bits;
                double bitsComp = comprimido.length() * 8;
                double rc = bitsOrig / bitsComp;
                //System.out.println("RC: " + rc);

                String rc1 = "_" + rc;

                gravatxt(nomes[i] + ";;" + j + ";;" + rc1, "Resultados\\Tabela.csv");
            }

            gravatxt("", "Resultados\\Tabela.csv");
        }

        System.out.println("Fim");

    }

    public static void gravatxt(String x, String nome) {
        try {
            writer = new FileWriter(nome, true);
            arquivoSalvar = new PrintWriter(writer, true);
            //writer.close();
        } catch (Exception e) {
            System.exit(0);
        }

        if (x != null) {
            arquivoSalvar.println(x);
            arquivoSalvar.flush();
            arquivoSalvar.close();
        }
    }

    public static void comprimir(File original, File comprimido, int k, int bits, boolean gray) throws Exception {

        //abre os 2 arquivos
        DataInputStream input = LeituraEscrita.getInput(original);
        DataOutputStream output = LeituraEscrita.getOutput(comprimido);

        //divide por 2 pq sao 2 bytes
        int tam = (int) original.length() / 2;

        //salva o tamanho
        output.writeInt(tam);

        //salva o k
        output.writeByte(k);

        //cria o aritmetico
        ArithEncoderStream aritmetico = new ArithEncoderStream(output);

        //cria os PPMs de cada plano
        //todos os PPMs usam o mesmo aritmetico
        PPMCEncoder encoder = new PPMCEncoder(k, aritmetico);


        //comprime o arquivo
        for (int i = 0; i < tam; i++) {
            int simbolo = input.read();
            encoder.comprimeSimbolo((char)simbolo);

        }

        //depois que comprimiu tudo fecha o aritmetico
        aritmetico.flush();

        //fecha os arquivos
        input.close();
        output.flush();//sempre de flush em output antes do close
        output.close();

    }

    public static void descomprimir(File comprimido, File descomprimido) throws Exception {

        if (true)
            return;

        //abre os 2 arquivos
        DataInputStream input = LeituraEscrita.getInput(comprimido);
        DataOutputStream output = LeituraEscrita.getOutput(descomprimido);

        //le o tamanho
        //se vc salvou 'int' deve ler 'int' tb
        int tam = input.readInt();

        //le o k
        //se vc salvou 'byte' deve ler 'byte'
        int k = input.readUnsignedByte();

        //cria o aritmetico
        ArithDecoderStream aritmetico = new ArithDecoderStream(input);

        //cria os PPMs de cada plano
        //todos os PPMs usam o mesmo aritmetico
        PPMBinDecoder decoder = new PPMBinDecoder(k, aritmetico);

        //descomprime o arquivo
        for (int i = 0; i < tam; i++) {

            int resultado = decoder.descomprimeBit();

            //salva os bytes invertidos no descomprimido
            output.write(resultado);

        }

        //fecha os arquivos
        input.close();
        output.flush();//sempre de flush em output antes do close
        output.close();
    }

    public static boolean comparar(File f1, File f2) throws Exception {
        if (f1.length() != f2.length()) {
            return false;
        }
        DataInputStream input1 = LeituraEscrita.getInput(f1);
        DataInputStream input2 = LeituraEscrita.getInput(f2);
        int tam = (int) f1.length();
        for (int i = 0; i < tam; i++) {
            if (input1.read() != input2.read()) {
                return false;
            }
        }
        return true;
    }
}
