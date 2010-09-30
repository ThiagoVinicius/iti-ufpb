package jogoshannon.server.util;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {

    public void testDesacentua() {

        assertEquals(
            "O percevejo (ou maria fedida na linguagem " +
            "popular brasileira) e um inseto da sub-familia Blissinae, " +
            "que ataca plantacoes e pastagens. Existem algumas especies " +
            "que sao hematofagas.",
                
            StringUtils.desacentua(
                "O percevejo (ou maria fedida na linguagem " +
                "popular brasileira) é um inseto da sub-família Blissinae, " +
                "que ataca plantações e pastagens. Existem algumas espécies " +
                "que são hematófagas."));

    }
    
    public void testNormaliza() {

        assertEquals(
                "O_PERCEVEJO_OU_MARIA_FEDIDA_NA_LINGUAGEM_" +
                "POPULAR_BRASILEIRA_E_UM_INSETO_DA_SUB_FAMILIA_BLISSINAE_" +
                "QUE_ATACA_PLANTACOES_E_PASTAGENS_EXISTEM_ALGUMAS_ESPECIES_" +
                "QUE_SAO_HEMATOFAGAS_",
                
            StringUtils.normalizar(
                "O percevejo (ou maria fedida na linguagem " +
                "popular brasileira) é um inseto da sub-família Blissinae, " +
                "que ataca plantações e pastagens. Existem algumas espécies " +
                "que são hematófagas."));

    }

}
