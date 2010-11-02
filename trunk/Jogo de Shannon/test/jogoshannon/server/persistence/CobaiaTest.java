package jogoshannon.server.persistence;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.jdo.PersistenceManager;

import jogoshannon.server.GestorPersistencia;
import jogoshannon.server.persistent.Cobaia;
import jogoshannon.server.persistent.Rodada;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class CobaiaTest extends TestCase {
    
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    
    @Before
    public void setUp () {
        helper.setUp();
    }
    
    @After
    public void tearDown () {
        helper.tearDown();
    }
    
    @Test
    public void testVoid () {
    }
    
    @Test
    public void testCriarCobaia () {
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        Cobaia cob = new Cobaia();
        try {
            pm.makePersistent(cob);
        } finally {
            pm.close();
        }
    }
    
    @Test
    public void testEditarResultados () {
        
        List<Rodada> res1 = Arrays.asList(new Rodada[] {new Rodada(new int[] {1, 2, 3, 4, 5, 6} )});
        List<Rodada> res2 = Arrays.asList(new Rodada[] {new Rodada(new int[] {6, 5, 4, 3, 2, 1} )});
        
        PersistenceManager pm;
        
        pm = GestorPersistencia.get().getPersistenceManager();
        Cobaia cob = new Cobaia();
        cob.setDesafios(res1);
        try {
            pm.makePersistent(cob);
        } finally {
            pm.close();
        }
        
        Long id = cob.getKey().getId();
        
        pm = GestorPersistencia.get().getPersistenceManager();
        cob = null;
        try {
            cob = pm.getObjectById(Cobaia.class, id);
            assertEquals(res1, cob.getDesafios());
            cob.setDesafios(res2);
        } finally {
            pm.close();
        }
        
        pm = GestorPersistencia.get().getPersistenceManager();
        cob = null;
        try {
            cob = pm.getObjectById(Cobaia.class, id);
            assertEquals(res2, cob.getDesafios());
        } finally {
            pm.close();
        }
        
    }
    
}
