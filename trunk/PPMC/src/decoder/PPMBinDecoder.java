package decoder;

import encoder.ModeloBinario;

public final class PPMBinDecoder{
	
	private int kmax;
	
	private ModeloBinario modeloBinario;
	
	private ArithDecoderStream aritmetico;

	public PPMBinDecoder(int k, ArithDecoderStream aritmetico)throws Exception{
		this.kmax = k;
		this.modeloBinario = new ModeloBinario(kmax);
		this.aritmetico = aritmetico;
	}
	
	private void decodifica()throws Exception{
		int k = kmax;
		while(true){
			if(k > modeloBinario.leituraLength){//		Erro se k > comprimento de leitura
				k = modeloBinario.leituraLength;
			}
			if(k < 0){//		K NEGATIVO
				int simb = aritmetico.getCurrentSymbolCount(2);		//pega o simbolo
				aritmetico.removeSymbolFromStream(simb,simb+1,2);	//remove simbolo
				modeloBinario.simbolo = simb;				//pega simbolo
			}else{//		K 0 OU MAIOR
				int[] tab = modeloBinario.getTabela(k);	//pega tabela
				if(tab ==  null){							//se nao achou
					k--;
					continue;
				}else{											//se achou
					int count = aritmetico.getCurrentSymbolCount(tab[2]);	//pega contador
					int simb = (count < tab[1])?(0):(1);		//simbolo = 0 ou 1
					aritmetico.removeSymbolFromStream(tab[simb],tab[simb+1],tab[2]);	//remove simbolo
					modeloBinario.simbolo = simb;								//pega simbolo
				}
			}
			break;
		}
		modeloBinario.atualizaModelo();						//adiciona simbolo
		modeloBinario.addSimbToLeitura();
	}
        
	public int descomprimeBit() throws Exception{
		decodifica();
		return modeloBinario.simbolo;
	}
	
}
