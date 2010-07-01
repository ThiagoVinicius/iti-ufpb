package jogoshannon.client.view;

import jogoshannon.client.presenter.PrincipalApresentador;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class PrincipalExibicao extends Composite implements PrincipalApresentador.Exibicao {

	public static enum EstadosServidor {
		AGUARDANDO_RESPOSTA,
		TUDO_CERTO,
		SEM_ALTERACOES;
	}
	
	private Label labelFrase;
	private TextBox textResposta;
	private Label labelErro;
	private Label labelCerto;
	private Label labelId;
	private Image carregando;
	private DialogBox fimDeJogo;
	private PopupPanel situacaoServidor;
	
	private static final int QUEBRA_LINHA_APOS = 50;
	
	public PrincipalExibicao () {
		
		SimplePanel root = new SimplePanel();
		initWidget(root);
		
		FlowPanel titulo_id_jogo = new FlowPanel();
		
		SimplePanel jogo_ui = new SimplePanel();
		jogo_ui.addStyleName("jogo-ui");
		
		DecoratorPanel painelEntrada = new DecoratorPanel();
		
		VerticalPanel subRoot = new VerticalPanel();
		subRoot.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		subRoot.setBorderWidth(0);
		
		HorizontalPanel painel = new HorizontalPanel();
		painel.setBorderWidth(0);
		painel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		painel.setVerticalAlignment(VerticalPanel.ALIGN_BOTTOM);
		painel.setSpacing(10);
		labelFrase = new Label();
		labelFrase.setWordWrap(true);
		labelFrase.setWidth("40em");
		labelFrase.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		textResposta = new TextBox();
		textResposta.setMaxLength(1);
		textResposta.setWidth("1em");
		carregando = new Image("ampulheta.gif");
		carregando.setSize("32", "32");
		painel.add(labelFrase);
		painel.add(textResposta);
		painel.add(carregando);
		
		fimDeJogo = new DialogBox(false, true);
		fimDeJogo.setText("Fim de jogo");
		fimDeJogo.setWidget(new Label("Parabéns, você concluiu o jogo!\n" +
				"Obrigado pela sua participacao.\n" +
				"(para jogar novamente, recarregue a página)"));
	
		fimDeJogo.setModal(false);
		fimDeJogo.setVisible(false);
		fimDeJogo.hide();
		fimDeJogo.setGlassEnabled(true);
		
		situacaoServidor = new PopupPanel(false, false);
		situacaoServidor.add(new Label("Comunicando com o servidor"));
		situacaoServidor.setStylePrimaryName("servidor-popup");
		
		labelErro = new Label();
		labelErro.setStyleName("serverResponseLabelError");
		
		labelCerto = new Label();

		painelEntrada.add(painel);
		
		subRoot.add(painelEntrada);
		subRoot.add(labelCerto);
		subRoot.add(labelErro);
		
		jogo_ui.add(subRoot);
		
		SimplePanel painelTitulo = new SimplePanel();
		painelTitulo.addStyleName("jogo-titulo");
		HTML titulo = new HTML("Jogo de Shannon");
		painelTitulo.add(titulo);
		
		HorizontalPanel painelId = new HorizontalPanel();
		painelId.setSpacing(10);
		painelId.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		painelId.addStyleName("jogo-id");
		HTML tituloId = new HTML("ID # ");
		labelId = new Label("");
		painelId.add(tituloId);
		painelId.add(labelId);

		//titulo_id_jogo.add(situacaoServidor);
		titulo_id_jogo.add(painelId);
		titulo_id_jogo.add(painelTitulo);
		titulo_id_jogo.add(jogo_ui);
		
		root.add(titulo_id_jogo);
		
		//RootPanel.get().add(fimDeJogo);
	}
	
	@Override
	public char getResposta() {
		String texto = textResposta.getText();
		if (texto.length() > 0) {
			return textResposta.getText().charAt(0);
		} else {
			return 0;
		}
	}

	@Override
	public void setDesafio(String frase) {
		StringBuilder paraExibir = new StringBuilder(frase);
		
		for (int i = QUEBRA_LINHA_APOS; i < paraExibir.length(); i += QUEBRA_LINHA_APOS) {
			int found = frase.indexOf("_", i);
			if (found != -1) {
				paraExibir.setCharAt(found, ' ');
				i = found;
			}
		}
		
		textResposta.setFocus(true);
		labelFrase.setText(paraExibir.toString());
	}

	@Override
	public void setTextoErro(String erro) {
		labelErro.setText(erro);
		boolean invisivel = erro.isEmpty();
		labelErro.setVisible(!invisivel);
	}
	
	public Widget asWidget () {
		return this;
	}

	@Override
	public TextBox getCampoResposta() {
		return textResposta;
	}

	@Override
	public void ignoraLetra() {
		textResposta.cancelKey();
	}

	@Override
	public void limparResposta() {
		textResposta.setText("");
	}
	
	public void setCarregando (boolean estado) {
		textResposta.setEnabled(!estado);
		carregando.setVisible(estado);
	}
	
	public void setEstadoServidor (EstadosServidor estadoAtual) {
		//situacaoServidor.setUrl(estadoAtual.getUrl());
		switch (estadoAtual) {
			case AGUARDANDO_RESPOSTA:
				situacaoServidor.setPopupPositionAndShow(new PositionCallback() {
					@Override
					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = (Window.getClientWidth() - situacaoServidor.getOffsetWidth())/2;
						situacaoServidor.setPopupPosition(left, 0);
					}
				});
				break;
			case TUDO_CERTO:
				situacaoServidor.hide();
				break;
		}
	}
	
	@Override
	public void exibeFimDeJogo (String titulo, String texto) {
		textResposta.setEnabled(false);
		
		fimDeJogo.setText(titulo);
		((HasText)fimDeJogo.getWidget()).setText(texto);
		
		fimDeJogo.setVisible(true);
		fimDeJogo.center();
	}
	
	Timer timerLimparParabens = new Timer() {
		@Override
		public void run() {
			setTextoParabens("");
		}
	};
	
	@Override
	public void setTextoParabens (String texto) {
		labelCerto.setText(texto);
		boolean invisivel = texto.isEmpty();
		labelCerto.setVisible(!invisivel);
		
		if (!texto.isEmpty()) {
			timerLimparParabens.cancel();
			timerLimparParabens.schedule(7500);
		}
	}
	
	public void setId (String id) {
		labelId.setText(id);
	}
	

}
