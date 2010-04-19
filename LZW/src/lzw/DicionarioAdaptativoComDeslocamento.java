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
public class DicionarioAdaptativoComDeslocamento extends Tabela{


    private final int TAMANHO = 256;
    public final int LIMITE = 65536;
    //private int cont;

    public DicionarioAdaptativoComDeslocamento()
    {
        table = new Hashtable<String, Posicoes>();
        dicionario = new ArrayList<String>();
        criaRaiz();
        //cont = TAMANHO;
    }

    private void criaRaiz()
    {
        char str[] = new char[1];
        for(int i = 0; i < TAMANHO; i++)
        {
            str[0] = (char) i;
            table.put(new String(str), new Posicoes(i));
            dicionario.add(new String(str));
        }
    }

    @Override
    public int addDicionario(String letra)
    {
        if(dicionario.size() == LIMITE)
        {
            table.remove(dicionario.get(dicionario.size()-1));
            table.put((frase + letra), new Posicoes(dicionario.size()-1));
            dicionario.set(dicionario.size()-1, frase+letra);
        }
        else
        {
            dicionario.add(frase + letra);
            table.put((frase + letra), new Posicoes(dicionario.size()-1));

            //cont++;
        }


        saidaCodificacao = table.get(frase).pos;
        
        int pos = table.get(frase).pos;

        if (pos != 0) {

            String aux = dicionario.get(pos);
            dicionario.set(pos, dicionario.get(pos - 1));
            dicionario.set(pos - 1, aux);

            table.get(dicionario.get(pos - 1)).pos = pos - 1;
            table.get(dicionario.get(pos)).pos = pos;
        }
        
        //cont++;

        frase = letra;

        return saidaCodificacao;
    }

    @Override
    public void addLetra(String letra) {
        frase = frase+letra;
    }

    @Override
    public int addUltimoDicionario() {
        if(table.containsKey(frase))
        {
            return table.get(frase).pos;
        }
        else
        {
            if (dicionario.size() == LIMITE) {
                table.remove(dicionario.get(dicionario.size() - 1));
                table.put((frase), new Posicoes(dicionario.size()-1));
                dicionario.set(dicionario.size()-1, frase);
            } else {
                dicionario.add(frase);
                table.put((frase), new Posicoes(dicionario.size()-1));

                //cont++;
            }
            return table.get(frase).pos;
        }

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
    public int tamanhoDicionario() {
        return dicionario.size();
    }

    
    
    @Override
    public String addInicio(int cod) {
        
        saidaDecodificacao = dicionario.get(cod);
        codigo = cod;
        return saidaDecodificacao;
    }

    @Override
    public String addDicionario(int cod) {

        //saidaDecodificacao = dicionario.get(codigo);

        frase = dicionario.get(codigo);
        
        int pos = codigo;

        if (pos != 0) {
            String aux = dicionario.get(pos);
            dicionario.set(pos, dicionario.get(pos - 1));
            dicionario.set(pos - 1, aux);

            table.get(dicionario.get(pos - 1)).pos = pos - 1;
            table.get(dicionario.get(pos)).pos = pos;
        }

        saidaDecodificacao = dicionario.get(cod);

        char c = dicionario.get(cod).charAt(0);

        if(dicionario.size() == LIMITE)
        {
            table.remove(dicionario.get(dicionario.size()-1));
            table.put(frase + c, new Posicoes(dicionario.size() - 1));

            dicionario.set((dicionario.size() - 1), frase + c);
        }
        else
        {
            dicionario.add(frase + c);
            table.put(frase + c, new Posicoes(dicionario.size() - 1));
        }

        codigo = cod;

        return saidaDecodificacao;
    }

    @Override
    public String addLetra(int cod)
    {                      
        frase = dicionario.get(codigo);

        char c = dicionario.get(codigo).charAt(0);

        if(dicionario.size() == LIMITE)
        {
            table.remove(dicionario.get(dicionario.size()-1));
            table.put(frase + c, new Posicoes(dicionario.size() - 1));

            dicionario.set((dicionario.size() - 1), frase + c);
        }
        else
        {
            dicionario.add(frase + c);
            table.put(frase + c, new Posicoes(dicionario.size() - 1));
        }

        saidaDecodificacao = dicionario.get(cod);

        int pos = codigo;

        if (pos != 0) {
            String aux = dicionario.get(pos);
            dicionario.set(pos, dicionario.get(pos - 1));
            dicionario.set(pos - 1, aux);

            table.get(dicionario.get(pos - 1)).pos = pos - 1;
            table.get(dicionario.get(pos)).pos = pos;
        }

        codigo = cod;

        return saidaDecodificacao;
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