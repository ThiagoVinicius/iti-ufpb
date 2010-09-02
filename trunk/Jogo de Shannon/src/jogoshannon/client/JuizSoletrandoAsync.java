package jogoshannon.client;

import jogoshannon.shared.Frase;
import jogoshannon.shared.Tentativas;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface JuizSoletrandoAsync {

    void getId(AsyncCallback<Long> callback);

    void getFrases(Long idExperimento, AsyncCallback<Frase[]> callback);

    void atualizaTentativas(Tentativas contadores[],
            AsyncCallback<Void> callback);

    void getResultados(long id, AsyncCallback<Tentativas[]> callback);

}
