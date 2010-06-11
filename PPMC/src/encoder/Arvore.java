/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package encoder;

import java.util.LinkedList;

/**
 *
 * @author Administrador
 */
public class Arvore {
    private No raiz;
    private final int tamanhoContexto;
    private String contexto = "";
    private LinkedList<Character> simbolosNaoCodificados;

    public Arvore(int sizeContext) {
        raiz = new No(-1, (char)255);
        tamanhoContexto = sizeContext;

        simbolosNaoCodificados = new LinkedList<Character>();
        for (int i = 0; i < 256; i++) {
            simbolosNaoCodificados.add((char)i);
        }
    }

    public void processaSimbolo(char simbolo) {
        if (raiz.temFilhos()) {
            int tamanho = contexto.length();
            
            //atualiza de k = tamanhoContexto ate k = 1, nessa ordem
            for (int i = 0; i < tamanho; i++) {
                No filho = raiz;
                for (int j = i; j < tamanho; j++) {
                    filho = filho.getFilho(contexto.charAt(j));
                }

                No procurado = filho.getFilho(simbolo);
                if (procurado != null) {
                    //caso em que e encontrada uma ocorrencia em um contexto maior que 0
                    procurado.incrementaContador();
                    
                    //SE NECESSARIO, MANDA PARA O ARITMETICO
                    //frequenciaEscape == procurado.size
                }
                else {
                    //insercao de um novo simbolo em um contexto maior que 0
                    
                    if (filho.temFilhos()) {
                        //AQUI DEVE SER IMPLEMENTADA A EXCLUSAO; NAO ME PARECE SER TAO SIMPLES...
                        //mandando o escape para o aritmetico
                        //(filho.getFrequenciaFilhos(), filho.getFrequenciaFilhos() + filho.getQuantidadeFilhos(),
                        //filho.getFrequenciaFilhos() + filho.getQuantidadeFilhos)
                        //frequenciaEscape == procurado.size - 1
                    }

                    //nao tenho certeza se caso esse no nao tenha filhos se deve ser mandado para o aritmetico tambem
                    //acredito que nao
                    filho.adicionaFilho(simbolo);
                }
            }

            //atualiza k = 0
            No filho = raiz.getFilho(simbolo);
            if (filho != null) {
                //atualizacao de um simbolo que ja apareceu antes (esta presente em k = 0)
                //SE NECESSARIO, MANDA PARA O ARITMETICO
                //(raiz.getFrequenciaAte(simbolo), raiz.getFrequenciaAte(simbolo) + raiz.getContador(),
                //raiz.getFrequenciaFilhos() + raiz.getQuantidadeFilhos())
                //quantidadeEscape == filho.size
                filho.incrementaContador();
            }
            else {
                //insercao do simbolo no contexto k = 0
                //MANDA PARA O ARITMETICO 
                //envia o escape
                //(raiz.getFrequenciaFilhos(), raiz.getFrequenciaFilhos() + raiz.getQuantidadeFilhos(),
                //raiz.getFrequenciaFilhos() + raiz.getQuantidadeFilhos())
                //envia a letra
                //(simbolosNaoCodificados.indexOf(simbolo), simbolosNaoCodificados.indexOf(simbolo) + 1,
                //simbolosNaoCodificados.size())
                raiz.adicionaFilho(simbolo);
                simbolosNaoCodificados.remove(simbolo);
            }
        }
        else {
            //leitura e codificacao do primeiro simbolo do fluxo de entrada
            raiz.adicionaFilho(simbolo);
            //MANDA PARA O ARITMETICO
            //(simbolosNaoCodificados.indexOf(simbolo), simbolosNaoCodificados.indexOf(simbolo) + 1, 256)
            simbolosNaoCodificados.remove(simbolo);
        }

        if (contexto.length() == tamanhoContexto) {
            contexto = contexto.substring(1) + simbolo;
        }
        else {
            contexto += simbolo;
        }
    }
    
}
