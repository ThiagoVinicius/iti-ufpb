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
public class ArvoreBinaria
{
    private char[] criaAlfabeto(String sequencia)
    {
        String letras = "";

        while(!sequencia.equals(""))
        {
            letras = letras + sequencia.charAt(0);
            sequencia = sequencia.replaceAll(sequencia.charAt(0)+"", "");
        }

        return letras.toCharArray();
    }

    //public void criaTabela(No raiz, String[] tabela, char[] letras, String sequencia)
    private void criaTabela(No raiz, String[] tabela, String sequencia)
    {
        if (raiz.dir != null)
        {
            //criaTabela(raiz.dir, tabela, letras, sequencia+"1");
            criaTabela(raiz.dir, tabela, sequencia+"1");
            //criaTabela(raiz.esq, tabela, letras, sequencia+"0");
            criaTabela(raiz.esq, tabela, sequencia+"0");
        }
        else 
        {
            tabela[raiz.letra] = sequencia;
        }
    }

    /**
     *
     * @param sequencia
     * @param letras
     * @return Retorna a quantidade de ocorrencia de cada letra na sequencia
     */
    private int[] pegaTamanho(String sequencia, char[] letras)
    {
        int[] tamanhos = new int[letras.length];

        int tam = sequencia.length();
        
        for(int i = 0; i < letras.length; i++)
        {
            tamanhos[i] = tam - sequencia.replaceAll(letras[i]+"", "").length();
        }
        
        return tamanhos;
    }

    /**
     * Cria todos os nos folhas em ordem crescente
     * @param letras
     * @param tamanho
     * @return Sequencia de nos folha
     */
    private No[] criaNosFolha(char[] letras, int[] tamanho)
    {
        No[] nosFolhas = new No[letras.length];
        No aux;

        nosFolhas[0] = new No(tamanho[0], letras[0], true);

        for (int i = 1; i < nosFolhas.length; i++)
        {
            aux = new No(tamanho[i], letras[i], true);

            nosFolhas[i] = aux;

            for(int j = 0; j < i; j++)
            {
                if (aux.soma < nosFolhas[j].soma)
                {
                    for (int k = i; k > j; k--) {
                        nosFolhas[k] = nosFolhas[k -1];
                        nosFolhas[k - 1] = null;
                    }

                    nosFolhas[j] = aux;
                    
                    break;
                }
            }
        }

        return nosFolhas;
    }

    private No criaNoIntermediario(No esq, No dir)
    {
        No noPai = new No(esq.soma+dir.soma, '-', false);
        noPai.insereNos(dir, esq);
        return noPai;
    }

    private No criaArvore(No[] nos)
    {
        for(int i = 0; i < nos.length-1; i++ )
        {
            No filho1 = nos[i];
            No filho2 = nos[i+1];
            
            //System.out.println(nos[i].letra + " = " + nos[i].soma);

            No pai = criaNoIntermediario(filho1, filho2);
            nos[i] = null;
            nos[i+1] = null;
            nos = insereNoPai(nos, pai);
        }

        return nos[nos.length-1];
    }

    private No[] insereNoPai(No[] nos, No pai)
    {
        for(int i = 0; i < nos.length; i++)
        {
            if(nos[i] != null)
            {
                for(int j = i; j < nos.length; j++)
                {
                    if(pai.soma <= nos[j].soma)
                    {
                        for(int k = i-1; k < j-1; k++)
                        {
                            nos[k] = nos[k+1];
                            nos[k+1] = null;
                        }

                        nos[j-1] = pai;
                        pai = null;
                        return nos;
                    }
                }

                for (int k = i - 1; k < nos.length-1; k++) {
                    nos[k] = nos[k + 1];
                    nos[k + 1] = null;
                }

                nos[nos.length-1] = pai;
                pai = null;
                return nos;
            }
        }

        nos[nos.length - 1] = pai;
        pai = null;
        
        return nos;
    }

    private void imprimir(No raiz)
    {
        if(raiz != null)
        {
            System.out.println(raiz.letra + " = " + raiz.soma);
            imprimir(raiz.esq);            
            imprimir(raiz.dir);
        }
        else
        {
            System.out.println(" fim ");
        }
    }

     String[] tabela;

    public String[] constroiArvore(char[] letras, int[] tamanho)
    {
        tabela = new String[257];
        No[] nos = criaNosFolha(letras, tamanho);
        No raiz = criaArvore(nos);
        criaTabela(raiz, tabela, "");

        for(int i= 0; i < 257; i++)
        {
            //System.out.println(tabela[i]);
        }

        return tabela;
    }
    
    public static void main(String[] args)
    {
        ArvoreBinaria arv = new ArvoreBinaria();

        String palavra = "this is an example of a huffman tree";
        palavra = "abracadabra";
        palavra = "aaaaaaaaaaaaaaaaaaaaaa" +
                   "bbbbbbbbb"+
                   "cccccccccc"+
                   "dddddddddddd"+
                   "eeeeeeeeeeeeeeee"+
                   "rrrrrrrr";
        
        palavra = "COMMENT_CA_MARCHE";

        char[] letras = arv.criaAlfabeto(palavra);
        int[] tamanho = arv.pegaTamanho(palavra, letras);
        No[] nos = arv.criaNosFolha(letras, tamanho);

        String[] tabela = new String[256];


        for(int i= 0; i < letras.length; i++)
        {
            System.out.println(nos[i].letra + " = " + nos[i].soma);
        }

        System.out.println("");
        
        No raiz = arv.criaArvore(nos);

        arv.criaTabela(raiz, tabela, "");

        arv.imprimir(raiz);

        for(int i= 0; i < letras.length; i++)
        {
            System.out.println(letras[i] + " = " + tabela[letras[i]]);
        }

        System.out.println("Resultado da codificacao:");

        int tam = 0;

        for(int i = 0; i < palavra.length(); i++)
        {
            System.out.print(tabela[palavra.charAt(i)]);
            tam = tam + tabela[palavra.charAt(i)].length();
        }

        System.out.println("\nTamanho = " + tam);
        
//        String pass = "1-2-3";
//
//        pass = pass.substring(0, pass.lastIndexOf("-"));
//
//        System.out.println("\nTamanho = " + pass);  

        
    }
}
