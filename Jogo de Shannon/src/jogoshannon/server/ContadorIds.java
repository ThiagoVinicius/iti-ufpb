package jogoshannon.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class ContadorIds {
	
	//Esse id nunca muda - somente há uma instancia desse objeto no banco.
	@PrimaryKey
	@Persistent//(valueStrategy = IdGeneratorStrategy.IDENTITY)
	public Key chaveUnica;
	
	public static final Key CHAVE = KeyFactory.createKey(ContadorIds.class.getSimpleName(), 666L);
	
	@Persistent
	private long maximoId;
	
	public ContadorIds () {
		chaveUnica = CHAVE;
	}

	public void set(long maximoId) {
		this.maximoId = maximoId;
	}

	public long get() {
		return maximoId;
	}
	
	
	
}
