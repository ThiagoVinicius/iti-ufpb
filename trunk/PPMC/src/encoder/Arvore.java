/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package encoder;

/**
 *
 * @author Administrador
 */
public class Arvore {
    private No raiz;
    private final int tamanhoContexto;
    private String contexto = "";

    public Arvore(int sizeContext) {
        raiz = new No(-1, (char)255);
        tamanhoContexto = sizeContext;
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
                    procurado.incrementaContador();
                    
                    //SE NECESSARIO, MANDA PARA O ARITMETICO
                    //quantidadeEscape == procurado.size
                }
                else {
                    filho.adicionaFilho(simbolo);

                    //SE NECESSARIO, MANDA PARA O ARITMETICO
                    //quantidadeEscape == procurado.size - 1
                }
            }

            //atualiza k = 0
            No filho = raiz.getFilho(simbolo);
            if (filho != null) {
                filho.incrementaContador();
                
                //SE NECESSARIO, MANDA PARA O ARITMETICO
                //quantidadeEscape == filho.size
            }
            else {
                raiz.adicionaFilho(simbolo);
                
                //SE NECESSARIO, MANDA PARA O ARITMETICO
                //quantidadeEscape == filho.size
            }
        }
        else {
            raiz.adicionaFilho(simbolo);
            //MANDA PARA O ARITMETICO
        }

        if (contexto.length() == tamanhoContexto) {
            contexto = contexto.substring(1) + simbolo;
        }
        else {
            contexto += simbolo;
        }
    }
    
}
