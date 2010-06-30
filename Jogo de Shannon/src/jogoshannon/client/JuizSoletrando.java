package jogoshannon.client;

import jogoshannon.shared.Frase;
import jogoshannon.shared.SessaoInvalidaException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("juizSoletrando")
public interface JuizSoletrando extends RemoteService {
	
	public long getId();
	public Frase getFrase(int id) throws SessaoInvalidaException;
	public int getTotalFrases() throws SessaoInvalidaException;
	public void atualizaTentativas(int fraseId, int contadores[]) throws SessaoInvalidaException;
	public void destruirSessao();

}
