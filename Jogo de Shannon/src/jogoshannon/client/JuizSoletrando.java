package jogoshannon.client;

import jogoshannon.shared.Frase;
import jogoshannon.shared.SessaoInvalidaException;
import jogoshannon.shared.Tentativas;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("juizSoletrando")
public interface JuizSoletrando extends RemoteService {
	
	public long getId();
	public Frase[] getFrases() throws SessaoInvalidaException;
	public void atualizaTentativas(Tentativas contadores[]) throws SessaoInvalidaException;

}
