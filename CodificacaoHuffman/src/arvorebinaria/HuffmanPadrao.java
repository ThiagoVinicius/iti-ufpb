/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package arvorebinaria;

import huffmanAdaptativo.No;

/**
 *
 * @author Janduy
 */
public abstract class HuffmanPadrao {

    public String sequencia;
    public String[] codificacao;

    //public No noPai;

    public abstract String codificaPrimeiro(String c);
    public abstract String codifica(String c);

    public abstract String decodificaPrimeiro(String c);
    public abstract String decodifica(String c);

}
