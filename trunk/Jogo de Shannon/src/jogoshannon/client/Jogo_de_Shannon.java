package jogoshannon.client;

import jogoshannon.client.presenter.Apresentador;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Jogo_de_Shannon implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	public void onModuleLoad() {
		JuizSoletrandoAsync servidor = GWT.create(JuizSoletrando.class);
		((ServiceDefTarget) servidor).setRpcRequestBuilder(new DuplicadorCookie());
	    HandlerManager eventBus = new HandlerManager(null);
//	    Apresentador idSessao = new IdApresentador(servidor);
	    Apresentador appViewer = new ControladorAplicacao(eventBus, servidor);
	    appViewer.vai(RootPanel.get("principal"));
//	    idSessao.vai(RootPanel.get("id_container"));
	}
}
