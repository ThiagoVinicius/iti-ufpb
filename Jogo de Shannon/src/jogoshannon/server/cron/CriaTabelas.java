package jogoshannon.server.cron;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jogoshannon.server.GestorPersistencia;
import jogoshannon.server.persistent.ConjuntoFrases;
import jogoshannon.server.persistent.Experimento;
import jogoshannon.server.persistent.ExperimentoDefault;
import jogoshannon.server.persistent.FraseStore;
import jogoshannon.shared.Frase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class CriaTabelas extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(CriaTabelas.class);

    private static final Frase frases[] = {

            new Frase(
                    "A_MEDIDA_QUE_OS_COMPOSITORES_DA_SEGUNDA_METADE_DO_SECULO_XIX_EXPANDIRAM_OS_CONCEITOS_DA_MUSICA_TONAL_COM_NOVAS_COMBINAÇOES_DE_ACORDES_TONALIDADES_E_RECURSOS_HARMONICOS_A_ESCALA_CROMATICA_E_OS_CROMATISMOS"),
            new Frase(
                    "CADA_EQUIPE_JOGOU_COM_UMA_OUTRA_EQUIPE_TRES_VEZES_DUAS_VEZES_EM_CASA_E_UMA_VEZ_FORA_DE_CASA_OU_UMA_VEZ_EM_CASA_E_DUAS_VEZES_FORA_DE_CASA_EM_UM_TOTAL_DE_DEZOITO_PARTIDAS_AS_DUAS_PARTIDAS_RESTANTES_FORAM_DISPUTADAS"),
            new Frase(
                    "AS_RAIZES_ETIMOLOGICAS_DO_TERMO_BRASIL_SAO_DE_DIFICIL_RECONSTRUÇAO_O_FILOLOGO_ADELINO_JOSE_DA_SILVA_AZEVEDO_POSTULOU_QUE_SE_TRATA_DE_UMA_PALAVRA_DE_PROCEDENCIA_CELTA_UMA_LENDA_QUE_FALA_DE_UMA_TERRA_DE_DELICIAS_VISTA"),
    //		
    // new
    // Frase("A MEDIDA QUE OS COMPOSITORES DA SEGUNDA METADE DO SECULO XIX EXPANDIRAM OS CONCEITOS DA MUSICA TONAL COM NOVAS COMBINAÇOES DE ACORDES TONALIDADES E RECURSOS HARMONICOS A ESCALA CROMATICA E OS CROMATISMOS"),
    // new
    // Frase("CADA EQUIPE JOGOU COM UMA OUTRA EQUIPE TRES VEZES DUAS VEZES EM CASA E UMA VEZ FORA DE CASA OU UMA VEZ EM CASA E DUAS VEZES FORA DE CASA EM UM TOTAL DE DEZOITO PARTIDAS AS DUAS PARTIDAS RESTANTES FORAM DISPUTADAS"),
    // new
    // Frase("AS RAIZES ETIMOLOGICAS DO TERMO BRASIL SAO DE DIFICIL RECONSTRUÇAO O FILOLOGO ADELINO JOSE DA SILVA AZEVEDO POSTULOU QUE SE TRATA DE UMA PALAVRA DE PROCEDENCIA CELTA UMA LENDA QUE FALA DE UMA TERRA DE DELICIAS VISTA"),

    };

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        logger.info("Executando script de criacao de tabelas");

        PersistenceManager pm = GestorPersistencia.get()
                .getPersistenceManager();

        Query consulta = pm.newQuery(FraseStore.class);

        List<FraseStore> fraseStore;
        try {

            fraseStore = (List<FraseStore>) consulta.execute();
            if (fraseStore.size() == 0) {
                logger.info("Banco (Frases) estava vazio.");
                for (Frase f : frases) {
                    FraseStore frase = new FraseStore(f);
                    pm.makePersistent(frase);
                }
            } else {
                logger.info("Banco (Frases) possuia dados.");
            }
            
            Experimento def = ExperimentoDefault.getDefault(pm);
            
            if (def == null) {
                logger.info("Sem experimento defaul. Criando experimento default.");
                def = new Experimento();
                ConjuntoFrases conj = new ConjuntoFrases();
                for (FraseStore f : fraseStore) {
                    conj.putFrase(f.getConteudo().getFrase());
                }
                pm.makePersistent(conj);
                def.setFrases(conj);
                def.setDescricao("Experimento com as frases que ja existiam.");
                pm.makePersistent(def);
                
                ExperimentoDefault.setKey(def.getKey());
                
            }

            logger.info("Executado com sucesso");

        } catch (Exception e) {
            logger.error("Execução falhou", e);
        } finally {
            pm.close();
        }
        
        

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.print("<html><body></body></html>");

    }

}
