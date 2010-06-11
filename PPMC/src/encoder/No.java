/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package encoder;

import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class No {
    private int contador;
    private final char simbolo;
    private ArrayList<No> filhos;

    public No(int counter, char symbol) {
        contador = counter;
        simbolo = symbol;
        filhos = new ArrayList<No>();
    }

    public void incrementaContador() {
        contador++;
    }

    public int getContador() {
        return contador;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public ArrayList<No> getFilhos() {
        return filhos;
    }

    public No getFilho(char simbolo) {
        int tamanho = filhos.size();
        
        for (int i = 0; i < tamanho; i++) {
            if (filhos.get(i).simbolo == simbolo) {
                return filhos.get(i);
            }
        }
        return null;
    }

    public boolean temFilhos() {
        return !filhos.isEmpty();
    }

    public void adicionaFilho(char simbolo) {
        filhos.add(new No(1, simbolo));
    }
}
