/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package huffmanAdaptativo;

import controladorEntradaSaida.ControladorEntradaSaida;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Janduy
 */
public class HuffmanPai
{
    HuffmanAdaptativoSimples huff;
    ControladorEntradaSaida controle;

    public HuffmanPai()
    {
        huff = new HuffmanAdaptativoSimples();
        controle = new ControladorEntradaSaida();
    }

    public void codificaPrimeiro(String c)
    {
        huff.codificaPrimeiro(c);        
    }

    public void codifica(String c) throws IOException
    {
        //System.out.println("-"+c+"-");
        controle.escreve(huff.codifica(c));
    }

    public String decodifica() throws IOException
    {
        String s = huff.decodifica(controle.ler());

        while (s.charAt(0) == 257)
        {
            s = huff.decodifica(controle.ler());
        }

        if(s.charAt(0) == 256)
        {
            char c = controle.lerCaracter();
            huff.novoChar(c);

            System.out.println("decodifi " +c);

            return c+"";
        }

        return s;
    }

    public String decodificaPrimeiro() throws IOException
    {
        String s = controle.lerCaracter()+"";
        //System.out.println("s-"+s+"-");
        huff.decodificaPrimeiro(s);
        //System.out.println(s);
        return s;
    }

    public void setOutput(DataOutputStream output, DataOutputStream outputSeq) {
        controle.setSaida(output, outputSeq);
    }

    public void flush() throws IOException {
        controle.flush();
        controle.escreveSequencia(huff.sequencia);
    }

    public void setInput(DataInputStream input, DataInputStream inputseq) {
        controle.setEntrada(input, inputseq);
    }
}
