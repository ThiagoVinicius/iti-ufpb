package jogoshannon.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import jogoshannon.server.persistent.Cobaia;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.Experimento;
import jogoshannon.server.persistent.Obra;
import jogoshannon.server.util.StringUtils;

public class GestorExperimentos {
    
    private static final float MARGEM_SEGURANCA = 2.5f;
    private static final Pattern PRIMEIRA_PALAVRA = Pattern.compile("[ ][a-zA-Z]");
    
    private static class Selecao {
        public Selecao(long inicio, long fim, String result) {
            this.inicio = inicio;
            this.fim = fim;
            this.result = result;
        }
        public final long inicio;
        public final long fim;
        public final String result;
    }
    
    public static Cobaia novaCobaia (Experimento exp, PersistenceManager pm) throws IOException {
        Cobaia novo = new Cobaia();
        novo.setExperimento(exp);
        if (exp.getFrasesKey() == null) {
            ConjuntoFrases conj = criarAleatorio(pm, exp);
            novo.setConjuntoFrases(conj);
        }
        pm.makePersistent(novo);
        exp.addCobaia(novo);
        return novo;
    }
    
    public static ConjuntoFrases criarAleatorio (PersistenceManager pmo, Experimento exp) throws IOException {
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        Query query = pm.newQuery(Obra.class);
        query.setFilter("uploadUrl == null");
        List<Obra> result = (List<Obra>) query.execute();
        try {
            return criarAleatorio(result, exp.getContagemFrases(), exp.getMostrarLetras(), pmo);
        } finally {
            pm.close();
        }
    }
    
    public static ConjuntoFrases criarAleatorio (List<Obra> origem, int nFrases, 
            List<Integer> letras, PersistenceManager pm) throws IOException {

        Random rand = new Random();
        int top = Collections.max(letras);
        ConjuntoFrases frases = new ConjuntoFrases();
        StringBuilder descricao = new StringBuilder();
        
        descricao.append("Criado automaticamente em "+(new Date()));
        descricao.append("\n");
        descricao.append("Frases originais:");
        descricao.append("\n\n");
        
        
        for (int i = 0; i < nFrases; ++i) {
            
            int escolhido = rand.nextInt(origem.size());
            Selecao res = separa(origem.get(escolhido), top);
            String lido = res.result;
            lido = StringUtils.desacentua(lido);
            lido = lido.substring(localizaPrimeiraPalavra(lido));
            lido = StringUtils.normalizar(lido);
            frases.putFrase(lido);
            
            descricao.append("---- ---- ---- ----\n");
            descricao.append(res.result);
            descricao.append("\n");
            descricao.append("Retirado de: ");
            descricao.append(origem.get(escolhido).getTitulo());
            descricao.append(", por ");
            descricao.append(origem.get(escolhido).getAutor());
            descricao.append(". [");
            descricao.append(res.inicio);
            descricao.append(", ");
            descricao.append(res.fim);
            descricao.append("]\n");
            
        }
        
        frases.setDescricao(descricao.toString());
        
        Experimento result = new Experimento();
        result.getMostrarLetras().addAll(letras);
        
        pm.makePersistent(frases);
        return frases;
        
    }
    
    private static Selecao separa (Obra obra, int tam) throws IOException {
        tam *= MARGEM_SEGURANCA;
        int comp = (int) comprimento(obra);
        InputStreamReader isr = abrirObra(obra);
        Random rand = new Random();
        long inicio = rand.nextInt(comp - tam);
        char dados[] = new char[tam];
        isr.skip(inicio);
        isr.read(dados);
        isr.close();
        String lido = new String(dados);
        return new Selecao(inicio, inicio+tam, lido);
    }
    
    private static int localizaPrimeiraPalavra (String texto) {
        Matcher match = PRIMEIRA_PALAVRA.matcher(texto);
        if (match.find() == false) {
            return -1;
        } else {
            return match.start()+1;
        }
        
    }
    
    private static long comprimento (Obra obra) throws IOException {
        if (obra.getCharLen() >= 0) {
            return obra.getCharLen();
        } else {
            InputStreamReader isr = abrirObra(obra);
            long comprimento = isr.skip(Long.MAX_VALUE);
            isr.close();
            obra.setCharLen(comprimento);
            return comprimento;
        }
    }
    
    private static InputStreamReader abrirObra (Obra obra) 
        throws UnsupportedEncodingException, IOException {
        
        return abrirObra(obra, 0L);
    }
    
    private static InputStreamReader abrirObra (Obra obra, long offset) 
        throws UnsupportedEncodingException, IOException {
        
        String charset = obra.getCodificacao();
        if (charset == null) {
            charset = "ISO-8859-1";
        }
        return new InputStreamReader(obra.open(offset), charset);
        
    }
    
}
