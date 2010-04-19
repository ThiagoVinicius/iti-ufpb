/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lzw;

/**
 *
 * @author Janduy
 */
public abstract class Dicionario {      

    public abstract int addDicionario(String frase);
    public abstract int addUltimoDicionario();
    public abstract void addLetra(String letra);

    public abstract int tamanhoDicionario();
    public abstract boolean contemFrase(String letra);
    public abstract boolean contemFrase(int cod);

    public abstract String addInicio(int cod);
    public abstract String addDicionario(int cod);
    public abstract String addLetra(int cod);

    public void imprimeDicionario(){}
}
