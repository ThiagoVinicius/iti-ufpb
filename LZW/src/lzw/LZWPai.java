/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lzw;

import compressionframework.Modelo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Janduy e Thiago
 */
public abstract class LZWPai extends Modelo
{

    int saidaCodificada;
    String saidaDecodificada;
    Dicionario dicionario;

    DataOutputStream output;
    DataInputStream input;

    static String ss = "";


    public LZWPai(Dicionario dicionario) {
        this.dicionario = dicionario;
    }

    public void setInput(DataInputStream input)
    {
        this.input = input;
    }

    public void setOutput(DataOutputStream output)
    {
        this.output = output;
    }
    
    public void codifica(String letra) throws IOException
    {

               
        if(dicionario.contemFrase(letra))
        {
            dicionario.addLetra(letra);
        }
        else
        {
            saidaCodificada = dicionario.addDicionario(letra);
            output.writeChar((char)saidaCodificada);
            //ss = ss+ " "+saidaCodificada;
        }
    }


    @Override
    public void fimDaCodificacao() throws IOException
    {
        saidaCodificada = dicionario.addUltimoDicionario();
        output.writeChar((char)saidaCodificada);
        //ss = ss+" "+saidaCodificada;
    }

    public void decodifica(String letra) throws IOException
    {
        int cod = Integer.parseInt(letra);

        if(cod >= dicionario.tamanhoDicionario())
        {
            saidaDecodificada = dicionario.addLetra(cod);
            for(int i = 0; i < saidaDecodificada.length(); i++)
            {
                output.write(saidaDecodificada.charAt(i));
            }
            //ss = ss+saidaDecodificada;
        }
        else if(dicionario.contemFrase(cod))
        {
            saidaDecodificada = dicionario.addDicionario(cod);
            for(int i = 0; i < saidaDecodificada.length(); i++)
            {
                output.write(saidaDecodificada.charAt(i));
            }
            
            //ss = ss+saidaDecodificada;
        }
        else
        {
            saidaDecodificada = dicionario.addLetra(cod);
            for(int i = 0; i < saidaDecodificada.length(); i++)
            {
                output.write(saidaDecodificada.charAt(i));
            }
            //ss = ss+saidaDecodificada;
        }
    }

    @Override
    public void inicioDaDecodificacao(String letra) throws IOException
    {
        int cod = Integer.parseInt(letra);
        saidaDecodificada = dicionario.addInicio(cod);
        for(int i = 0; i < saidaDecodificada.length(); i++)
        {
            output.write(saidaDecodificada.charAt(i));
        }
        //ss = ss+saidaDecodificada;
    }

}