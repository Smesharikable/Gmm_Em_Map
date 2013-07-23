/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import Jama.Matrix;
import model.GMM;
import model.GMMTest;
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
public class MapTest {
    
    public MapTest() {
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
     * Test of fitByMeans method, of class Map.
     */
    @Test
    public void testFitByMeans() {
        System.out.println("fitByMeans");
        
//        double[][] dinput = {{2, 2}, 
//                             {1, 2.5}, 
//                             {3, 4}};
        double[][] dinput = {{2, 1,   3}, 
                             {2, 2.5, 4}};       
        double[][] mu = {{1.0420, 2.0189},
                         {2.0304, 3.0247}};
        Matrix input = new Matrix(dinput, 2, 3);
        int iterations = 1;
        
        GMM testGMM = GMMTest.testGMM();
        Map instance = new Map(testGMM);
        Matrix[] expResult = new Matrix[2];
        expResult[0] = new Matrix(mu[0], 2);
        expResult[1] = new Matrix(mu[1], 2);
        
        GMM result = instance.fitByMeans(input, iterations);
        Matrix[] resMu = result.getMu();
        assertArrayEquals(expResult[0].getRowPackedCopy(), resMu[0].getRowPackedCopy(), 0.0001);
        assertArrayEquals(expResult[1].getRowPackedCopy(), resMu[1].getRowPackedCopy(), 0.0001);
    }

    /**
     * Test of fitByWeights method, of class Map.
     */
    @Test
    public void testFitByWeights() {
        System.out.println("fitByWeights");
        Matrix input = null;
        int iterations = 0;
        int states = 0;
        Map instance = null;
        GMM[] expResult = null;
        GMM[] result = instance.fitByWeights(input, iterations, states);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}