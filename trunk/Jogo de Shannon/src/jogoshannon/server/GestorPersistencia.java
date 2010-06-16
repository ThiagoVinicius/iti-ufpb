package jogoshannon.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class GestorPersistencia {
	
	private final static PersistenceManagerFactory singleton = 
		JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	private GestorPersistencia () {};
	
	public static PersistenceManagerFactory get () {
		return singleton;
	}
	
}
