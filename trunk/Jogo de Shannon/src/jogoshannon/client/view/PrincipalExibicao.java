package jogoshannon.client.view;

import jogoshannon.client.Jogo_de_Shannon;
import jogoshannon.client.presenter.PrincipalApresentador;
import jogoshannon.client.util.VerificadorDeCampo;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class PrincipalExibicao extends Composite implements
        PrincipalApresentador.Exibicao {

    public static enum EstadosServidor {
        AGUARDANDO_RESPOSTA, TUDO_CERTO, SEM_ALTERACOES;
    }

    private Label labelFrase;
    private Label labelErro;
    private Label labelCerto;
    private Label labelId;
    private Image carregando;
    private DialogBox fimDeJogo;
    private PopupPanel situacaoServidor;
    private TecladoVirtual teclado;

    private static final int QUEBRA_LINHA_APOS = 50;

    public PrincipalExibicao() {

        SimplePanel root = new SimplePanel();
        initWidget(root);

        FlowPanel titulo_id_jogo = new FlowPanel();

        SimplePanel jogo_ui = new SimplePanel();
        jogo_ui.addStyleName("jogo-ui");
        jogo_ui.addStyleName("centralizado");

        DecoratorPanel painelEntrada = new DecoratorPanel();

        VerticalPanel subRoot = new VerticalPanel();
        subRoot.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        subRoot.setBorderWidth(0);

        HorizontalPanel painel = new HorizontalPanel();
        painel.setBorderWidth(0);
        painel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        painel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        painel.setSpacing(10);
        labelFrase = new Label();
        labelFrase.setWordWrap(true);
        labelFrase.setWidth("40em");
        labelFrase.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        carregando = new Image(Jogo_de_Shannon.IMAGENS.ampulheta());
        painel.add(labelFrase);
        painel.add(carregando);

        fimDeJogo = new DialogBox(false, true);
        fimDeJogo.setText("Fim de jogo");
        fimDeJogo.setWidget(new Label("Parabéns, você concluiu o jogo!\n"
                + "Obrigado pela sua participacao.\n"
                + "(para jogar novamente, recarregue a página)"));

        fimDeJogo.setModal(false);
        fimDeJogo.setVisible(false);
        fimDeJogo.hide();
        fimDeJogo.setGlassEnabled(true);

        situacaoServidor = new PopupPanel(false, false);
        situacaoServidor.add(new Label("Comunicando com o servidor"));
        situacaoServidor.setStylePrimaryName("servidor-popup");

        labelErro = new Label(" ");
        labelErro.setStyleName("respostaErrada");

        labelCerto = new Label(" ");
        labelCerto.setStyleName("respostaCorreta");

        teclado = new TecladoVirtual();
        teclado.addStyleName("teclado");
        teclado.addStyleName("centralizado");

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
        painelId.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        painelId.addStyleName("jogo-id");
        HTML tituloId = new HTML("ID # ");
        labelId = new Label("");
        painelId.add(tituloId);
        painelId.add(labelId);

        // titulo_id_jogo.add(situacaoServidor);
        titulo_id_jogo.add(painelId);
        titulo_id_jogo.add(painelTitulo);
        titulo_id_jogo.add(jogo_ui);
        titulo_id_jogo.add(teclado);

        amarrarEventos();

        root.add(titulo_id_jogo);

        // RootPanel.get().add(fimDeJogo);
    }

    private void amarrarEventos() {

        Event.addNativePreviewHandler(new NativePreviewHandler() {
            @Override
            public void onPreviewNativeEvent(NativePreviewEvent event) {
                if (event.getTypeInt() == Event.ONKEYPRESS) {
                    NativeEvent ne = event.getNativeEvent();
                    boolean aceito = !VerificadorDeCampo.teclaModificadora(ne);
                    if (aceito) {
                        char tecla = (char) (ne.getKeyCode() & 0xffff);
                        teclado.pressionaTecla(tecla);
                    }
                }
            }
        });

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

        labelFrase.setText(paraExibir.toString());
    }

    @Override
    public void setTextoErro(String erro) {
        labelErro.setText(erro);
    }

    public Widget asWidget() {
        return this;
    }

    @Override
    public TecladoVirtual getTeclado() {
        return teclado;
    };

    public void setCarregando(boolean estado) {
        if (estado) {
            teclado.desativaTodasTeclas();
        } else {
            teclado.ativaTodasTeclas();
        }
        carregando.setVisible(estado);
    }

    public void setEstadoServidor(EstadosServidor estadoAtual) {
        switch (estadoAtual) {
        case AGUARDANDO_RESPOSTA:
            situacaoServidor.setPopupPositionAndShow(new PositionCallback() {
                @Override
                public void setPosition(int offsetWidth, int offsetHeight) {
                    int left = (Window.getClientWidth() - situacaoServidor
                            .getOffsetWidth()) / 2;
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
    public void exibeFimDeJogo(String titulo, String texto) {
        teclado.desativaTodasTeclas();

        fimDeJogo.setText(titulo);
        ((HasText) fimDeJogo.getWidget()).setText(texto);

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
    public void setTextoParabens(String texto) {
        if (!texto.isEmpty()) {
            timerLimparParabens.cancel();
            timerLimparParabens.schedule(7500);
        }

        labelCerto.setText(texto);

    }

    public void setId(String id) {
        labelId.setText(id);
        Window.setTitle(Jogo_de_Shannon.TITULO_PADRAO + " - jogando com id = "
                + id);
    }

    @Override
    public void ativaTodasTeclas() {
        teclado.ativaTodasTeclas();
    }

    @Override
    public void desativaTecla(char tecla) {
        teclado.desativaTecla(tecla);
    }

    @Override
    protected void onDetach() {
        fimDeJogo.hide();
        super.onDetach();
    }

}
