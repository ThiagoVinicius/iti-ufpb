package encoder;

import java.util.LinkedList;

public final class PPMCEncoder {

    private final int kmax;
    private final Arvore modelo;
    private final ArithEncoderStream aritmetico;
    private LinkedList<Character> simbolosNaoCodificados;

    public PPMCEncoder(int k, ArithEncoderStream aritmetico) throws Exception {
        this.kmax = k;
        this.modelo = new Arvore(kmax);
        this.aritmetico = aritmetico;

        simbolosNaoCodificados = new LinkedList<Character>();
        for (char i = 0; i < 256; i++) {
            simbolosNaoCodificados.add(i);
        }


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
    public void comprimeSimbolo(char simbolo) throws Exception {
        int tabela[];
        while (true) {
            tabela = modelo.processaSimbolo(simbolo);
            int estado = tabela[Arvore.ESTADO];
            if ((estado & Arvore.ESTADO_CODIFIQUE) != 0) {
                aritmetico.encode(tabela[Arvore.LOW], tabela[Arvore.HIGH], tabela[Arvore.TOTAL]);
            }
            if ((estado & Arvore.ESTADO_IGNORANCIA_ABSOLUTA) != 0) {
                int indice = simbolosNaoCodificados.indexOf(simbolo);
                int low = indice;
                int high = indice + 1;
                int total = simbolosNaoCodificados.size();

                modelo.raiz.adicionaFilho(simbolo);
                simbolosNaoCodificados.remove(simbolo);
                aritmetico.encode(low, high, total);
            }
            if ((estado & Arvore.ESTADO_PARE) != 0) {
                break;
            }
        }

    }

}
