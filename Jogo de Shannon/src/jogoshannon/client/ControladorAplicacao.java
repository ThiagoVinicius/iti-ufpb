package jogoshannon.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import jogoshannon.client.presenter.Apresentador;
import jogoshannon.client.presenter.PrincipalApresentador;
import jogoshannon.client.view.PrincipalExibicao;

public class ControladorAplicacao implements Apresentador, ValueChangeHandler<String> {

	private HandlerManager eventos;
	private HasWidgets pagina;
	private JuizSoletrandoAsync servidor;
	
	public ControladorAplicacao (HandlerManager eventos, JuizSoletrandoAsync servidor) {
		this.eventos = eventos;
		this.servidor = servidor;
		amarra();
	}
	
	private void amarra() {
		History.addValueChangeHandler(this);
	}
	
	@Override
	public void vai(HasWidgets pagina) {
		this.pagina = pagina;
		
		if ("".equals(History.getToken())) {
			History.newItem("jogar");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		
		if (token != null) {
			Apresentador oEscolhido = null;
			
			if (token.equals("jogar")) {
				oEscolhido = new PrincipalApresentador(eventos, new PrincipalExibicao(), servidor);
			}
			
			if (oEscolhido != null) {
				oEscolhido.vai(pagina);
			}
			
		}
		
	}

}
