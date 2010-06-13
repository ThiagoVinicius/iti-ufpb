/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package encoder;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Administrador
 */
public class Arvore {
    private No raiz;
    private final int tamanhoContexto;
    private String contexto = "";
    private LinkedList<Character> simbolosNaoCodificados;
    private Set<Character> simbolosExcluidos;
    private boolean mandouAritmetico = false;
    private final ArithEncoderStream aritmetico;

    public Arvore(int sizeContext, ArithEncoderStream aritmetico) {

        this.aritmetico = aritmetico;

        raiz = new No(-1, (char)255);
        tamanhoContexto = sizeContext;

        simbolosNaoCodificados = new LinkedList<Character>();
        for (int i = 0; i < 256; i++) {
            simbolosNaoCodificados.add((char)i);
        }

        simbolosExcluidos = new HashSet<Character>();
    }

    public void processaSimbolo(char simbolo) throws Exception {

        int low, high, total;
        low = high = total = 0;

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
                    if (!mandouAritmetico) {
                        //MANDA PARA O ARITMETICO
                        //(procurado.getFrequenciaAte(simbolo), procurado.getFrequenciaAte(simbolo) + procurado.getContador(),
                        //procurado.getFrequenciaFilhos() + procurado.getQuantidadeFilhos())
                        int frequenciaParcial = procurado.getFrequenciaAte(simbolo);
                        low = frequenciaParcial;
                        high = frequenciaParcial + procurado.getContador();
                        total = procurado.getFrequenciaFilhos() + procurado.getQuantidadeFilhos();
                        aritmetico.encode(low, high, total);
                        mandouAritmetico = true;
                    }

                    procurado.incrementaContador();
                }
                else {
                    //insercao de um novo simbolo em um contexto maior que 0
                    
                    if (filho.temFilhos()) {
                        if (!mandouAritmetico) {
                            //mandando o escape para o aritmetico
                            //lowcount (filho.getFrequenciaFilhos(simbolosExcluidos),
                            //highcount -> filho.getFrequenciaFilhos(simbolosExcluidos) + filho.getQuantidadeFilhos(),
                            //total -> filho.getFrequenciaFilhos(simbolosExcluidos) + filho.getQuantidadeFilhos)
                            int frequenciasRestantes = filho.getFrequenciaFilhos(simbolosExcluidos), 
                                totalFrequencias = frequenciasRestantes + filho.getQuantidadeFilhos();
                            low = frequenciasRestantes;
                            high = totalFrequencias;
                            total = totalFrequencias;
                            aritmetico.encode(low, high, total);
                        }

                        simbolosExcluidos.addAll(filho.getSimbolosFilhos());
                    }

                    filho.adicionaFilho(simbolo);
                }
            }

            //atualiza k = 0
            No filho = raiz.getFilho(simbolo);
            if (filho != null) {
                //atualizacao de um simbolo que ja apareceu antes (esta presente em k = 0)
                if (!mandouAritmetico) {
                    //SE NECESSARIO, MANDA PARA O ARITMETICO
                    //(raiz.getFrequenciaAte(simbolo), raiz.getFrequenciaAte(simbolo) + raiz.getContador(),
                    //raiz.getFrequenciaFilhos() + raiz.getQuantidadeFilhos())
                    int frequenciaAte = raiz.getFrequenciaAte(simbolo);
                    low = frequenciaAte;
                    high = frequenciaAte + raiz.getContador();
                    total = raiz.getFrequenciaFilhos() + raiz.getQuantidadeFilhos();
                    aritmetico.encode(low, high, total);
                }

                filho.incrementaContador();
            }
            else {
                //insercao do simbolo no contexto k = 0
                
                //MANDA PARA O ARITMETICO
                
                //envia o escape

                //lowcount(raiz.getQuantidadeFilhos(),
                //highcount-> raiz.getFrequenciaFilhos(simbolosExcluidos) + raiz.getQuantidadeFilhos(),
                //total -> raiz.getFrequenciaFilhos(simbolosExcluidos) + raiz.getQuantidadeFilhos())
                int quantidade = raiz.getQuantidadeFilhos(),
                    valorAlto = raiz.getFrequenciaFilhos(simbolosExcluidos) + quantidade;
                low = quantidade;
                high = valorAlto;
                total = valorAlto;
                aritmetico.encode(low, high, total);

                //envia a letra

                //(simbolosNaoCodificados.indexOf(simbolo), simbolosNaoCodificados.indexOf(simbolo) + 1,
                //simbolosNaoCodificados.size())
                int indice = simbolosNaoCodificados.indexOf(simbolo);
                low = indice;
                high = indice + 1;
                total = simbolosNaoCodificados.size();
                aritmetico.encode(low, high, total);
                    
                raiz.adicionaFilho(simbolo);
                simbolosNaoCodificados.remove(simbolo);
            }
        }
        else {
            //leitura e codificacao do primeiro simbolo do fluxo de entrada
            raiz.adicionaFilho(simbolo);
            //MANDA PARA O ARITMETICO
            //(simbolosNaoCodificados.indexOf(simbolo), simbolosNaoCodificados.indexOf(simbolo) + 1, 256)
            low = simbolosNaoCodificados.indexOf(simbolo);
            high = simbolosNaoCodificados.indexOf(simbolo);
            total = 256;
            aritmetico.encode(low, high, total);
            simbolosNaoCodificados.remove(simbolo);
        }

        if (contexto.length() == tamanhoContexto) {
            contexto = contexto.substring(1) + simbolo;
        }
        else {
            contexto += simbolo;
        }
        simbolosExcluidos.clear();
        mandouAritmetico = false;
    }
}
