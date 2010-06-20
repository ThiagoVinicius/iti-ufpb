package jogoshannon.client.view;

import jogoshannon.client.presenter.PrincipalApresentador;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PrincipalExibicao extends Composite implements PrincipalApresentador.Exibicao {

	public static enum EstadosServidor {
		AGUARDANDO_RESPOSTA,
		TUDO_CERTO,
		SEM_ALTERACOES;
		
		public String getUrl() {
			switch (this) {
			case AGUARDANDO_RESPOSTA:
				return "bola_vermelha.png";
			case TUDO_CERTO:
				return "bola_verde.png";
			default:
				return "bola_cinza.png";
			}
		}
	}
	
	private Label labelFrase;
	private TextBox textResposta;
	private Label labelErro;
	private Label labelCerto;
	private Image carregando;
	private Image situacaoServidor;
	private DialogBox fimDeJogo;
	
	public PrincipalExibicao () {
		SimplePanel root = new SimplePanel();
		initWidget(root);
		
		DecoratorPanel painelEntrada = new DecoratorPanel();
		
		VerticalPanel subRoot = new VerticalPanel();
		subRoot.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		subRoot.setBorderWidth(0);
		
		HorizontalPanel painel = new HorizontalPanel();
		painel.setBorderWidth(0);
		painel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		painel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		painel.setSpacing(10);
		labelFrase = new Label();
		labelFrase.setWordWrap(true);
		labelFrase.setWidth("40em");
		labelFrase.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		textResposta = new TextBox();
		textResposta.setMaxLength(1);
		textResposta.setWidth("1em");
		carregando = new Image("ampulheta.gif");
		situacaoServidor = new Image(EstadosServidor.SEM_ALTERACOES.getUrl());
		painel.add(labelFrase);
		painel.add(textResposta);
		painel.add(carregando);
		painel.add(situacaoServidor);
		
		fimDeJogo = new DialogBox(true);
		//fimDeJogo.setAnimationEnabled(true);
		
		fimDeJogo.setTitle("Fim de jogo");
		//fimDeJogo.setText("Parabéns, você concluiu o jogo!");
		fimDeJogo.setVisible(false);
		
		labelErro = new Label();
		labelErro.setStyleName("serverResponseLabelError");
		
		labelCerto = new Label("Hahahahaha");

		painelEntrada.add(painel);
		
		subRoot.add(painelEntrada);
		subRoot.add(labelErro);
		subRoot.add(labelCerto);
		subRoot.add(fimDeJogo);
		
		root.add(subRoot);
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
		boolean invisivel = erro.isEmpty();
		labelErro.setVisible(!invisivel);
		if (!invisivel) {
			labelCerto.setText("");
		}
	}
	
	public Widget asWidget () {
		return this;
	}

	@Override
	public HasKeyPressHandlers getCampoResposta() {
		return textResposta;
	}

	@Override
	public void ignoraLetra() {
		textResposta.cancelKey();
	}

	@Override
	public void setResposta(char r) {
		textResposta.setText(Character.toString(r));
	}
	
	public void setCarregando (boolean estado) {
		carregando.setVisible(estado);
	}
	
	public void setEstadoServidor (EstadosServidor estadoAtual) {
		situacaoServidor.setUrl(estadoAtual.getUrl());
	}
	
	public void exibeFimDeJogo () {
		Window.alert("Você terminou o jogo, muito bem.");
	}
	
	public void setTextoParabens (String texto) {
		labelCerto.setText(texto);
		boolean invisivel = texto.isEmpty();
		labelCerto.setVisible(!invisivel);
		if (!invisivel) {
			setTextoErro("");
		}
	}
	

}
