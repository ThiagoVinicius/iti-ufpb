package encoder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public final class BitOutputStream {

    static final int CODE_VALUE_BITS = 31;
    DataOutputStream outputFinal;
    ByteArrayOutputStream outputTemp;
    int buffer = 0;
    int bitsNoBuffer = 0;

    public BitOutputStream(DataOutputStream out) throws Exception {
        outputFinal = out;
        outputTemp = new ByteArrayOutputStream();
    }

    public int flush() throws Exception {

        for(int i = 0; i < CODE_VALUE_BITS-2; i++)
        {
            writeBitFalse();
        }

        while (bitsNoBuffer < 8) {
            writeBitFalse();//salvar 0 no final ate completar 8 bits
        }
        esvaziarBuffer();

        //fechar temp
        outputTemp.flush();
        outputTemp.close();

        //copiar temp pro outputFinal
        //byte[] temp = outputTemp.toByteArray();
        //outputFinal.writeInt(temp.length);//salvar quantos bytes escritos
        //outputFinal.write(temp);
        return outputFinal.size();
    }

    public void writeBitTrue() throws Exception {
        if (bitsNoBuffer == 8) {
            esvaziarBuffer();
            writeBitTrue();
        } else {
            buffer = (buffer << 1) | 1;//add 1
            bitsNoBuffer++;
        }
    }

    public void writeBitFalse() throws Exception {
        if (bitsNoBuffer == 8) {
            esvaziarBuffer();
            writeBitFalse();
        } else {
            buffer = buffer << 1;//add 0
            bitsNoBuffer++;
        }
    }

    private void esvaziarBuffer() throws Exception {
        outputFinal.write(buffer);
        outputFinal.flush();
        buffer = 0;
        bitsNoBuffer = 0;
    }
}
