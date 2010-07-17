package jogoshannon.client.event;

import jogoshannon.client.view.UsuarioWidget;

import com.google.gwt.event.shared.GwtEvent;

public class UsuarioRemovidoEvent extends GwtEvent<UsuarioRemovidoHandler> {

    public static final Type<UsuarioRemovidoHandler> TIPO = new Type<UsuarioRemovidoHandler>();

    private UsuarioWidget origem;

    public UsuarioRemovidoEvent(UsuarioWidget origem) {
        this.origem = origem;
    }

    @Override
    protected void dispatch(UsuarioRemovidoHandler handler) {
        handler.onUsuarioRemovido(this);
    }

    @Override
    public Type<UsuarioRemovidoHandler> getAssociatedType() {
        return TIPO;
    }

    public UsuarioWidget getOrigem() {
        return origem;
    }

}
