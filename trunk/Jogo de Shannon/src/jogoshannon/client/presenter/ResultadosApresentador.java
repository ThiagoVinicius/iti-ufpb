package jogoshannon.client.presenter;

import jogoshannon.client.JuizSoletrandoAsync;
import jogoshannon.client.event.UsuarioRemovidoEvent;
import jogoshannon.client.event.UsuarioRemovidoHandler;
import jogoshannon.shared.Tentativas;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ResultadosApresentador implements Apresentador {
	
	public static interface Exibicao {
		Widget asWidget();
		HasClickHandlers botaoAdicionar();
		long getIdAdicionar();
		void adicionarId(long id);
		void setCarregandoId(long id, boolean carregando);
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
				adicionarIdPeloCampo();
			}
		});
		
		
		eventos.addHandler(UsuarioRemovidoEvent.TIPO, new UsuarioRemovidoHandler() {
			@Override
			public void onUsuarioRemovido(UsuarioRemovidoEvent evento) {
				removerId(evento.getOrigem().getId());
			}
		});
	}
	
	private void adicionarIdPeloCampo() {
		final long id = view.getIdAdicionar();
		adicionarId(id);
		view.limparIdAdicionar();
	}
	
	private void adicionarId(final long id) {
		view.adicionarId(id);
		view.setCarregandoId(id, true);
		
		servidor.getResultados(id, new AsyncCallback<Tentativas[]>() {
			@Override
			public void onSuccess(Tentativas[] result) {
				view.setCarregandoId(id, false);
			}
			@Override
			public void onFailure(Throwable caught) {
				removerId(id);
			}
		});
		
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
