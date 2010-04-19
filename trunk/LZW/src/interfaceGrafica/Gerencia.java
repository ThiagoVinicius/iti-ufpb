/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceGrafica;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import lzw.LZWPai;

/**
 *
 * @author Janduy
 */
public class Gerencia extends Thread {

    private final int BUFFER = 4*1024*1024;//4MB

    LZWPai lzw;
    JProgressBar pro;
    JTextArea area;
    File file;
    int valor;

    public Gerencia(LZWPai lzw, JProgressBar pro, JTextArea area, File file, int valor) {
        this.lzw = lzw;
        this.pro = pro;
        this.area = area;
        this.file = file;
        this.valor = valor;
    }

    @Override
    public void run() {

        switch(valor)
        {
            case 0:
                codifica();
            break;
            case 1:
                decodifica();
            break;
        }
        
    }

    private void codifica() {
        
        try {
            DataInputStream input;
            DataOutputStream output;

            pro.setValue(0);

            input = new DataInputStream(new BufferedInputStream(new FileInputStream(file), BUFFER));

            output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file + ".comp"), BUFFER));

            lzw.setOutput(output);

            pro.setMaximum((int) file.length());

            long t1 = System.currentTimeMillis();

            for (int i = 0; i < file.length(); i++) {
                lzw.codifica((char) input.read() + "");


                pro.setValue(i + 1);
            }

            lzw.fimDaCodificacao();

            long t2 = System.currentTimeMillis() - t1;

            output.flush();

            double rc = (file.length()*1.0)/(output.size()*1.0);

            area.setText(area.getText() +
                         "\nTamanho comprimido: " +
                         output.size() + " bytes" +
                         "\nRC: " +rc + ":1" +
                         "\nTempo gasto: " +
                         t2 + " ms");

            input.close();
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decodifica() {
        try {
            DataInputStream input;
            DataOutputStream output;

            input = new DataInputStream(new BufferedInputStream(new FileInputStream(file), BUFFER));

            output = new DataOutputStream(new BufferedOutputStream(
                                          new FileOutputStream((file+"").replaceAll(".comp", "")), BUFFER));

            lzw.setOutput(output);

            pro.setMaximum((int) file.length());

            long t1 = System.currentTimeMillis();

            int con = input.readChar();

            lzw.inicioDaDecodificacao(con+"");

            for (int i = 2; i < file.length(); i = i + 2) {
                con = input.readChar();
                lzw.decodifica(con+"");
                pro.setValue(i+2);
            }

            long t2 = System.currentTimeMillis() - t1;
            
            output.flush();

            area.setText(area.getText()+
                         "\nTamanho decodificado: " +
                         output.size() + " bytes" +
                         "\nTempo gasto: " +
                         t2 + " ms");

            input.close();
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
