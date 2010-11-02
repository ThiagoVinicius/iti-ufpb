package jogoshannon.client.remote;

import java.util.List;

import jogoshannon.shared.CobaiaStub;
import jogoshannon.shared.DadosJogo;
import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.SessaoInvalidaException;
import jogoshannon.shared.Tentativas;
import jogoshannon.shared.UsuarioNaoEncontradoException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("juizSoletrando")
public interface JuizSoletrando extends RemoteService {

    public DadosJogo getFrases(Long idExperimento) throws SessaoInvalidaException;

    public void atualizaTentativas(Tentativas contadores[])
            throws SessaoInvalidaException;

    public CobaiaStub[] getResultados(List<Long> id)
            throws UsuarioNaoEncontradoException;
    
    public ExperimentoStub getExperimentoStub (Long id);
    
    public ExperimentoStub[] getExperimentos();
    
    public boolean souAdmin ();

}
