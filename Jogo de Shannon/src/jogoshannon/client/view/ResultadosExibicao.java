package jogoshannon.client.view;

import jogoshannon.client.presenter.ResultadosApresentador.Exibicao;
import jogoshannon.client.util.ConjuntoUsuarios;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.NumberFormat;
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

public class ResultadosExibicao extends Composite implements Exibicao {
	
	private static final int LINHA_TITULO = 0;
	private static final int OFFSET_LINHA = 1;
	
	private static final int LEGENDA_COLUNA = 0;
	private static final int OFFSET_COLUNA = 1;
	
	private static final int ENTROPIA_MIN_END_OFFSET = 1;
	private static final int ENTROPIA_MAX_END_OFFSET = 2;
	
	//private FlexTable;
	FlowPanel usuarios;
	FlexTable tabelaTentativas;
	TextBox entradaId;
	Button botaoAdicionar;
	ConjuntoUsuarios conjUsuarios;
	int maxLinha;
	
	public ResultadosExibicao (HandlerManager eventos) {
		super();
		
		conjUsuarios = new ConjuntoUsuarios(eventos);
		
		SimplePanel root = new SimplePanel();
		initWidget(root);
		
		VerticalPanel painelPrincipal = new VerticalPanel();
		
		usuarios = new FlowPanel();
		tabelaTentativas = new FlexTable();
		tabelaTentativas.getRowFormatter().addStyleName(0, "tituloTabela");
	    tabelaTentativas.addStyleName("corpoTabela");
		//tabelaTentativas.setBorderWidth(1);
		
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

	@Override
	public void setCarregandoId(long id, boolean carregando) {
		UsuarioWidget cara = conjUsuarios.get(id);
		if (cara != null) {
			cara.setCarregando(carregando);
		}
	}
	
	private String doubleToString(double numero) {
		
		return NumberFormat.getFormat("0.000").format(numero);
	}
	
	@Override
	public void atualizaEntropiaMinima(int linha, double[] dados) {
		linha += ENTROPIA_MIN_END_OFFSET;
		tabelaTentativas.setText(OFFSET_LINHA+linha, LEGENDA_COLUNA, "Entropia Mínima");
		for (int i = 0; i < dados.length; ++i) {
			Label texto = new Label(doubleToString(dados[i]));
			texto.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
			tabelaTentativas.setWidget(OFFSET_LINHA+linha, OFFSET_COLUNA+i, texto);
		}
	}
	
	@Override
	public void atualizaEntropiaMaxima(int linha, double[] dados) {
		linha += ENTROPIA_MAX_END_OFFSET;
		tabelaTentativas.setText(OFFSET_LINHA+linha, LEGENDA_COLUNA, "Entropia Máxima");
		for (int i = 0; i < dados.length; ++i) {
			Label texto = new Label(doubleToString(dados[i]));
			texto.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
			tabelaTentativas.setWidget(OFFSET_LINHA+linha, OFFSET_COLUNA+i, texto);
		}
	}

	@Override
	public void atualizarLinha(int linha, int[] dados) {
		maxLinha = Math.max(maxLinha, linha);
		tabelaTentativas.setText(OFFSET_LINHA+linha, LEGENDA_COLUNA, ""+(linha+1));
		for (int i = 0; i < dados.length; ++i) {
			Label texto = new Label(dados[i] == 0 ? "" : ""+dados[i]);
			texto.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
			tabelaTentativas.setWidget(OFFSET_LINHA+linha, OFFSET_COLUNA+i, texto);
		}
	}

	@Override
	public void setTitulosTabela(String[] titulos) {
		for (int i = 0; i < titulos.length; ++i) {
			tabelaTentativas.setText(LINHA_TITULO, OFFSET_COLUNA+i, titulos[i]);
		}
	}
	
}
