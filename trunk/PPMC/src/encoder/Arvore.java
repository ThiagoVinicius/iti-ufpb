/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package encoder;

import java.util.ArrayList;
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
    private ArrayList<Character> simbolosExcluidos;

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
                }
                else {
                    //insercao de um novo simbolo em um contexto maior que 0
                    
                    if (filho.temFilhos()) {
                        //mandando o escape para o aritmetico
                        //lowcount (filho.getQuantidadeFilhos(),
                        //highcount -> filho.getFrequenciaFilhos(simbolosExcluidos) + filho.getQuantidadeFilhos(),
                        //total -> filho.getFrequenciaFilhos(simbolosExcluidos) + filho.getQuantidadeFilhos)

                        simbolosExcluidos = filho.getSimbolosFilhos();
                    }

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
                
                filho.incrementaContador();
            }
            else {
                //insercao do simbolo no contexto k = 0
                //MANDA PARA O ARITMETICO 
                //envia o escape

                //lowcount(raiz.getQuantidadeFilhos(),
                //highcount-> raiz.getFrequenciaFilhos(simbolosExcluidos) + raiz.getQuantidadeFilhos(),
                //total -> raiz.getFrequenciaFilhos(simbolosExcluidos) + raiz.getQuantidadeFilhos())

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
        simbolosExcluidos.clear();
    }
}
