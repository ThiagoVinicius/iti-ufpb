package jogoshannon.client.view;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TecladoVirtual extends Composite implements
HasValueChangeHandlers<Character>, ValueChangeHandler<Character> {

    private static TecladoVirtualUiBinder uiBinder = GWT
            .create(TecladoVirtualUiBinder.class);

    interface TecladoVirtualUiBinder extends UiBinder<Widget, TecladoVirtual> {
    }
    
    protected interface Css extends CssResource {
        String botaoHabilitado();
        String botaoDesabilitado();
    }

    @UiField
    protected Css style;
    
    @UiField
    protected VerticalPanel vPanel;
    
    @UiField
    protected SimplePanel container;
    
    private Teclado tecladoModel;

    // sinonimo de 'this', nas classes internas
    private final TecladoVirtual self = this;

    private static final String NOMES_TECLAS[][] = {
            { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", },
            { "A", "S", "D", "F", "G", "H", "J", "K", "L", "Ç", },
            { "Z", "X", "C", "V", "B", "N", "M", }, 
            { "Espaço" }

    };

    private static final char BOTOES_TECLAS[][] = {
            { 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', },
            { 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Ç', },
            { 'Z', 'X', 'C', 'V', 'B', 'N', 'M', }, 
            { '\u0020' }

    };

    private class Teclado {

        private HashMap<Character, BotaoTeclado> teclas = new HashMap<Character, BotaoTeclado>();

        public Teclado() {
        }

        public void adicionaBotao(BotaoTeclado novo) {
            teclas.put(novo.tecla, novo);
            setAtivado(novo, true);
        }

        public void pressionarTecla(char tecla) {
            BotaoTeclado aPropria = teclas.get(Character.toUpperCase(tecla));
            if (aPropria != null) {
                if (aPropria.isEnabled()) {
                    ValueChangeEvent.fire(self, tecla);
                }
            }
        }

        @SuppressWarnings("unused")
        private boolean estaAtivado(char tecla) {
            BotaoTeclado checar = teclas.get(Character.toUpperCase(tecla));
            if (checar != null) {
                return checar.isEnabled();
            }
            return false;
        }

        private void setAtivado(BotaoTeclado botao, boolean estado) {
            botao.setEnabled(estado);
            if (estado) {
                botao.setStylePrimaryName(style.botaoHabilitado());
            } else {
                botao.setStylePrimaryName(style.botaoDesabilitado());
                botao.setFocus(false);
            }
        }

        public void desabilitaTecla(char tecla) {
            BotaoTeclado desabilitar = teclas.get(Character.toUpperCase(tecla));
            if (desabilitar != null) {
                setAtivado(desabilitar, false);
            }
        }

        public void desabilitarTodasTeclas() {
            for (BotaoTeclado desabilitar : teclas.values()) {
                if (desabilitar != null) {
                    setAtivado(desabilitar, false);
                }
            }
        }

        @SuppressWarnings("unused")
        public void habilitarTecla(char tecla) {
            BotaoTeclado habilitar = teclas.get(Character.toUpperCase(tecla));
            if (habilitar != null) {
                setAtivado(habilitar, true);
            }
        }

        public void habilitarTodasTeclas() {
            for (BotaoTeclado habilitar : teclas.values()) {
                if (habilitar != null) {
                    setAtivado(habilitar, true);
                }
            }
        }

    }

    private class BotaoTeclado extends Button implements ClickHandler {

        public final char tecla;
        public final Teclado meuPai;

        public BotaoTeclado(String html, char tecla, Teclado meuPai) {
            super(html);
            this.tecla = tecla;
            this.meuPai = meuPai;
            bind();
        }

        private void bind() {
            meuPai.adicionaBotao(this);
            addClickHandler(this);
        }

        @Override
        public void onClick(ClickEvent event) {
            meuPai.pressionarTecla(tecla);
        }

    }

    public TecladoVirtual() {

        initWidget(uiBinder.createAndBindUi(this));

        tecladoModel = new Teclado();
        montarTeclado(tecladoModel);

    }

    private void montarTeclado(Teclado teclado) {

        for (int i = 0; i < BOTOES_TECLAS.length; ++i) {
            HorizontalPanel hPanel = new HorizontalPanel();
            vPanel.add(hPanel);
            for (int j = 0; j < BOTOES_TECLAS[i].length; ++j) {
                BotaoTeclado novo = new BotaoTeclado(NOMES_TECLAS[i][j],
                        BOTOES_TECLAS[i][j], teclado);
                hPanel.add(novo);
            }
        }

    }

    public void pressionaTecla(char tecla) {
        tecladoModel.pressionarTecla(tecla);
    }

    public void ativaTodasTeclas() {
        tecladoModel.habilitarTodasTeclas();
    }

    public void desativaTodasTeclas() {
        tecladoModel.desabilitarTodasTeclas();
    }

    public void desativaTecla(char tecla) {
        tecladoModel.desabilitaTecla(tecla);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<Character> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void onValueChange(ValueChangeEvent<Character> event) {
        tecladoModel.desabilitaTecla(event.getValue());
    }

}

