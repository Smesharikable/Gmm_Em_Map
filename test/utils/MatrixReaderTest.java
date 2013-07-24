/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Jama.Matrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AirVan
 */
public class MatrixReaderTest {
    
    public MatrixReaderTest() {
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
     * Test of setPath method, of class MatrixReader.
     */
    @Test
    public void testSetPath() {
        String path1 = "c:\\Voici\\input.txt";
        MatrixReader reader = new MatrixReader("c:\\Voici\\test.txt");
        reader.setPath(path1);
        MatrixReader reader2 = new MatrixReader(path1);
        assertEquals(reader.getMatrix().get(10, 10), reader2.getMatrix().get(10, 10), 0.001);
    }

    /**
     * Test of getMatrix method, of class MatrixReader.
     */
    @Test
    public void testGetMatrix() {
        String path1 = "c:\\Voici\\input.txt";
        MatrixReader reader = new MatrixReader(path1);
        assertEquals(reader.getMatrix().get(0, 0), -10.615, 0.001);
        assertEquals(reader.getMatrix().get(9, 1), 10.148, 0.001);
    }
}