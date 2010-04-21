/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package codificador;

import compressionframework.Codificador;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Janduy
 */
public class CodificadorInteiroPorTamanho extends Codificador
{
    long valor;
    int nBtis;
    int quantidade;

    DataInputStream input;
    DataOutputStream output;

    public CodificadorInteiroPorTamanho() {
        valor = nBtis = quantidade = 0;
    }



    /**
     * 
     * @param dados - Comtem dois inteiros separados por espaco. Primeiro representando o dado e o segundo representado o numero de bits
     
    @Override
    public void codifica(String dados) throws IOException{

        String[] v = dados.split(" ");

        //System.out.print(dados + " " );

        nBtis = Integer.parseInt(v[1]);
        
        valor = (valor << nBtis) | Integer.parseInt(v[0]);
        quantidade += nBtis;

        int resto = quantidade;

        for(int i = 8; i <= quantidade; i+= 8)
        {
            output.write((int)(valor >> (quantidade - i)));
            System.out.println(" " + (int)(valor >> (quantidade - i)));
            valor = valor & (int)(Math.pow(2, (quantidade - i)) -1);
            resto = quantidade - i;
        }
        
        quantidade = resto;
    }*/

    /*@Override
    public int decodifica(String dados)  throws IOException {

        nBtis = Integer.parseInt(dados);

        //System.out.print(nBtis + " " );
        int total = quantidade - nBtis;
        
        for(int i = total; i <= 0; i += 8 )
        {
            int a = input.read();
            System.out.print(a + " " );
            valor = (valor << 8) | a;
            
            quantidade += 8;
        }

        int resultado = (int)(valor >> (quantidade - nBtis));        
        valor = valor & (int)(Math.pow(2, (quantidade - nBtis)) -1);

        

        quantidade = quantidade - nBtis;

        System.out.println("v" + valor + " " + quantidade);

        //System.out.print(resultado + " " );

        return resultado;
    }

    @Override
    public void flush() throws IOException{

        output.write((int)(valor<<(8-quantidade)));
        System.out.println("valor = " + valor);
        System.out.println("quan = " + quantidade);
        System.out.println((int)(valor<<(8-quantidade))+"\n");
        
        valor = nBtis = quantidade = 0;
    }*/

    @Override
    public int getValue(){

        if(quantidade < nBtis)
            return -1;

        int saida = (int)(valor >> (quantidade-nBtis));
        //System.out.println("sa " + saida + " " + nBtis);
        valor = valor & (valor-saida);
        //System.out.println("valor " + valor);
        quantidade -= nBtis;
        return saida;
    }

    @Override
    public void setInput(DataInputStream input) {
        this.input = input;
    }

    @Override
    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    @Override
    public void codifica(String dados) throws IOException {
        int number = new Integer(dados);
        int temp;
        boolean last = false;
        while (true) {
            temp = number & 0x7f;
            number = number >>> 7;
            last = number == 0;
            if (last) {
                output.write(temp); //escreve um byte
                break;
            } else {
                output.write(temp | 0x80); //escreve um byte
            }
        }
    }


    @Override
    public int decodifica(String dados) throws IOException {
        int result = 0;

        int read;

        boolean last = false;

        for (int i = 0; !last; ++i) {

            read = input.read(); //le um byte

            last = (read & 0x80) == 0;

            result |= (read & 0x7f) << (7 * i);

        }


        return result;
    }
    
}
