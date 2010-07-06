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
		int[] contages;
		int valor;
		
		for(int i = 0; i < tentativas.length; i++)
		{
			contages = tentativas[i].contagens;
			
			for(int j = 0; j < contages.length; j++)
			{
				valor = Math.min(contages[j], 28);
				
				tabela[valor-1][j]++;
			}
		}
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
		double entropiaMax;
		double entropiaMin;
		
		for(int n = 0; n < entropiaMaxima.length; n++)
		{
			entropiaMax = entropiaMin = 0;
			
			for(int i = 0; i < tabela.length-1; i++)
			{
				entropiaMax += (i+1)*(tabela[n][i] - tabela[n][i+1])*Math.log(i+1);
				entropiaMin += tabela[n][i]*Math.log(tabela[n][i]);
			}
			
			entropiaMaxima[n] = entropiaMax;
			entropiaMinima[n] = entropiaMin;
		}
	}
	
}
