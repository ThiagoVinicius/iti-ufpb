/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceGrafica;

import huffmanAdaptativo.HuffmanPai;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 *
 * @author Janduy
 */
public class Gerencia extends Thread {

    HuffmanPai huff;

    private final int BUFFER = 4*1024*1024;//4MB
    JProgressBar pro;
    JTextArea area;
    File file;
    int valor;

    public Gerencia(JProgressBar pro, JTextArea area, File file, int valor) {

        huff = new HuffmanPai();
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
            DataOutputStream outputSeq;

            pro.setValue(0);

            input = new DataInputStream(new BufferedInputStream(new FileInputStream(file), BUFFER));

            output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file + ".comp"), BUFFER));
            outputSeq = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file + ".comp_seq"), BUFFER));

            outputSeq.writeInt((int) file.length());

            System.out.println(file.length());

            huff.setOutput(output, outputSeq);

            pro.setMaximum((int) file.length());

            long t1 = System.currentTimeMillis();

            huff.codificaPrimeiro((char) input.read() + "");

            for (int i = 1; i < file.length(); i++) {
                huff.codifica((char) input.read() + "");

                pro.setValue(i + 1);
            }

            huff.flush();

            long t2 = System.currentTimeMillis() - t1;

            output.flush();
            outputSeq.flush();

            double rc = (file.length()*1.0)/((output.size()*1.0)+(outputSeq.size()*1.0));

            area.setText(area.getText() +
                         "\nTamanho comprimido: " +
                         output.size() + " bytes" +
                         "\nRC: " +rc + ":1" +
                         "\nTempo gasto: " +
                         t2 + " ms");

            input.close();
            output.close();
            outputSeq.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decodifica() {
        try {
            
            String f = (file+"").endsWith(".comp") ? (file+"") : (file+".comp");

            File fi = new File(f);

            DataInputStream input;
            DataInputStream inputseq;
            DataOutputStream output;

            input = new DataInputStream(new BufferedInputStream(new FileInputStream(f), BUFFER));
            inputseq = new DataInputStream(new BufferedInputStream(new FileInputStream(f+"_seq"), BUFFER));

            if((new File(f.replaceAll(".comp", ""))).exists())
            {
                f = JOptionPane.showInputDialog(null, "Diretorio já existe!\nSe desejar renomeie o arquivo.", f.replaceAll(".comp", ""));
            }

            output = new DataOutputStream(new BufferedOutputStream(
                                          new FileOutputStream(f), BUFFER));

            int tamanho = inputseq.readInt();

            huff.setInput(input, inputseq);

            pro.setMaximum(tamanho);

            long t1 = System.currentTimeMillis();

            //int con = input.readChar();
            int con = 0;

            System.out.println(tamanho);

            char c1 = huff.decodificaPrimeiro().charAt(0);
            //System.out.println("-"+c1+"-");

            output.write(c1);

//            pro.setIndeterminate(true);
//
//            try
//            {
//                while(true)
//                {
//                    lzw.decodifica(con+"");
//                }
//            }
//            catch(Exception e)
//            {
//                e.printStackTrace();
//            }
//
//            pro.setIndeterminate(false);

            for (int i = 1; i < tamanho; i ++) {
                String sa = huff.decodifica();
                //System.out.print(sa);
                output.write(sa.charAt(0));
                pro.setValue(i);
            }

            pro.setValue(tamanho);

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
