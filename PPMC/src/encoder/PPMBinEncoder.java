package encoder;


public final class PPMBinEncoder{
	
	private int kmax;

	private ModeloBinario modeloBinario;

	private ArithEncoderStream aritmetico;

	public PPMBinEncoder(int k, ArithEncoderStream aritmetico)throws Exception{
		this.kmax = k;
		this.modeloBinario = new ModeloBinario(kmax);
		this.aritmetico = aritmetico;
	}
	
	private void codifica() throws Exception{
		int k = kmax;
		while(true){
			if(k > modeloBinario.leituraLength){//erro se k > leituraLength
				k = modeloBinario.leituraLength;
			}
			if(k < 0){//k negativo
				int simb = modeloBinario.simbolo;	//pega o simbolo
				aritmetico.encode(simb,simb+1,2);	//comprime o simbolo
			}else{
				int[] tab = modeloBinario.getTabela(k);		//pega a tabela
				if(tab == null){										//se nao achou
					k--;
					continue;
				}else{													//se achou
					int simb = modeloBinario.simbolo;					//pega o simbolo
					aritmetico.encode(tab[simb],tab[simb+1],tab[2]);	//codifica
				}
			}
			break;
		}
		modeloBinario.atualizaModelo();	//atualiza os contadores
		modeloBinario.addSimbToLeitura();
	}
	
	public void resetModelo(){
		modeloBinario.reset();
	}
	
	public void comprimeBit(int bit) throws Exception{
		modeloBinario.simbolo = bit;
		codifica();
	}

}
