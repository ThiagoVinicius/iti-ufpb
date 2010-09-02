package jogoshannon.client;

import jogoshannon.shared.ConjuntoFrasesStub;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProducaoProgramaAsync {

    void getConjuntosFrases(AsyncCallback<ConjuntoFrasesStub[]> callback);

}
