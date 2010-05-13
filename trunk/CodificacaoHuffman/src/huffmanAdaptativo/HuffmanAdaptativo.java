/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package huffmanAdaptativo;

import arvorebinaria.HuffmanPadrao;

/**
 *
 * @author Janduy
 */
public class HuffmanAdaptativo extends HuffmanPadrao
{

    public HuffmanAdaptativo() {
        sequencia = "";
        codificacao = new String[257];
    }



    @Override
    public String codificaPrimeiro(String c) {
        No esq = new No(1, (char)256, true);
        No dir = new No(1, c.charAt(0), true);

        //noPai = criaNoIntermediario(esq, dir);

        codificacao[256] = "0";
        codificacao[c.charAt(0)] = "1";

        addNovaLetra(c.charAt(0));

        return "0";
    }

    @Override
    public String codifica(String c) {

        if(existeNaArvore(c))
        {

        }
        else
        {
            addNovoSimbolo(c);
        }

        return "";
    }

    private No criaNoIntermediario(No esq, No dir)
    {
        No noPai = new No(esq.soma+dir.soma, '-', false);
        noPai.insereNos(dir, esq);
        return noPai;
    }

    private void addNovaLetra(char c)
    {
        sequencia += c;
    }

    public boolean existeNaArvore(String c)
    {
        return sequencia.contains(c);
    }

    private void addNovoSimbolo(String c) {

    }

    @Override
    public String decodificaPrimeiro(String c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String decodifica(String c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
