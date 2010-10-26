package jogoshannon.client.remote;

import jogoshannon.shared.ConjuntoFrasesStub;
import jogoshannon.shared.ExperimentoStub;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("admin/producao")
public interface ProducaoPrograma extends RemoteService {
    
    public void checkAdmin();
    public ConjuntoFrasesStub[] getConjuntosFrases();
    public long putExperimento (ExperimentoStub exp);
    public String getUploadUrl (String titulo, String autor, String descricao);
    
}
