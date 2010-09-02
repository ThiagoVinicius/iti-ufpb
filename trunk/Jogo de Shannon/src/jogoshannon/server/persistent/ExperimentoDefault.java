package jogoshannon.server.persistent;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import jogoshannon.server.GestorPersistencia;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class ExperimentoDefault {
    
    @NotPersistent
    private static final long SINGLETON_ID = 1;
    
    @SuppressWarnings("unused")
    @PrimaryKey
    @Persistent
    private Key key;
    
    @Persistent
    private Key value;
    
    private synchronized static ExperimentoDefault getInstance(
            PersistenceManager pm) {
        ExperimentoDefault instance = null;
        try {
            instance = pm.getObjectById(ExperimentoDefault.class, SINGLETON_ID);
        } catch (JDOObjectNotFoundException e) {
        }

        if (instance == null) {
            instance = new ExperimentoDefault();
            instance.setValue(null);
            instance.key = KeyFactory.createKey(ExperimentoDefault.class
                    .getSimpleName(), SINGLETON_ID);
            pm.makePersistent(instance);
        }

        return instance;

    }
    
    public static void setKey(Key keyDefault) {
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        getInstance(pm).setValue(keyDefault);
        pm.close();
    }
    
    public static void setId(long idDefault) {
        Key newKey = KeyFactory.createKey(Experimento.class.getSimpleName(), idDefault);
        setKey(newKey);
    }
    
    public static Experimento getDefault (PersistenceManager pm) {
        Key key = getInstance(pm).getValue();
        Experimento result = null;
        
        if (key != null) {
            result = pm.getObjectById(Experimento.class, key);
        }
        
        return result;
        
    }
    
    private void setValue (Key value) {
        this.value = value;
    }
    
    private Key getValue () {
        return value;
    }
    
}
