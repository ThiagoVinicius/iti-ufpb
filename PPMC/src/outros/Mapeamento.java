package outros;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;


public final class Mapeamento {

	private Mapeamento(){};//impedir criacao de objetos
	
	private static final int VAZIO = -1;
	
	public static int calculaMaxBits(int numeroNiveis){
		int comparar = 1;
        int bits = 0;

        while(comparar < numeroNiveis){
            comparar <<= 1;
            bits++;
        }
        return bits;
	}
	
	public static int calculaBits(char[] dados){

		boolean[] hist = new boolean[65536];

		for (int i = 0; i < dados.length; i++) {
			hist[dados[i]] = true;
		}

		int niveis = 0;

		for (int i = 0; i < hist.length; i++) {
			if(hist[i])
				niveis++;
		}

		if(niveis == 1)return 0;
		
		int bits = 0;

		while(niveis != 0){
			bits++;
			niveis = niveis >> 1;
		}

		return bits;
	}
	
	public static int[] getVetorMapeamentoDireto(char[] entrada, boolean map12){
		if(map12)
			return getVetorMapeamentoDireto12(entrada);
		else
			return getVetorMapeamentoDiretoDown(entrada);
	}
	
	private static int[] getVetorMapeamentoDiretoDown(char[] entrada){

		int[] saida = new int[65536];

		for (int i = 0; i < entrada.length; i++) {//calcula histograma
			saida[entrada[i]] = 1;
		}

		int cont = 0;
		for (int i = 0; i < saida.length; i++) {
			if(saida[i] == 1){
				saida[i] = cont++;//i sera mapeado para cont
			}else{
				saida[i] = VAZIO;
			}
		}
		return saida;
	}
	
	private static int[] getVetorMapeamentoDireto12(char[] entrada){
		int[] vetorMapDir = getVetorMapeamentoDiretoDown(entrada);
		
		int max = 0;
		for (int map : vetorMapDir)
			if(map > max)max = map;
		
		long max12 = 0xFFF;
		
		for (int i = 0; i < vetorMapDir.length; i++)
			vetorMapDir[i] = (int)(vetorMapDir[i] * max12 / max);
		
		return vetorMapDir;
	}
	
	public static void mapear(char[] dados, int[] vetorMap){
		for (int i = 0; i < dados.length; i++)
			dados[i] = (char)vetorMap[dados[i]];
	}
	
	public static void writeMapeamentoInverso(int[] vetorMapDireto,File infoMap)throws Exception{
		DataOutputStream out = LeituraEscrita.getOutput(infoMap);
		writeMapeamentoInverso(vetorMapDireto,out);
		out.flush();
		out.close();
	}
	
	public static void writeMapeamentoInverso(int[] vetorMapDireto,DataOutputStream out)throws Exception{
		int numeroNiveis = calculaNiveis(vetorMapDireto);
		int[] vetorMapInverso = convertMapDiretoToInverso(vetorMapDireto,numeroNiveis);
		
		out.writeChar(numeroNiveis);

		for (int vmi : vetorMapInverso)
			out.writeChar(vmi);
	}
	
	private static int calculaNiveis(int[] vetorMapDireto){
		int contador=0;
		for (int vmd : vetorMapDireto)
			if(vmd != VAZIO)
				contador++;

		return contador;
	}

	private static int[] convertMapDiretoToInverso(int[] vetorMapDireto,int numeroNiveis){
		int[] saida = new int[numeroNiveis];
		int cont = 0;

		for(int i = 0; i < vetorMapDireto.length; i++)
			if(vetorMapDireto[i] >= 0)
				saida[cont++] = i;

		return saida;
	}
	
	public static int[] readMapeamentoInverso(File infoMap, boolean map12)throws Exception{
		DataInputStream in = LeituraEscrita.getInput(infoMap);
		int[] vetorMapInverso = readMapeamentoInverso(in, map12);
		in.close();
		return vetorMapInverso;
	}
	
	public static int[] readMapeamentoInverso(DataInputStream in, boolean map12)throws Exception{
		
		int numeroNiveis = in.readChar();
		
		long max12 = 0xFFF;
		long max = numeroNiveis-1;
		
		int[] vetorMapInverso = new int[(map12)?(4096):(numeroNiveis)];
		
		if(map12){
			for(int i = 0; i < numeroNiveis; i++)
				vetorMapInverso[(int)(i*max12/max)] = in.readChar();
		}else{
			for(int i = 0; i < numeroNiveis; i++)
				vetorMapInverso[i] = in.readChar();
		}
		
		return vetorMapInverso;
	}
	
}
