package jogoshannon.client.event;

import jogoshannon.client.view.UsuarioWidget;

import com.google.gwt.event.shared.GwtEvent;

public class UsuarioCheckBoxEvent extends GwtEvent<UsuarioCheckBoxHandler> {

    public static final Type<UsuarioCheckBoxHandler> TIPO = new Type<UsuarioCheckBoxHandler>();

    private UsuarioWidget origem;
    private boolean marcado;
    
    public UsuarioCheckBoxEvent(UsuarioWidget origem, boolean marcado) {
        this.origem = origem;
        this.marcado = marcado;
    }

    @Override
    protected void dispatch(UsuarioCheckBoxHandler handler) {
        handler.onCheckboxMudou(this);
    }

    @Override
    public Type<UsuarioCheckBoxHandler> getAssociatedType() {
        return TIPO;
    }

    public UsuarioWidget getOrigem() {
        return origem;
    }
    
    public boolean getEstaMarcado() {
        return marcado;
    }

}
