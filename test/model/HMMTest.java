package model;

import Jama.Matrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

/**
 *
 * @author Snezhana
 */
public class HMMTest {
    
    public HMMTest() {
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

    /*
   @Test
    public void testHMMInitA() {
        HMM instance = new HMM(3);
        double[][] expResult = {{0.5,0.5,0},{0,0.5,0.5},{0,0,0.5}};
        Matrix result = instance.getA();
        Arrays.equals(expResult, result.getArray());
    }
    
    @Test
    public void testHMMInitPi() {
        HMM instance = new HMM(3);
        double[] expResult = {1,0,0};
        double[] result = instance.getPi();
        Arrays.equals(expResult, result);
    }
*/
}