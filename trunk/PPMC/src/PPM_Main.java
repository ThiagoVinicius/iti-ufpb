
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;

import outros.LeituraEscrita;

import decoder.ArithDecoderStream;
import decoder.PPMCDecoder;

import encoder.ArithEncoderStream;
import encoder.PPMCEncoder;

public final class PPM_Main {

    private PPM_Main() {
    }
    static PrintWriter arquivoSalvar;
    static FileWriter writer;

    public static void main(String[] args) throws Exception {

        //String[] nomes = {"Ecg1_dec", "Ecg2_dec", "Ecg3_dec", "Ecg4_dec", "Emg_dec", "Emg2_dec", "Claudid3_dec", "ecg105_dec", "1hz_senoide_dec", "3canais_dec"};

        String[] nomes = { "bib", "book1", "book2", "geo", "news", "obj1", 
        				"obj2", "paper1", "paper2", "paper3", "paper4", "paper5",
        				"paper6", "pic", "progc", "progl", "progp", "trans" };
        gravatxt("Arquivo;;Contexto;;Tamanho original (bytes);;Tamanho comprimido (bytes);;RC;;Tempo de compressao (s);;Tempo de descompressao (s)", "Resultados/Tabela.csv");

        System.out.println("\ncomprimindo ... descomprimindo");

        for (int i = 0; i < nomes.length; i++) {
            for (int j = 0; j <= 4; j++) {
                int l = nomes[i].length() - 1;
                do {
                    l--;
                }
                while(l >= 0 && nomes[i].charAt(l) != '.');
                
                String termino = "";
                if (-1 != l) {
                	termino = nomes[i].substring(l + 1);
                }

                File original = new File(nomes[i]);//arquivo de entrada tem que ter tamanho par
                File comprimido = new File("Resultados/" + nomes[i] + j + "_comprimido.txt");
                File descomprimido = new File("Resultados/" + nomes[i] + "_descomprimido." + termino);

                //System.out.println("\ncomprimindo " + nomes[i] + " para k = " + j);
                //bits da amostra
                //ele pega os n bits menos significativos
                int bits = 8;
                //tamanho do contexto
                int k = j;
                boolean gray = true;
                
                long tStart;
                
                tStart = System.currentTimeMillis();
                comprimir(original, comprimido, k, bits, gray);
                float tCompressao = (System.currentTimeMillis() - tStart)/1000f;

                //System.out.println("descomprimindo");
                tStart = System.currentTimeMillis();
                descomprimir(comprimido, descomprimido);
                float tDesompressao = (System.currentTimeMillis() - tStart)/1000f;

                if (comparar(original, descomprimido) == false) {
                    System.out.println("Diferente: " + nomes[i] + " k = " + j);
                }

                //RC(razao de compressao)
                //a RC leva em conta o numero de bits do sinal de entrada!!!
                //lembre disso na hora de calcular a RC do golomb-rice
                long bitsOrig = original.length();
                long bitsComp = comprimido.length();
                double rc = bitsOrig / (double)bitsComp;
                //System.out.println("RC: " + rc);

                String rc1 = "_" + rc;
                
                String texto = String.format("%s;;%d;;%d;;%d;;%.5f;;%.5f;;%.5f", 
                		nomes[i], j, bitsOrig, bitsComp, rc, tCompressao, tDesompressao);
                gravatxt(texto, "Resultados/Tabela.csv");
            }

            gravatxt("", "Resultados/Tabela.csv");
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

        long tam = original.length();

        //salva o tamanho
        output.writeLong(tam);

        //salva o k
        output.writeByte(k);

        //cria o aritmetico
        ArithEncoderStream aritmetico = new ArithEncoderStream(output);

        //cria os PPMs de cada plano
        //todos os PPMs usam o mesmo aritmetico
        PPMCEncoder encoder = new PPMCEncoder(k, aritmetico);


        //comprime o arquivo
        for (long i = 0; i < tam; i++) {
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

        //abre os 2 arquivos
        DataInputStream input = LeituraEscrita.getInput(comprimido);
        DataOutputStream output = LeituraEscrita.getOutput(descomprimido);

        //le o tamanho
        //se vc salvou 'int' deve ler 'int' tb
        long tam = input.readLong();

        //le o k
        //se vc salvou 'byte' deve ler 'byte'
        int k = input.readUnsignedByte();

        //cria o aritmetico
        ArithDecoderStream aritmetico = new ArithDecoderStream(input);

        //cria os PPMs de cada plano
        //todos os PPMs usam o mesmo aritmetico
        PPMCDecoder decoder = new PPMCDecoder(k, aritmetico);

        //descomprime o arquivo
        for (long i = 0; i < tam; i++) {

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
