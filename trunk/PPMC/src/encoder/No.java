/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package encoder;

import java.util.ArrayList;
import java.util.Collection;

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

    public ArrayList<Character> getSimbolosFilhos() {
        ArrayList<Character> simbolos = new ArrayList<Character>();
        for (No no : filhos) {
            simbolos.add(no.simbolo);
        }
        return simbolos;
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

    public int getFrequenciaFilhos() {
        int retorno = 0;
        for (No no : filhos) {
            retorno += no.contador;
        }
        return retorno;
    }

    public int getFrequenciaFilhos(Collection<Character> simbolosExcluidos) {
        int retorno = 0;
        for (No no : filhos) {
            if (!simbolosExcluidos.contains(no.simbolo)) {
                retorno += no.contador;
            }
        }
        return retorno;
    }

    public int getFrequenciaAte(char simbolo) {
        int retorno = 0;
        for (No no : filhos) {
            if (no.simbolo == simbolo) {
                break;
            }
            retorno += no.contador;
        }
        return retorno;
    }

    public int getQuantidadeFilhos() {
        //se o escape tem frequencia igual ao tamanho do alfabeto (nenhum simbolo novo aparecera)
        //retorna 0
        if (filhos.size() == 256) {
            return 0;
        }
        return filhos.size();
    }

//    public int getQuantidadeFilhos(ArrayList<Character> simbolosExcluidos) {
//        int retorno = 0;
//        for (No no : filhos) {
//            if (!simbolosExcluidos.contains(no.simbolo)) {
//                retorno++;
//            }
//        }
//
//        return retorno;
//    }
}
