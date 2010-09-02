package jogoshannon.client;

import jogoshannon.shared.ConjuntoFrasesStub;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("producao")
public interface ProducaoPrograma extends RemoteService {
    
    public ConjuntoFrasesStub[] getConjuntosFrases();
    
}
