/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lzw;

import codificador.CodificadorInteiroPorTamanho;
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

    CodificadorInteiroPorTamanho cod;

    DataOutputStream output;
    DataInputStream input;

    static String ss = "";


    public LZWPai(Dicionario dicionario) {
        this.dicionario = dicionario;
    }

    public void setCodificador(CodificadorInteiroPorTamanho cod)
    {
        this.cod = cod;
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
            //cod.codifica(saidaCodificada + " "+ nBits(dicionario.tamanhoDicionario()));
            //ss = ss+ " "+saidaCodificada;
        }
    }


    @Override
    public void fimDaCodificacao() throws IOException
    {
        saidaCodificada = dicionario.addUltimoDicionario();
        output.writeChar((char)saidaCodificada);
        //cod.codifica(saidaCodificada + " "+ nBits(dicionario.tamanhoDicionario()));
        //ss = ss+" "+saidaCodificada;
    }

    public void decodifica(String letra) throws IOException
    {
        int codig = Integer.parseInt(letra);

        //codig = cod.decodifica(nBits(dicionario.tamanhoDicionario())+"");

        if(codig == -1)
            throw  new IOException();

        if(codig >= dicionario.tamanhoDicionario())
        {
            saidaDecodificada = dicionario.addLetra(codig);
            for(int i = 0; i < saidaDecodificada.length(); i++)
            {
                output.write(saidaDecodificada.charAt(i));
            }
            //ss = ss+saidaDecodificada;
        }
        else if(dicionario.contemFrase(codig))
        {
            saidaDecodificada = dicionario.addDicionario(codig);
            for(int i = 0; i < saidaDecodificada.length(); i++)
            {
                output.write(saidaDecodificada.charAt(i));
            }
            
            //ss = ss+saidaDecodificada;
        }
        else
        {
            saidaDecodificada = dicionario.addLetra(codig);
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
        int codig = Integer.parseInt(letra);

        //codig = cod.decodifica(nBits(dicionario.tamanhoDicionario())+"");

        saidaDecodificada = dicionario.addInicio(codig);
        for(int i = 0; i < saidaDecodificada.length(); i++)
        {
            output.write(saidaDecodificada.charAt(i));
        }
        //ss = ss+saidaDecodificada;
    }

    private int nBits(int tamanho)
    {
        if(tamanho < 256)
            return 8;
        else if(tamanho < 512)
            return 9;
        else if(tamanho < 1024)
            return 10;
        else if(tamanho < 2048)
            return 11;
        else if(tamanho < 4096)
            return 12;
        else if(tamanho < 8192)
            return 13;
        else if(tamanho < 16384)
            return 14;
        else if(tamanho < 32768)
            return 15;
        else if(tamanho < 65536)
            return 16;
        else if(tamanho < 131072)
            return 17;
        else if(tamanho < 262144)
            return 18;
        else if(tamanho < 524288)
            return 19;
        else if(tamanho < 1048576)
            return 20;
        else if(tamanho < 2097152)
            return 21;
        else if(tamanho < 4194304)
            return 22;
        else if(tamanho < 8388608)
            return 23;
        else if(tamanho < 16777216)
            return 24;
        else if(tamanho < 33554432)
            return 25;
        else if(tamanho < 67108864)
            return 26;
        else if(tamanho < 134217728)
            return 27;
        else if(tamanho < 268435456)
            return 28;
        else if(tamanho < 536870912)
            return 29;
        else if(tamanho < 1073741824)
            return 30;
        else
            return 31;
    }

}