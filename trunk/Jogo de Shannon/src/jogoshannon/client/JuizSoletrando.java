package jogoshannon.client;

import jogoshannon.shared.Frase;
import jogoshannon.shared.SessaoInvalidaException;
import jogoshannon.shared.Tentativas;
import jogoshannon.shared.UsuarioNaoEncontradoException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("juizSoletrando")
public interface JuizSoletrando extends RemoteService {

    public long getId();

    public Frase[] getFrases(Long idExperimento) throws SessaoInvalidaException;

    public void atualizaTentativas(Tentativas contadores[])
            throws SessaoInvalidaException;

    public Tentativas[] getResultados(long id)
            throws UsuarioNaoEncontradoException;

}
