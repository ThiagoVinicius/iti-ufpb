package jogoshannon.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class FraseCompletaEvent extends GwtEvent<FraseCompletaEventHandler> {
	
	private static final Type<FraseCompletaEventHandler> TIPO = new 
		Type<FraseCompletaEventHandler>();

	@Override
	protected void dispatch(FraseCompletaEventHandler handler) {
		handler.onFraseCompletaEvent(this);
	}

	@Override
	public Type<FraseCompletaEventHandler> getAssociatedType() {
		return TIPO;
	}

}
