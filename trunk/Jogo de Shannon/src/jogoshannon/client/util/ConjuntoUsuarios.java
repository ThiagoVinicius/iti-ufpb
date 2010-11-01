package jogoshannon.client.util;

import java.util.Collection;
import java.util.HashMap;

import jogoshannon.client.event.UsuarioRemovidoEvent;
import jogoshannon.client.view.UsuarioWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;

public class ConjuntoUsuarios {

    private HashMap<Long, UsuarioWidget> oProprio;
    private final SimpleEventBus eventos;

    public ConjuntoUsuarios(SimpleEventBus eventos) {
        this.eventos = eventos;
        this.oProprio = new HashMap<Long, UsuarioWidget>();
    }

    public boolean adiciona(final UsuarioWidget e) {

        boolean jaExiste = oProprio.containsKey(e.getId());

        if (jaExiste) {
            return false;
        }

        oProprio.put(e.getId(), e);
        e.getBotaoRemover().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                UsuarioRemovidoEvent vouDisparar = new UsuarioRemovidoEvent(e);
                eventos.fireEvent(vouDisparar);
            }
        });

        return true;

    }

    public UsuarioWidget get(long id) {
        return oProprio.get(id);
    }

    public UsuarioWidget remover(long id) {
        return oProprio.remove(id);
    }
    
    public Collection<UsuarioWidget> getValores() {
        return oProprio.values();
    }
    
    public void clear () {
        oProprio.clear();
    }

}
