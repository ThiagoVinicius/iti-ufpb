package jogoshannon.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import jogoshannon.client.presenter.ResultadosApresentador.Exibicao;
import jogoshannon.client.util.ConjuntoUsuarios;

public class ResultadosExibicao extends Composite implements Exibicao {
	
	//private FlexTable;
	FlowPanel usuarios;
	FlexTable tabelaTentativas;
	TextBox entradaId;
	Button botaoAdicionar;
	ConjuntoUsuarios conjUsuarios;
	
	public ResultadosExibicao (HandlerManager eventos) {
		super();
		
		conjUsuarios = new ConjuntoUsuarios(eventos);
		
		SimplePanel root = new SimplePanel();
		initWidget(root);
		
		VerticalPanel painelPrincipal = new VerticalPanel();
		
		usuarios = new FlowPanel();
		tabelaTentativas = new FlexTable();
		
		HorizontalPanel painelEntrada = new HorizontalPanel();
		entradaId = new TextBox();
		entradaId.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (!Character.isDigit(event.getCharCode())) {
					((TextBox) event.getSource()).cancelKey();
				}
			}
		});
		botaoAdicionar = new Button("Adicionar");
		painelEntrada.add(new Label("Adicionar ID: "));
		painelEntrada.add(entradaId);
		painelEntrada.add(botaoAdicionar);
		
		painelPrincipal.add(usuarios);
		painelPrincipal.add(painelEntrada);
		painelPrincipal.add(tabelaTentativas);
		
		root.setWidget(painelPrincipal);
		
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void adicionarId(long id) {
		if (id != 0) {
			UsuarioWidget novo = new UsuarioWidget(id);
			novo.ativaBotaoRemover(true);
			boolean foi = conjUsuarios.adiciona(novo);
			
			if (foi) {
				usuarios.add(novo);
			}
			
		}
	}

	@Override
	public HasClickHandlers botaoAdicionar() {
		return botaoAdicionar;
	}

	@Override
	public long getIdAdicionar() {
		String texto = entradaId.getText().trim();
		if (texto.isEmpty()) {
			texto = "0";
		}
		return new Long (texto);
	}

	@Override
	public void limparIdAdicionar() {
		entradaId.setText("");
	}

	@Override
	public void removerId(long id) {
		UsuarioWidget cara = conjUsuarios.remover(id);
		if (cara != null) {
			usuarios.remove(cara);
		}
	}
	
}
