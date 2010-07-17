package jogoshannon.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class TentativaEvent extends GwtEvent<TentativaHandler> {

    public static final Type<TentativaHandler> TIPO = new Type<TentativaHandler>();

    public TentativaEvent(boolean correta, char letra) {
        this.correta = correta;
        this.letra = letra;
    }

    private boolean correta;
    private char letra;

    @Override
    protected void dispatch(TentativaHandler handler) {
        handler.onTentativaEvent(this);
    }

    @Override
    public Type<TentativaHandler> getAssociatedType() {
        return TIPO;
    }

    public boolean getCorreta() {
        return correta;
    }

    public char getLetra() {
        return letra;
    }

}
