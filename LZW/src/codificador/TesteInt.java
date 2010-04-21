/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package codificador;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 *
 * @author Janduy
 */
public class TesteInt {

    public static void main(String[] args)throws Exception {



        File f1 = new File("in.dat");
        File f2 = new File("ou.dat");        

        ByteArrayOutputStream fakeOutput = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(fakeOutput);

        int[] v = {2, 16, 35, 34, 67, 100, 129, 300, 600, 2025, 65539};
        int[] t = {2, 5, 6, 6, 7, 7, 8, 9, 10, 11, 17};

        CodificadorInteiroPorTamanho co = new CodificadorInteiroPorTamanho();

        co.setOutput(output);

        for(int i = 8; i < 32; i++)
        {
            //System.out.println((int)Math.pow(2, i));
        }

        for(int i = 0; i < v.length; i++)
        {
            co.codifica(v[i]+"");
        }

        //co.flush();

        output.flush();
        output.close();

        System.out.println(Arrays.toString(fakeOutput.toByteArray()));

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(fakeOutput.toByteArray()));
        co.setInput(input);

        for(int i = 0; i < v.length; i++)
        {
            System.out.println(co.decodifica(""+t[i]));            
        }

    }

}
