package jogoshannon.client.remote;

import java.util.List;

import jogoshannon.shared.CobaiaStub;
import jogoshannon.shared.DadosJogo;
import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.Tentativas;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface JuizSoletrandoAsync {

    void getFrases(Long idExperimento, AsyncCallback<DadosJogo> callback);

    void atualizaTentativas(Tentativas contadores[],
            AsyncCallback<Void> callback);

    void souAdmin(AsyncCallback<Boolean> callback);

    void getExperimentoStub(Long id, AsyncCallback<ExperimentoStub> callback);

    void getExperimentos(AsyncCallback<ExperimentoStub[]> callback);

    void getResultados(List<Long> id, AsyncCallback<CobaiaStub[]> callback);

}
