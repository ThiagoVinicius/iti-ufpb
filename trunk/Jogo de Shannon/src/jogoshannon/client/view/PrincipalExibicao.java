/**
 * 
 */
package jogoshannon.client.view;

import java.util.LinkedList;
import java.util.List;

import jogoshannon.client.Jogo_de_Shannon;
import jogoshannon.client.presenter.PrincipalApresentador;
import jogoshannon.client.util.VerificadorDeCampo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * @author Thiago
 *
 */
public class PrincipalExibicao extends Composite 
implements PrincipalApresentador.Exibicao {

    private static PrincipalExibicaoUiBinder uiBinder = GWT
            .create(PrincipalExibicaoUiBinder.class);

    interface PrincipalExibicaoUiBinder extends
            UiBinder<Widget, PrincipalExibicao> {
    }
    
    
    @UiField
    protected Label labelId;
    
    @UiField
    protected Label labelFrase;
    
    @UiField
    protected Image carregando;
    
    @UiField
    protected Label labelErro;
    
    @UiField
    protected Label labelCerto;
    
    @UiField
    protected TecladoVirtual teclado;
    
    private DialogBox fimDeJogo;
    
    private PopupPanel situacaoServidor;
    
    private List<HandlerRegistration> desregistrar = new LinkedList<HandlerRegistration>();
    
    private static final int QUEBRA_LINHA_APOS = 50;

    public PrincipalExibicao() {
        initWidget(uiBinder.createAndBindUi(this));
        
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
        
        amarrarEventos();
    }

    private void amarrarEventos() {

        desregistrar.add(Event
                .addNativePreviewHandler(new NativePreviewHandler() {
                    @Override
                    public void onPreviewNativeEvent(NativePreviewEvent event) {
                        if (event.getTypeInt() == Event.ONKEYPRESS) {
                            NativeEvent ne = event.getNativeEvent();
                            boolean aceito = !VerificadorDeCampo
                                    .teclaModificadora(ne);
                            if (aceito) {
                                char tecla = (char) (ne.getKeyCode() & 0xffff);
                                teclado.pressionaTecla(tecla);
                            }
                        }
                    }
                }));
        
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

    public void setEstadoServidor(PrincipalApresentador.EstadosServidor estadoAtual) {
        setEstadoServidor(estadoAtual, null);
    }
    
    public void setEstadoServidor(PrincipalApresentador.EstadosServidor estadoAtual, String info) {
        
        if (info != null) {
            Label lab = (Label) situacaoServidor.getWidget();
            lab.setText("Comunicando com o servidor ("+info+")");
        } else {
            Label lab = (Label) situacaoServidor.getWidget();
            lab.setText("Comunicando com o servidor");
        }
        
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
        for (HandlerRegistration r : desregistrar) {
            r.removeHandler();
        }
        super.onDetach();
    }

    
}
