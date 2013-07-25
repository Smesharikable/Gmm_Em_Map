package algorithms;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Snezhana
 */
public class ViterbiTest {
    
    public ViterbiTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of myMax method, of class Viterbi.
     */
    @Test
    public void testMyMax() {       
        double[] M = {1, 2, 3, 0, -8};
        Viterbi instance = null;
        double expResult = 3;               
        double result = instance.myMax(M);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of argmax method, of class Viterbi.
     */
    @Test
    public void testArgmax() {
        System.out.println("argmax");
        double[] M = {1, 2, 3, 0, -8};
        Viterbi instance = null;
        int expResult = 2;
        int result = instance.argmax(M);
        assertEquals(expResult, result);
    }
}