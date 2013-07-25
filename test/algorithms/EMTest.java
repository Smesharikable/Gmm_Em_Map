/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import Jama.Matrix;
import model.GMM;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author 1
 */
public class EMTest {
    
    public EMTest() {
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
     * Test of doEM method, of class EM.
     */
    @Test
    public void testDoEM() {
        System.out.println("doEM");
        Matrix input = null;
        EM instance = null;
        GMM expResult = null;
        GMM result = instance.doEM(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}