/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lzw;

/**
 *
 * @author Janduy
 */
public class Teste {
    
    public static void main(String[] args) throws Exception {

        String palavra = "accababac";
        String[] seq;
        //LZWAdaptativoComDeslocamento lzw = new LZWAdaptativoComDeslocamento();
        LZWAdaptativo lzw = new LZWAdaptativo();

        for(int i = 0; i < palavra.length(); i++)
        {
            lzw.codifica(palavra.charAt(i)+"");
            //lzw.dicionario.imprimeDicionario();
            //System.out.println("");
        }

        lzw.fimDaCodificacao();

        System.out.println(lzw.ss);

        System.out.println(lzw.dicionario.tamanhoDicionario());

        seq = lzw.ss.trim().split(" ");

        lzw = null;
        //lzw = new LZWAdaptativoComDeslocamento();
        lzw = new LZWAdaptativo();

        //System.out.println(lzw.dicionario.tamanhoDicionario());

        lzw.ss = "";

        lzw.inicioDaDecodificacao(seq[0]);
        //lzw.dicionario.imprimeDicionario();
        //System.out.println(lzw.ss);

        for(int i = 1; i < seq.length; i++)
        {
            //System.out.println("\ndeco " + seq[i]);
            lzw.decodifica(seq[i]);
            //lzw.dicionario.imprimeDicionario();
            //System.out.println(lzw.ss);
        }

        System.out.println(lzw.ss);
    }

}
