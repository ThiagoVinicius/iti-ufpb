package jogoshannon.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UsuarioWidget extends Composite {

	private final long id;
	private Label idLabel;
	private Button remover;
	private Image carregando;
	
	public UsuarioWidget (long id) {
		super();
		
		this.id = id;
		
		SimplePanel root = new SimplePanel();
		initWidget(root);
		
		idLabel = new Label(""+id);
		idLabel.setWidth("5em");
		
		remover = new Button("Remover");
		remover.setEnabled(false);
		
		carregando = new Image("ampulheta.gif");
		
		HorizontalPanel painelPrincipal = new HorizontalPanel();
		painelPrincipal.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		painelPrincipal.add(idLabel);
		painelPrincipal.add(remover);
		painelPrincipal.add(carregando);
		
		root.setWidget(painelPrincipal);
		
	}
	
	public HasClickHandlers getBotaoRemover() {
		return remover;
	}
	
	public void ativaBotaoRemover (boolean ativar) {
		remover.setEnabled(ativar);
	}
	
	public long getId () {
		return id;
	}

	@Override
	public int hashCode() {
		return new Long(id).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioWidget other = (UsuarioWidget) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
