package jogoshannon.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class JogoCompletoEvent extends GwtEvent<JogoCompletoHandler> {

    public static final Type<JogoCompletoHandler> TIPO = new Type<JogoCompletoHandler>();

    @Override
    protected void dispatch(JogoCompletoHandler handler) {
        handler.onJogoCompletoEvent(this);
    }

    @Override
    public Type<JogoCompletoHandler> getAssociatedType() {
        return TIPO;
    }

}
