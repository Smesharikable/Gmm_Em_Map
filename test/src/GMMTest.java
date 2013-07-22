/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import Jama.Matrix;
import java.math.BigDecimal;
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
public class GMMTest {
    
    public GMMTest() {
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
     * Test of getMu method, of class GMM.
     */
    @Test
    public void testGetMu() {
        System.out.println("getMu");
        GMM instance = null;
        Matrix[] expResult = null;
        Matrix[] result = instance.getMu();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSigma method, of class GMM.
     */
    @Test
    public void testGetSigma() {
        System.out.println("getSigma");
        GMM instance = null;
        Matrix[] expResult = null;
        Matrix[] result = instance.getSigma();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getP method, of class GMM.
     */
    @Test
    public void testGetP() {
        System.out.println("getP");
        GMM instance = null;
        double[] expResult = null;
        BigDecimal[] result = instance.getP();
        //assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of prior method, of class GMM.
     */
    @Test
    public void testPrior() {
        System.out.println("prior");
        
        double[] input = {2, 2};
        double[][] inmu = {{1, 2}, {2, 3}};
//        double[][][] insigma = {
//            {{5.7718, 1.7160},  {1.7160, 0.5102}}, 
//            {{3.0287, -0.7990}, {-0.7990, 0.2108}}
//        };
        double[][][] insigma = {
            {{2, 0}, {0, 0.5}}, 
            {{2, 0}, {0, 0.5}}
        };
        double[] p = {0.5, 0.5};
        
        Matrix vector = new Matrix(input, 2);
        Matrix[] mu = new Matrix[2];
        mu[0] = new Matrix(inmu[0], 2);
        mu[1] = new Matrix(inmu[1], 2);
        Matrix[] sigma = new Matrix[2];
        sigma[0] = new Matrix(insigma[0], 2, 2);
        sigma[1] = new Matrix(insigma[1], 2, 2);
        
        GMM instance = new GMM(mu, sigma, p);
        
        double expResult = 0.25;
        double result = instance.prior(vector).doubleValue();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of posterior method, of class GMM.
     */
    @Test
    public void testPosterior() {
        System.out.println("posterior");
        
        double[] inVect = {2, 2};
        double[][] inmu = {{1, 2}, {2, 3}};
//        double[][][] insigma = {
//            {{5.7718, 1.7160},  {1.7160, 0.5102}}, 
//            {{3.0287, -0.7990}, {-0.7990, 0.2108}}
//        };
        double[][][] insigma = {
            {{2, 0}, {0, 0.5}}, 
            {{2, 0}, {0, 0.5}}
        };
        double[] p = {0.5, 0.5};
        Matrix[] input = new Matrix[1];
        input[0] = new Matrix(inVect, 2);
        
        Matrix[] mu = new Matrix[2];
        mu[0] = new Matrix(inmu[0], 2);
        mu[1] = new Matrix(inmu[1], 2);
        Matrix[] sigma = new Matrix[2];
        sigma[0] = new Matrix(insigma[0], 2, 2);
        sigma[1] = new Matrix(insigma[1], 2, 2);
        
        GMM instance = new GMM(mu, sigma, p);
        Matrix[] expResult = new Matrix[1];
        double[] res1 = {0.6792, 0.3208}; 
        expResult[0] = new Matrix(res1, 2);
        Matrix[] result = instance.posterior(input);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}