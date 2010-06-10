package outros;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public final class LeituraEscrita {

	private static final int BUFFER = 4*1024*1024;//4MB
	
	private LeituraEscrita(){}//impedir a criacao de objeto dessa classe
	
	public static DataInputStream getInput(File f)throws Exception{
		return new DataInputStream(new BufferedInputStream(new FileInputStream(f),BUFFER));
	}
	
	public static DataOutputStream getOutput(File f)throws Exception{
		return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f),BUFFER));
	}
	
	public static char[] read(DataInputStream in, int tam)throws Exception{
		char[] dados = new char[tam];
		for (int i = 0; i < tam; i++)
			dados[i] = in.readChar();
		return dados;
	}
	
	public static char[] readInvertido(DataInputStream in, int tam)throws Exception{
		char[] dados = new char[tam];
		int b1,b2;
		for (int i = 0; i < tam; i++){
			b1 = in.read();
			b2 = in.read();
			dados[i] = (char)((b2 << 8) | b1);//inverter
		}
		return dados;
	}
	
	public static char[] read(File f)throws Exception{
		DataInputStream in = getInput(f);
		char[] dados = read(in, (int)f.length()/2);
		in.close();
		return dados;
	}
	
	public static char[] readInvertido(File f)throws Exception{
		DataInputStream in = getInput(f);
		char[] dados = readInvertido(in, (int)f.length()/2);
		in.close();
		return dados;
	}
	
	public static void write(DataOutputStream out, char[] dados)throws Exception{
		for (char c : dados)
			out.writeChar(c);
	}
	
	public static void writeInvertido(DataOutputStream out, char[] dados)throws Exception{
		for (char c : dados)
			out.writeChar( (c << 8) | (c >> 8));
	}
	
	public static void write(File f, char[] dados)throws Exception{
		DataOutputStream out = getOutput(f);
		write(out, dados);
		out.flush();
		out.close();
	}
	
	public static void writeInvertido(File f, char[] dados)throws Exception{
		DataOutputStream out = getOutput(f);
		writeInvertido(out, dados);
		out.flush();
		out.close();
	}
	
}
