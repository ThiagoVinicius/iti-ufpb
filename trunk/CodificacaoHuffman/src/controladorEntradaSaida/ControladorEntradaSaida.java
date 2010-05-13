/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controladorEntradaSaida;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Janduy
 */
public class ControladorEntradaSaida
{
    DataOutputStream saida, sequenciaSaida;
    DataInputStream entrada, sequenciaEntrada;
    int codigo;
    String palavra;

    public ControladorEntradaSaida() {
        palavra = "";
    }

    public void setSaida(DataOutputStream saida, DataOutputStream sequencia)
    {
        this.saida = saida;
        this.sequenciaSaida = sequencia;
    }

    public void setEntrada(DataInputStream entrada, DataInputStream sequenciaEntrada)
    {
        this.entrada = entrada;
        this.sequenciaEntrada = sequenciaEntrada;
    }

    public void escreve(String seq) throws IOException
    {     
        
        palavra = palavra + seq;

        while(palavra.length() >= 8)
        {
            codigo = Integer.parseInt(palavra.substring(0, 8), 2);

            saida.write(codigo);
            palavra = palavra.substring(8, palavra.length());
        }
    }

    public void escreveSequencia(String seq) throws IOException
    {
        for (int i = 1; i < seq.length(); i++)
        {
            sequenciaSaida.write(seq.charAt(i));
        }
    }

    public void flush() throws IOException
    {
        if(palavra.length() != 0)
        {
            int cont = 8 - palavra.length();

            for (int i = 0; i < cont; i++) {
                palavra = palavra + "0";
            }

            saida.write(Integer.parseInt(palavra, 2));
            palavra = "";
        }
    }

    public String ler() throws IOException
    {
        if(palavra.length() == 0)
        {
            int codigo = entrada.read();
            palavra = Integer.toBinaryString(codigo);

            int cont = (palavra.length() == 0) ? 0 : 8 - palavra.length();

            for(int i = 0; i < cont; i++)
            {
                palavra = "0" + palavra;
            }
        }

        String bit = palavra.charAt(0) + "";       

        palavra = palavra.substring(1, palavra.length());

        return bit;
    }

    public char lerCaracter() throws IOException
    {
        char se = (char)sequenciaEntrada.read();
        //System.out.println(se);
        return se;
    }
    
    public static void main(String[] args)
    {
        String palavra = "10010001";

        palavra = palavra.substring(0, 4);

        //System.out.println(Integer.parseInt(palavra.substring(0, 8), 2));

        System.out.println(palavra);

        int t = Integer.parseInt(palavra.substring(palavra.length()-4, palavra.length()), 2);
        
        String s = Integer.toBinaryString(t);
        


        System.out.println(palavra.substring(0, palavra.length()-4));

        t = Integer.parseInt(palavra.substring(0, palavra.length()-4), 2);
        s = Integer.toBinaryString(t);
    }
}
