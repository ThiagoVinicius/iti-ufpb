/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package huffmanAdaptativo;

import arvorebinaria.*;

/**
 *
 * @author Janduy
 */
public class HuffmanAdaptativoSimples extends HuffmanPadrao
{
    private boolean[] lista = new boolean[257];
    private int[] simbolos = new int[257];
    private final int ESCAPE = 256;
    private final int LIMITE = 0;
    private int contador;

    ArvoreBinaria arvore;

    String seq;

    public HuffmanAdaptativoSimples()
    {
        sequencia = "";
        seq = "";
        contador = 0;
        codificacao = new String[257];
        lista = new boolean[257];

        for(int i = 0; i < lista.length; i++)
        {
            lista[i] = false;
            simbolos[i] = 0;
        }
    }
    
    @Override
    public String codificaPrimeiro(String c) 
    {
        sequencia = (char)256 + "";
        char simb = c.charAt(0);
        sequencia += simb;

        simbolos[ESCAPE]++;
        simbolos[simb]++;

        arvore = new ArvoreBinaria();
        criaContadores();
        codificacao = arvore.constroiArvore(sequencia.toCharArray(), con);

        return "";
    }

    @Override
    public String codifica(String c) 
    {
        char simb = c.charAt(0);
        String retorno;

        if(lista[simb] == false)
        {
            lista[simb] = true;
            retorno = codificacao[ESCAPE] ;
            simbolos[simb] = simbolos[simb]+1;
            simbolos[ESCAPE] = simbolos[ESCAPE]+1;
            sequencia = sequencia + simb;
        }
        else
        {
            retorno = codificacao[simb] ;
            simbolos[simb] = simbolos[simb]+1;
        }

        arvore = new ArvoreBinaria();
        criaContadores();
        codificacao = arvore.constroiArvore(sequencia.toCharArray(), con);

        return retorno;
    }

    int[] con;

    private void criaContadores()
    {
        con = new int[sequencia.length()+1];
        
        for(int i = 1; i < sequencia.length()+1; i++)
        {
            con[i] = simbolos[sequencia.charAt(i-1)];
        }

        con[0] = simbolos[256];
    }

    @Override
    public String decodificaPrimeiro(String c) {
        sequencia = (char)256 + "";
        char simb = c.charAt(0);
        sequencia += simb;

        simbolos[ESCAPE]++;
        simbolos[simb]++;

        arvore = new ArvoreBinaria();
        criaContadores();
        codificacao = arvore.constroiArvore(sequencia.toCharArray(), con);

        return "";
    }

    @Override
    public String decodifica(String c)
    {
        seq = seq + c.charAt(0);        

        for(int i = 0; i < codificacao.length; i++)
        {
            if (seq.equals(codificacao[i]))
            {
                char simb = c.charAt(0);
                String retorno;

                if (i == ESCAPE)
                {                    
                    simbolos[ESCAPE] = simbolos[ESCAPE] + 1;
                    seq = "";
                    return ((char)ESCAPE)+"";
                }
                else {
                    //retorno = codificacao[i];
                    simbolos[i] = simbolos[i] + 1;

                    seq = "";

                    arvore = new ArvoreBinaria();
                    criaContadores();
                    codificacao = arvore.constroiArvore(sequencia.toCharArray(), con);

                    return ((char)i)+"";
                }               
            }
        }

        return (char)257 + "";        
    }

    void novoChar(char c) {
        simbolos[c]++;
        sequencia += c;

        arvore = new ArvoreBinaria();
        criaContadores();
        codificacao = arvore.constroiArvore(sequencia.toCharArray(), con);
    }
}
