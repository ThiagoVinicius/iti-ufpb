package jogoshannon.client;

import jogoshannon.shared.Frase;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface JuizSoletrandoAsync {

	void getId(AsyncCallback<Long> callback);

	void getFrase(int id, AsyncCallback<Frase> callback);

	void getTotalFrases(AsyncCallback<Integer> callback);

	void atualizaTentativas(int fraseId, int contadores[],
			AsyncCallback<Void> callback);

	void destruirSessao(AsyncCallback<Void> callback);

}
