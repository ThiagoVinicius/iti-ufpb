/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package encoder;

import java.util.ArrayDeque;
import java.util.HashSet;
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

    public static final char ESCAPE = 256;

    private final int tabela[] = new int[4];
    public No raiz;
    private int contadorImortal;
    private final int tamanhoContexto;
    private Queue <Character> contexto;
    private Queue <Character> contextoAux;
    public Set<Character> simbolosExcluidos;
    private boolean mandouAritmetico = false;
    public char decodificado;

    public Arvore(int sizeContext) {

        raiz = new No(0, (char)255);
        tamanhoContexto = sizeContext;

        simbolosExcluidos = new HashSet<Character>();

        contexto = new ArrayDeque <Character> (tamanhoContexto);
        contextoAux = new ArrayDeque <Character> (tamanhoContexto);

    }

    public int [] processaSimbolo(char simbolo) throws Exception {

        tabela[ESTADO] = 0;

        if (raiz.temFilhos()) {
            int tamanho = contexto.size();
            
            //atualiza de k = tamanhoContexto ate k = 1, nessa ordem
            while (contadorImortal < tamanho) {

                No filho = localizaContexto();

                No procurado = filho.getFilho(simbolo);
                if (procurado != null) {
                    //caso em que e encontrada uma ocorrencia em um contexto maior que 0
                    if (!mandouAritmetico) {
                        //MANDA PARA O ARITMETICO
                        //(procurado.getFrequenciaAte(simbolo), procurado.getFrequenciaAte(simbolo) + procurado.getContador(),
                        //procurado.getFrequenciaFilhos() + procurado.getQuantidadeFilhos())
                        encontraTabela(filho, simbolo);
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
                            encontraTabela(filho, simbolo);
                            tabela[ESTADO] = ESTADO_CODIFIQUE;
                        }

                        simbolosExcluidos.addAll(filho.getSimbolosFilhos());
//                        System.out.println("enc Excluidos: " + simbolosExcluidos);
                    }

                    filho.adicionaFilho(simbolo);
                }

                contextoAux.poll();
                ++contadorImortal;
                return tabela;

            }

            //atualiza k = 0
            No filho = raiz.getFilho(simbolo);
            if (filho != null) {

                tabela[ESTADO] = ESTADO_PARE;
                //atualizacao de um simbolo que ja apareceu antes (esta presente em k = 0)
                if (!mandouAritmetico) {
                    //SE NECESSARIO, MANDA PARA O ARITMETICO
                    //(raiz.getFrequenciaAte(simbolo), raiz.getFrequenciaAte(simbolo) + raiz.getContador(),
                    //raiz.getFrequenciaFilhos() + raiz.getQuantidadeFilhos())
                    encontraTabela(raiz, simbolo);
                    tabela[ESTADO] = ESTADO_CODIFIQUE | ESTADO_PARE;
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
                encontraTabela(raiz, simbolo);
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
        if (tamanhoContexto > 0) {
            contexto.offer(simbolo);
            contextoAux.addAll(contexto);
        }
        simbolosExcluidos.clear();
        mandouAritmetico = false;
        contadorImortal = 0;

        

        return tabela;
    }

    public void reseta () {
        contextoAux.clear();
        if (tamanhoContexto > 0) {
            contextoAux.addAll(contexto);
        }
        contadorImortal = 0;
        simbolosExcluidos.clear();
        mandouAritmetico = false;
    }

    public int[] atualizaContexto (int simboloCodificado, No contexto) {

        char simbolo = contexto.getSimbolo(simboloCodificado, simbolosExcluidos);
        encontraTabela(contexto, simbolo);
        if (simbolo == ESCAPE) {
//            System.out.println("Decodificado ESCAPE.");
            if (contextoAux.size() > 0) {
//                System.out.println("Boo reduzindo contexto.");
                contextoAux.poll(); //reduz o contexto
                simbolosExcluidos.addAll(contexto.getSimbolosFilhos());
//                System.out.println("dec Excluidos: " + simbolosExcluidos);
                tabela[ESTADO] = ESTADO_CODIFIQUE;
            } else {
                tabela[ESTADO] = ESTADO_CODIFIQUE | ESTADO_IGNORANCIA_ABSOLUTA | ESTADO_PARE;
            }
        } else {
            tabela[ESTADO] = ESTADO_CODIFIQUE | ESTADO_PARE;
            decodificado = simbolo;
        }
        
        return tabela;
    }

    public No localizaContextoNaoVazio () {
        No resultado = raiz;
        while (true) {
            for (char a : contextoAux) {
                resultado = resultado.getFilho(a);
            }
            if (resultado.temFilhos()) {
                break;
            } else {
//                System.out.println("dec Reducao de contexto.");
                contextoAux.poll();
                resultado = raiz;
            }
        }
        return resultado;
    }

    public No localizaContexto () {
        No resultado = raiz;
        for (char a : contextoAux) {
            resultado = resultado.getFilho(a);
        }
        return resultado;
    }

    /**
     * retorna true, caso o simbolo tenha sido encontrado, false, caso contrario.
     * @param contexto
     * @param simbolo
     * @return
     */
    public boolean encontraTabela (No contexto, char simbolo) {

        No procurado = contexto.getFilho(simbolo);
        if (procurado != null && !simbolosExcluidos.contains(simbolo)) {
            int frequenciaParcial = contexto.getFrequenciaAte(simbolo, simbolosExcluidos);
            tabela[LOW] = frequenciaParcial;
            tabela[HIGH] = frequenciaParcial + procurado.getContador();
            tabela[TOTAL] = contexto.getFrequenciaFilhos(simbolosExcluidos) + contexto.getQuantidadeFilhos();
            return true;
        } else {
            //emitindo escape
            int frequenciasRestantes = contexto.getFrequenciaFilhos(simbolosExcluidos),
                totalFrequencias = frequenciasRestantes + contexto.getQuantidadeFilhos();
            tabela[LOW] = frequenciasRestantes;
            tabela[HIGH] = totalFrequencias;
            tabela[TOTAL] = totalFrequencias;
            return false;
        }

    }

    public int getTotal (No contexto) {
        int result = contexto.getFrequenciaFilhos(simbolosExcluidos) + contexto.getQuantidadeFilhos();
        return result;
    }

}
