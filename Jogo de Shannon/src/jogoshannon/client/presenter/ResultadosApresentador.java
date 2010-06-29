package jogoshannon.client.presenter;

import jogoshannon.client.JuizSoletrandoAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class ResultadosApresentador implements Apresentador {
	
	public static interface Exibicao {
		
	}
	
	private HandlerManager eventos;
	private Exibicao view;
	private JuizSoletrandoAsync servidor;
	
	public ResultadosApresentador (HandlerManager eventos, 
								   Exibicao view, 
								   JuizSoletrandoAsync servidor) {
		this.eventos = eventos;
		this.view = view;
		this.servidor = servidor;
	}

	@Override
	public void vai(HasWidgets pagina) {
		// TODO Auto-generated method stub

	}

}
