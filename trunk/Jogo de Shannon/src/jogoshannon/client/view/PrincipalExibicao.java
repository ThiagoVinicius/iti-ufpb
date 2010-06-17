package jogoshannon.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import jogoshannon.client.presenter.PrincipalApresentador;

public class PrincipalExibicao extends Composite implements PrincipalApresentador.Exibicao {
	
	private Button botaoEnviar;
	private Label labelFrase;
	private TextBox textResposta;
	private Label labelErro;
	
	public PrincipalExibicao () {
		DecoratorPanel root = new DecoratorPanel();
		initWidget(root);
		
		VerticalPanel subRoot = new VerticalPanel();
		
		HorizontalPanel painel = new HorizontalPanel();
		painel.setBorderWidth(0);
		painel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		painel.setSpacing(10);
		labelFrase = new Label();
		labelFrase.setWidth("40em");
		botaoEnviar = new Button("Ok");
		textResposta = new TextBox();
		textResposta.setMaxLength(1);
		textResposta.setWidth("1em");
		painel.add(labelFrase);
		painel.add(textResposta);
		painel.add(botaoEnviar);
		
		labelErro = new Label();
		
		subRoot.add(painel);
		subRoot.add(labelErro);
		
		root.add(subRoot);
		
	}
	
	@Override
	public HasClickHandlers getBotaoEnviar() {
		return botaoEnviar;
	}

	@Override
	public char getResposta() {
		return textResposta.getText().charAt(0);
	}

	@Override
	public void setDesafio(String frase) {
		textResposta.setFocus(true);
		labelFrase.setText(frase);
	}

	@Override
	public void setTextoErro(String erro) {
		labelErro.setText(erro);
	}
	
	public Widget asWidget () {
		return this;
	}

}
