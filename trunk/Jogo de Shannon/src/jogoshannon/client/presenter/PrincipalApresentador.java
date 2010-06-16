package jogoshannon.client.presenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class PrincipalApresentador implements Apresentador {
	
	public interface Exibicao {
		HasClickHandlers getBotaoEnviar();
		void setDesafio(String frase);
		void setTextoErro(String erro);
		char getResposta();
		Widget asWidget();
	}
	
	Exibicao view;
	HandlerManager eventos;
	
	public PrincipalApresentador (HandlerManager eventos, Exibicao view) {
		this.view = view;
		this.eventos = eventos;
	}
	
	@Override
	public void vai(HasWidgets pagina) {
		pagina.clear();
		pagina.add(view.asWidget());
	}

}
