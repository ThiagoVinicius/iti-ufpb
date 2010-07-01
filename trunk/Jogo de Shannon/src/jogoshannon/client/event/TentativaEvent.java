package jogoshannon.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class TentativaEvent extends GwtEvent<TentativaEventHandler> {
	
	public static final Type<TentativaEventHandler> TIPO = new Type<TentativaEventHandler>();
	
	public TentativaEvent (boolean correta, char letra) {
		this.correta = correta;
		this.letra = letra;
	}
	
	private boolean correta;
	private char letra;
	
	@Override
	protected void dispatch(TentativaEventHandler handler) {
		handler.onTentativaEvent(this);
	}

	@Override
	public Type<TentativaEventHandler> getAssociatedType() {
		return TIPO;
	}

	public boolean getCorreta () {
		return correta;
	}
	
	public char getLetra () {
		return letra;
	}

}
