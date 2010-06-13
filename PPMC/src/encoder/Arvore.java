/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package encoder;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Administrador
 */
public class Arvore {

    public static final int LOW = 0;
    public static final int HIGH = 1;
    public static final int TOTAL = 2;
    public static final int ESTADO = 3;

    public static final int ESTADO_PARE = 0x01;
    public static final int ESTADO_CODIFIQUE = 0x02;
    public static final int ESTADO_IGNORANCIA_ABSOLUTA = 0x04;

    private final int tabela[] = new int[4];
    public No raiz;
    private int contadorImortal;
    private final int tamanhoContexto;
    private Queue <Character> contexto;
    private Queue <Character> contextoAux;
    private Set<Character> simbolosExcluidos;
    private boolean mandouAritmetico = false;

    public Arvore(int sizeContext) {

        raiz = new No(-1, (char)255);
        tamanhoContexto = sizeContext;

        simbolosExcluidos = new HashSet<Character>();

        contexto = new ArrayDeque <Character> (tamanhoContexto);
        contextoAux = new ArrayDeque <Character> (tamanhoContexto);

    }

    public int [] processaSimbolo(char simbolo) throws Exception {

        int resultado[] = tabela;

        if (raiz.temFilhos()) {
            int tamanho = contexto.size();
            
            //atualiza de k = tamanhoContexto ate k = 1, nessa ordem
            while (contadorImortal < tamanho) {

                No filho = raiz;

                for (char a : contextoAux) {
                    filho = filho.getFilho(a);
                }

                No procurado = filho.getFilho(simbolo);
                if (procurado != null) {
                    //caso em que e encontrada uma ocorrencia em um contexto maior que 0
                    if (!mandouAritmetico) {
                        //MANDA PARA O ARITMETICO
                        //(procurado.getFrequenciaAte(simbolo), procurado.getFrequenciaAte(simbolo) + procurado.getContador(),
                        //procurado.getFrequenciaFilhos() + procurado.getQuantidadeFilhos())
                        int frequenciaParcial = procurado.getFrequenciaAte(simbolo);
                        tabela[LOW] = frequenciaParcial;
                        tabela[HIGH] = frequenciaParcial + procurado.getContador();
                        tabela[TOTAL] = procurado.getFrequenciaFilhos() + procurado.getQuantidadeFilhos();
                        tabela[ESTADO] = ESTADO_CODIFIQUE;
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
                            //tabela[TOTAL] -> filho.getFrequenciaFilhos(simbolosExcluidos) + filho.getQuantidadeFilhos)
                            int frequenciasRestantes = filho.getFrequenciaFilhos(simbolosExcluidos),
                                totalFrequencias = frequenciasRestantes + filho.getQuantidadeFilhos();
                            tabela[LOW] = frequenciasRestantes;
                            tabela[HIGH] = totalFrequencias;
                            tabela[TOTAL] = totalFrequencias;
                            tabela[ESTADO] = ESTADO_CODIFIQUE;
                        }

                        simbolosExcluidos.addAll(filho.getSimbolosFilhos());
                    }

                    filho.adicionaFilho(simbolo);
                }

                contextoAux.poll();
                ++contadorImortal;
                return resultado;

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
                    tabela[LOW] = frequenciaAte;
                    tabela[HIGH] = frequenciaAte + raiz.getContador();
                    tabela[TOTAL] = raiz.getFrequenciaFilhos() + raiz.getQuantidadeFilhos();
                    tabela[ESTADO] = ESTADO_CODIFIQUE | ESTADO_PARE;
                    resultado = tabela;
                }

                filho.incrementaContador();
            }
            else {
                //insercao do simbolo no contexto k = 0
                
                //MANDA PARA O ARITMETICO
                
                //envia o escape

                //lowcount(raiz.getQuantidadeFilhos(),
                //highcount-> raiz.getFrequenciaFilhos(simbolosExcluidos) + raiz.getQuantidadeFilhos(),
                //tabela[TOTAL] -> raiz.getFrequenciaFilhos(simbolosExcluidos) + raiz.getQuantidadeFilhos())
                int quantidade = raiz.getQuantidadeFilhos(),
                    valorAlto = raiz.getFrequenciaFilhos(simbolosExcluidos) + quantidade;
                tabela[LOW] = quantidade;
                tabela[HIGH] = valorAlto;
                tabela[TOTAL] = valorAlto;
                tabela[ESTADO] = ESTADO_CODIFIQUE | ESTADO_PARE | ESTADO_IGNORANCIA_ABSOLUTA;

                //envia a letra

                //(simbolosNaoCodificados.indexOf(simbolo), simbolosNaoCodificados.indexOf(simbolo) + 1,
                //simbolosNaoCodificados.size())

            }

        }
        else {

            tabela[ESTADO] = ESTADO_IGNORANCIA_ABSOLUTA | ESTADO_PARE;
            //leitura e codificacao do primeiro simbolo do fluxo de entrada
        }

        if (contexto.size() == tamanhoContexto) {
            contexto.poll();
        }

        contexto.offer(simbolo);
        simbolosExcluidos.clear();
        mandouAritmetico = false;
        assert contextoAux.size() == 0;
        contextoAux.addAll(contexto);
        contadorImortal = 0;

        return tabela;
    }
}
