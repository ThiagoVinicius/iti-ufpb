package jogoshannon.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Desafio {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	private Key key;
	
	@Persistent
	private String frase;

	public void setFrase(String frase) {
		this.frase = frase;
	}

	public String getFrase() {
		return frase;
	}
	
	
	
}
