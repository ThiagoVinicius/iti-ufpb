package jogoshannon.client.util;

import java.util.Collection;
import java.util.HashMap;

import jogoshannon.client.event.UsuarioCheckBoxEvent;
import jogoshannon.client.view.UsuarioWidget;
import jogoshannon.shared.CobaiaStub;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.CheckBox;

public class ConjuntoUsuarios {

    private HashMap<Long, UsuarioWidget> widgets;
    private HashMap<Long, CobaiaStub> cobaias;
    private final SimpleEventBus eventos;

    public ConjuntoUsuarios(SimpleEventBus eventos) {
        this.eventos = eventos;
        this.widgets = new HashMap<Long, UsuarioWidget>();
        this.cobaias = new HashMap<Long, CobaiaStub>();
    }

    public boolean adicionaWidget(final UsuarioWidget e) {

        boolean jaExiste = widgets.containsKey(e.getId());

        if (jaExiste) {
            return false;
        }

        widgets.put(e.getId(), e);
        e.getBotaoRemover().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox origem = (CheckBox) event.getSource();
                    UsuarioCheckBoxEvent vouDisparar = 
                        new UsuarioCheckBoxEvent(e, origem.getValue());
                    eventos.fireEvent(vouDisparar);
                }

            }
        });

        return true;

    }
    
    public boolean adicionaCobaia(final CobaiaStub e) {
        boolean jaExiste = cobaias.containsKey(e.getId());
        if (jaExiste) {
            return false;
        }
        cobaias.put(e.getId(), e);
        return true;
    }

    public UsuarioWidget getWidget(long id) {
        return widgets.get(id);
    }
    
    public CobaiaStub getCobaia(long id) {
        return cobaias.get(id);
    }

    public UsuarioWidget removeWidget(long id) {
        return widgets.remove(id);
    }
    
    public CobaiaStub removeCobaia (long id) {
        return cobaias.remove(id);
    }
    
    public Collection<UsuarioWidget> getTodosWidgets() {
        return widgets.values();
    }
    
    public Collection<CobaiaStub> getTodasCobaias() {
        return cobaias.values();
    }
    
    public void limparWidgets () {
        widgets.clear();
    }
    
    public void limparCobaias () {
        cobaias.clear();
    }
    
    public void limparTudo () {
        limparWidgets();
        limparCobaias();
    }

}
