package jogoshannon.client.presenter;

import jogoshannon.client.JuizSoletrandoAsync;
import jogoshannon.client.event.UsuarioRemovidoEvent;
import jogoshannon.client.event.UsuarioRemovidoHandler;
import jogoshannon.client.view.UsuarioWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ResultadosApresentador implements Apresentador {
	
	public static interface Exibicao {
		Widget asWidget();
		HasClickHandlers botaoAdicionar();
		long getIdAdicionar();
		void adicionarId(long id);
		void limparIdAdicionar();
		void removerId(long id);
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
		
		amarrar();
	}
	
	private void amarrar () {
		view.botaoAdicionar().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				adicionarId();
			}
		});
		
		
		eventos.addHandler(UsuarioRemovidoEvent.TIPO, new UsuarioRemovidoHandler() {
			@Override
			public void onUsuarioRemovido(UsuarioRemovidoEvent evento) {
				removerId(evento.getOrigem().getId());
			}
		});
	}
	
	private void adicionarId() {
		view.adicionarId(view.getIdAdicionar());
		view.limparIdAdicionar();
	}
	
	private void removerId (long id) {
		view.removerId(id);
		//view.limparIdAdicionar();
	}

	@Override
	public void vai(HasWidgets pagina) {
		pagina.clear();
		pagina.add(view.asWidget());
	}

}
