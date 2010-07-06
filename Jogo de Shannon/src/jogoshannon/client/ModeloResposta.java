package jogoshannon.client;

import jogoshannon.shared.Frase;
import jogoshannon.shared.Tentativas;

public class ModeloResposta {
	
	
	private int tabela[][];
	private double entropiaMinima[];
	private double entropiaMaxima[];
	
	public ModeloResposta() {
		
		tabela = new int[28][Frase.QUANTIDADE_LETRAS.length];	
		entropiaMaxima = new double[Frase.QUANTIDADE_LETRAS.length];
		entropiaMinima = new double[Frase.QUANTIDADE_LETRAS.length];
	}
	
	public void adiciona(Tentativas[] tentativas)
	{
		
	}
	
	
	public int getLinhaCount()
	{
		return tabela.length;
	}
	
	public int[] getLinha(int linha)
	{
		return tabela[linha];
	}
	
	public double[] getEntropiaMinima()
	{
		return entropiaMinima;
	}
	
	public double[] getEntropiaMaxima()
	{
		return entropiaMaxima;
	}
	
	public void calculaEntropia()
	{
		
	}
	
}
