package jogoshannon.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class FraseCompletaEvent extends GwtEvent<FraseCompletaHandler> {
	
	public static final Type<FraseCompletaHandler> TIPO = new 
		Type<FraseCompletaHandler>();

	@Override
	protected void dispatch(FraseCompletaHandler handler) {
		handler.onFraseCompletaEvent(this);
	}

	@Override
	public Type<FraseCompletaHandler> getAssociatedType() {
		return TIPO;
	}

}
