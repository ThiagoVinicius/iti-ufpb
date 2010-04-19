/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lzw;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Janduy
 */
public abstract class Tabela extends Dicionario{
    
    int saidaCodificacao;

    String saidaDecodificacao;

    String ss = "";
    ArrayList<String> dicionario;
    Hashtable<String, Posicoes> table;
    
    String frase = "";
    int codigo;
    
    public static class Posicoes
    {
        public int pos;

        public Posicoes(int pos) {
            this.pos = pos;
        }
    }

    public void imprimeDicionario()
    {
        
    }
    
}
