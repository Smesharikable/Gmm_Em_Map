/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import Jama.Matrix;
import java.math.BigDecimal;
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
        double[] dinput_1 = {2, 2};
        double[][] dinput_2 = {{1, 3}, 
                             {2.5, 4}};
        Matrix input_1 = new Matrix(dinput_1, 2);
        Matrix input_2 = new Matrix(dinput_2, 2, 2);
        int iterations = 1;
        
        GMM testGMM = GMMTest.testGMM();
        Map instance = new Map(testGMM);
        
        double[][] expResult = {{0.5054, 0.4946},
                                {0.4880, 0.5120}};
        
        double[][] res = new double[2][testGMM.getNComponents()];
        res[0] = instance.fitByWeights(input_1, iterations).getP();
        res[1] = instance.fitByWeights(input_2, iterations).getP();
        
        assertArrayEquals(expResult[0], res[0], 0.0001);
        assertArrayEquals(expResult[1], res[1], 0.0001);
    }
}