package jogoshannon.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class JogoCompletoEvent extends GwtEvent<JogoCompletoEventHandler> {

	private static final Type<JogoCompletoEventHandler> TIPO = 
		new Type<JogoCompletoEventHandler>();
	
	@Override
	protected void dispatch(JogoCompletoEventHandler handler) {
		handler.onJogoCompletoEvent(this);
	}

	@Override
	public Type<JogoCompletoEventHandler> getAssociatedType() {
		return TIPO;
	}

}
