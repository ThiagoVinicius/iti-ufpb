package jogoshannon.client;

import jogoshannon.client.presenter.Apresentador;
import jogoshannon.client.resources.Imagens;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Jogo_de_Shannon implements EntryPoint {

    public static final String TITULO_PADRAO = "Jogo de Shannon";
    public static final Imagens IMAGENS = GWT.create(Imagens.class);
    
    private static JuizSoletrandoAsync juizSoletrando;
    private static ProducaoProgramaAsync producaoPrograma;
    private static ControladorAplicacao chefe;

    public void onModuleLoad() {
        chefe = new ControladorAplicacao();
        chefe.vai(RootPanel.get("principal"));
    }
    
    public synchronized static JuizSoletrandoAsync getJuizSoletrando () {
        if (juizSoletrando == null) {
            juizSoletrando = GWT.create(JuizSoletrando.class);
            ((ServiceDefTarget) juizSoletrando).setRpcRequestBuilder(new DuplicadorCookie());
        }
        return juizSoletrando;
    }
    
    public synchronized static ProducaoProgramaAsync getProducaoPrograma () {
        if (producaoPrograma == null) {
            producaoPrograma = GWT.create(ProducaoPrograma.class);
            ((ServiceDefTarget) producaoPrograma).setRpcRequestBuilder(new DuplicadorCookie());
        }
        return producaoPrograma;
    }
    
    public static ControladorAplicacao getApresentadorChefe () {
        return chefe;
    }
}
