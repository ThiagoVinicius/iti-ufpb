/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lzw;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Janduy e Thiago
 */
public class DicionarioAdaptativo extends Tabela{

    public final int TAMANHO = 256;
    public final int LIMITE = 65536;//2097152;
    //private int cont;

    public DicionarioAdaptativo()
    {
        table = new Hashtable<String, Posicoes>();
        dicionario = new ArrayList<String>();
        criaRaiz();
        //cont = TAMANHO;
    }

    private void criaRaiz()
    {
        for(int i = 0; i < TAMANHO; i++)
        {
            dicionario.add((char)i+"");
            table.put((char)i+"", new Posicoes(i));
        }
    }

    @Override
    public void addLetra(String letra) {
        frase = frase+letra;
    }

    @Override
    public int addDicionario(String letra)
    {
        //saidaCodificacao = table.get(frase).pos;

        if(dicionario.size() < LIMITE)
        {
            dicionario.add(frase + letra);
            table.put((frase + letra), new Posicoes(dicionario.size()-1));
        }

        saidaCodificacao = table.get(frase).pos;
        
        frase = letra;

        return saidaCodificacao;
    }


    @Override
    public int addUltimoDicionario() {
        /*if(table.containsKey(frase))
        {
            return table.get(frase).pos;
        } 
        else
        {
            //saidaCodificacao = table.get(frase).pos;
            
            if (dicionario.size() == LIMITE) {

                if (dicionario.get(dicionario.size() - 1).equals(frase))
                {
                    table.remove(dicionario.get(dicionario.size() - 2));
                    table.put((frase), new Posicoes(dicionario.size() - 2));
                    dicionario.set(dicionario.size() - 2, frase);
                } else {
                    table.remove(dicionario.get(dicionario.size() - 1));
                    table.put((frase), new Posicoes(dicionario.size() - 1));
                    dicionario.set(dicionario.size() - 1, frase);
                }
            } else {
                dicionario.add(frase);
                table.put((frase), new Posicoes(dicionario.size()-1));

                //cont++;
            }
        }*/

        saidaCodificacao = table.get(frase).pos;
        return saidaCodificacao;
        
    }    

    @Override
    public String addInicio(int cod) {

        saidaDecodificacao = dicionario.get(cod);
        codigo = cod;
        return saidaDecodificacao;
    }

    @Override
    public String addDicionario(int cod) {

        //saidaDecodificacao = dicionario.get(cod);

        frase = dicionario.get(codigo);        

        char c = dicionario.get(cod).charAt(0);

        //System.out.println("entrei " + frase + c);

        if(dicionario.size() < LIMITE)
        {            
            dicionario.add(frase + c);
            table.put(frase + c, new Posicoes(dicionario.size() - 1));
        }

        saidaDecodificacao = dicionario.get(cod);

        //System.out.println("saida " +saidaDecodificacao);

        codigo = cod;

        return saidaDecodificacao;
    }

    @Override
    public String addLetra(int cod) {

        frase = dicionario.get(codigo);
        char c = dicionario.get(codigo).charAt(0);

        if(dicionario.size() < LIMITE)
        {            
            dicionario.add(frase + c);
            table.put(frase + c, new Posicoes(dicionario.size() - 1));
        }

        saidaDecodificacao = frase + c;
        
        codigo = cod;

        return saidaDecodificacao;
    }

    @Override
    public int tamanhoDicionario() {
        return dicionario.size();
    }

    @Override
    public boolean contemFrase(String letra) {
        return table.containsKey(frase+letra);
    }

    @Override
    public boolean contemFrase(int cod) {
        return table.containsKey(dicionario.get(cod));
    }

    @Override
    public void imprimeDicionario()
    {
        for(int i = 95; i < dicionario.size(); i++)
        {
            System.out.println(i + " = " + dicionario.get(i));
        }
    }
    

}
