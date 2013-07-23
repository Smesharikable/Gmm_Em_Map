package model;

import Jama.Matrix;
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
     * Test of prior method, of class GMM.
     */
    @Test
    public void testPrior() {
        System.out.println("prior");
        
        double[] input = {2, 2};
//        double[][][] insigma = {
//            {{5.7718, 1.7160},  {1.7160, 0.5102}}, 
//            {{3.0287, -0.7990}, {-0.7990, 0.2108}}
//        };
        
        Matrix vector = new Matrix(input, 2);
        GMM instance = GMMTest.testGMM();
        
        double expResult = 0.0912;
        double result = instance.prior(vector).doubleValue();
        assertEquals(expResult, result, 0.0001);
    }

    /**
     * Test of posterior method, of class GMM.
     */
    @Test
    public void testPosterior() {
        System.out.println("posterior");    
        
        double[] inVect = {2, 2};
        Matrix input = new Matrix(inVect, 2);
        
        GMM instance = GMMTest.testGMM();
        
        Matrix[] expResult = new Matrix[2];
        double[] res1 = {0.6792};
        double[] res2 = {0.3208};
        expResult[0] = new Matrix(res1, 1);
        expResult[1] = new Matrix(res2, 1);
        Matrix[] result = instance.posterior(input);
        assertEquals(expResult[0].get(0, 0), result[0].get(0, 0), 0.0001);
    }
    
    public static GMM testGMM() {
        double[][] inmu = {{1, 2}, 
                           {2, 3}};
        double[][][] insigma = {
            {{2, 0}, {0, 0.5}}, 
            {{2, 0}, {0, 0.5}}
        };
        double[] p = {0.5, 0.5};
        
        Matrix[] mu = new Matrix[2];
        mu[0] = new Matrix(inmu[0], 2);
        mu[1] = new Matrix(inmu[1], 2);
        Matrix[] sigma = new Matrix[2];
        sigma[0] = new Matrix(insigma[0], 2, 2);
        sigma[1] = new Matrix(insigma[1], 2, 2);
        
        return new GMM(mu, sigma, p);
    }
}