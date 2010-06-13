package encoder;


public final class PPMCEncoder{
	
	private final int kmax;

	private final Arvore modelo;

	private final ArithEncoderStream aritmetico;

	public PPMCEncoder(int k, ArithEncoderStream aritmetico)throws Exception{
		this.kmax = k;
		this.modelo = new Arvore(kmax, aritmetico);
		this.aritmetico = aritmetico;
	}
	
//	private void codifica() throws Exception{
//		int k = kmax;
//		while(true){
//			if(k > modelo.leituraLength){//erro se k > leituraLength
//				k = modelo.leituraLength;
//			}
//			if(k < 0){//k negativo
//				int simb = modelo.simbolo;	//pega o simbolo
//				aritmetico.encode(simb,simb+1,2);	//comprime o simbolo
//			}else{
//				int[] tab = modelo.getTabela(k);		//pega a tabela
//				if(tab == null){										//se nao achou
//					k--;
//					continue;
//				}else{													//se achou
//					int simb = modelo.simbolo;					//pega o simbolo
//					aritmetico.encode(tab[simb],tab[simb+1],tab[2]);	//codifica
//				}
//			}
//			break;
//		}
//		modelo.atualizaModelo();	//atualiza os contadores
//		modelo.addSimbToLeitura();
//	}
//
	public void comprimeSimbolo(int simbolo) throws Exception{
		modelo.processaSimbolo((char)simbolo);
		//codifica();
	}

}
