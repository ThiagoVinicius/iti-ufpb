/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package huffmanAdaptativo;

/**
 *
 * @author Janduy e Thiago
 */
public class No
{
    public No esq;
    public No dir;

    public No noPai;

    public No suc;
    public No ant;


    public int soma;
    public char letra;
    public boolean folha;

    public No(int soma, char letra, boolean folha)
    {
        this.soma = soma;
        this.letra = letra;
        this.folha = folha;

        esq = null;
        dir = null;
    }
    
    public void insereNos(No dir, No esq)
    {
        this.dir = dir;
        this.esq = esq;
    }

    public void modificaSuc(No suc)
    {
        this.suc = suc;
    }
}
