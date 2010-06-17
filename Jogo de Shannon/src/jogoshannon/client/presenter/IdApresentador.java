package jogoshannon.client.presenter;

import jogoshannon.client.JuizSoletrandoAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;

public class IdApresentador implements Apresentador {
	
	JuizSoletrandoAsync servidor;
	HasText id;
	
	public IdApresentador (JuizSoletrandoAsync servidor) {
		this.servidor = servidor;
	}

	@Override
	public void vai(HasWidgets pagina) {
		pagina.clear();
		Label label = new Label();
		pagina.add(label);
		id = label;
		servidor.getId(new AsyncCallback<Long>() {
			public void onSuccess(Long result) {
				preecheId(result.toString());
			}
			public void onFailure(Throwable caught) {
				preecheId("???");
			}
		});
	}
	
	private void preecheId(String textoId) {
		id.setText(textoId);
	}

}
