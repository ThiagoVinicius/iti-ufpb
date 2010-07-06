package jogoshannon.server;

import java.util.Arrays;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Desafio {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Usuario usuario;
	
	@Persistent
	private FraseStore frase;
	
	@Persistent
	private int tentativas[];
	
	public Desafio (int numeroDesafios) {
		tentativas = new int[numeroDesafios];
	}
	
	public Desafio (int tentativas[]) {
		this.tentativas = tentativas;
	}

	public int[] getTentativas() {
		return tentativas;
	}

	public void setTentativas(int tentativas[]) {
		this.tentativas = tentativas;
	}
	
	public void somaTentativas (int somarCom[]) {
		for (int i = 0; i < somarCom.length; ++i) {
			tentativas[i] += somarCom[i];
		}
		//tocando na variável, dando a dica de que ela precisa ser salva. 
		setTentativas(tentativas);
	}
	
	public String toString () {
		return Arrays.toString(getTentativas());
	}
	
}
