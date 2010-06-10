package decoder;

import java.io.DataInputStream;

public final class BitInputStream {

	int bytesLidos = 0;
	int bytesMaximo = 0;
	
	DataInputStream input;
	
	int buffer = 0;
	int bitsNoBuffer = 0;
	
	public BitInputStream(DataInputStream in)throws Exception{
		input = in;
		bytesLidos = 0;
		bytesMaximo = input.readInt();
		input = in;
	}
	
	public boolean readBit()throws Exception{
		if(bitsNoBuffer == 0){
			encherBuffer();
			return readBit();
		}else{
			bitsNoBuffer--;
			return ((buffer >> bitsNoBuffer) & 1) == 1;
		}
	}
	
	private void encherBuffer()throws Exception{
		if(bytesLidos < bytesMaximo){
			bytesLidos++;
			buffer = input.read();
			bitsNoBuffer = 8;
		}else{
			buffer = 0;
			bitsNoBuffer = 8;
		}
	}
	
}
