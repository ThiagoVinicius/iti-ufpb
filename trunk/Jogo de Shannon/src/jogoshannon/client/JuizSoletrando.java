package jogoshannon.client;

import jogoshannon.shared.Frase;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("juizSoletrando")
public interface JuizSoletrando extends RemoteService {
	
	public long getId();
	public Frase getFrase(int id);
	public int getTotalFrases();
	public void atualizaTentativas(int fraseId, int contadores[]);

}
