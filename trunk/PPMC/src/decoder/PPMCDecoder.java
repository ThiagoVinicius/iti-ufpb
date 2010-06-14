package decoder;

import encoder.Arvore;
import encoder.No;
import java.util.Arrays;
import java.util.LinkedList;

public final class PPMCDecoder {

    private int kmax;
    private Arvore modelo;
    private ArithDecoderStream aritmetico;
    private boolean primeiro = true;
    private LinkedList<Character> simbolosNaoCodificados;

    public PPMCDecoder(int k, ArithDecoderStream aritmetico) throws Exception {
        this.kmax = k;
        this.modelo = new Arvore(kmax);
        this.aritmetico = aritmetico;
        simbolosNaoCodificados = new LinkedList<Character>();
        for (char i = 0; i < 256; i++) {
            simbolosNaoCodificados.add(i);
        }
    }

    private char decodifica() throws Exception {

        modelo.reseta();
        while (true) {
            No contexto = modelo.localizaContextoNaoVazio();
            int total = modelo.getTotal(contexto);
            int simboloCodificado = aritmetico.getCurrentSymbolCount(total);
            //System.out.printf("Loopeando na decodificacao. Total = %d\n", total);
            //System.out.printf("Simbolo codificado = %d\n", simboloCodificado);
            int tabela[] = modelo.atualizaContexto(simboloCodificado, contexto);
            int estado = tabela[Arvore.ESTADO];

            if ((estado & Arvore.ESTADO_CODIFIQUE) != 0) {
                assert total == tabela[Arvore.TOTAL] : modelo.simbolosExcluidos;
                //System.out.printf("Recuperando totais: %s\n", Arrays.toString(tabela));
                aritmetico.removeSymbolFromStream(tabela[Arvore.LOW], tabela[Arvore.HIGH], total);
            }
            if ((estado & Arvore.ESTADO_IGNORANCIA_ABSOLUTA) != 0) {

                total = simbolosNaoCodificados.size();
                simboloCodificado = aritmetico.getCurrentSymbolCount(total);
                //System.out.printf("Decodificado [ignorancia absoluta]: Total = %d\n", total);
                modelo.decodificado = simbolosNaoCodificados.get(simboloCodificado);
                simbolosNaoCodificados.remove(simboloCodificado);

                //System.out.printf("Decodificado [ignorancia absoluta]: Intervalo = (%d, %d, %d)\n", simboloCodificado, simboloCodificado+1, total);
                aritmetico.removeSymbolFromStream(simboloCodificado, simboloCodificado+1, total);
                //System.out.printf("Decodificado [ignorancia absoluta]: (%d) %c\n", (int) modelo.decodificado, modelo.decodificado);

                modelo.reseta();
                while (true) {
                    estado = modelo.processaSimbolo(modelo.decodificado)[Arvore.ESTADO];
                    if ((estado & Arvore.ESTADO_PARE) != 0) {
                        break;
                    }
                }

                modelo.raiz.adicionaFilho(modelo.decodificado);

                return modelo.decodificado;

            }
            if ((estado & Arvore.ESTADO_PARE) != 0) {
                modelo.reseta();
                //System.out.printf("Decodificado [Modelo]: %c\n", modelo.decodificado);
                while (true) {
                    estado = modelo.processaSimbolo(modelo.decodificado)[Arvore.ESTADO];
                    if ((estado & Arvore.ESTADO_PARE) != 0) {
                        return modelo.decodificado;
                    }
                }
            }

        }

    }

    public int descomprimeBit() throws Exception {
        if (primeiro) {
            primeiro = !primeiro;
            int total = simbolosNaoCodificados.size();
            int simboloCodificado = aritmetico.getCurrentSymbolCount(total);
            modelo.decodificado = simbolosNaoCodificados.get(simboloCodificado);
            simbolosNaoCodificados.remove(simboloCodificado);
            //System.out.printf("Intervalo [Primeiro simbolo]: (%d, %d, %d)\n", simboloCodificado, simboloCodificado+1, total);
            aritmetico.removeSymbolFromStream(simboloCodificado, simboloCodificado+1, total);
            modelo.processaSimbolo(modelo.decodificado);
            modelo.raiz.adicionaFilho(modelo.decodificado);
            //System.out.printf("Decodificado [Primeiro simbolo]: %c\n", modelo.decodificado);
            return modelo.decodificado;
        } else {
            return decodifica() & 0xFF;
        }
    }
}
