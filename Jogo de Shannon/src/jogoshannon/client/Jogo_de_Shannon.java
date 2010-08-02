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

    public void onModuleLoad() {
        JuizSoletrandoAsync servidor = GWT.create(JuizSoletrando.class);
        ((ServiceDefTarget) servidor)
                .setRpcRequestBuilder(new DuplicadorCookie());
        Apresentador appViewer = new ControladorAplicacao(servidor);
        appViewer.vai(RootPanel.get("principal"));
    }
}
