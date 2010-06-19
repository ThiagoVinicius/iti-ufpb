package jogoshannon.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class TentativaEvent extends GwtEvent<TentativaEventHandler> {
	
	private static final Type<TentativaEventHandler> TIPO = new Type<TentativaEventHandler>();
	
	public TentativaEvent (boolean correta) {
		this.correta = correta;
	}
	
	private boolean correta;
	
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

}
