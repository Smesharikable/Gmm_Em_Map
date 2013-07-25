package utils;

import Jama.Matrix;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * Class for Matrix Reading
 */
public class MatrixReader {
    
    public MatrixReader(String path) {
       this.filePath = path;
       this.countVectAmount();
       this.inMatrix = new Matrix(featVectAmount, featVectDimension);
    }
    
    /**
     * Divides data matrix into array of matrix 
     * @param data amount of feature vectors X dimension(39)
     * @return array of feature matrix
     */
    public static Matrix[] parseMatrix(Matrix data, int stateAmount) {
        int vectorAmount = data.getRowDimension();
        int blockLength = vectorAmount / stateAmount;
        Matrix[] outMatrix = new Matrix[stateAmount];
        for (int i = 0; i < stateAmount; i++ ) {
            int[] arrayOfRows = new int[blockLength];
            for (int j = 0; j < blockLength; j++) {
                arrayOfRows[j] = i * blockLength + j;
            }
            outMatrix[i] = data.getMatrix(arrayOfRows, 0, 38);
        } 
        return outMatrix;
    }
    
    /**
     * Sets new path for data file
     * @param path Path for data file
     */
    public void setPath(String path) {
        this.filePath = path;
        this.countVectAmount();
        this.inMatrix = new Matrix(featVectAmount, featVectDimension);
    }
    
    /**
     * Gets feature Matrix
     * @return feature Matrix
     */
    public Matrix getMatrix() {
        try {
            this.buildMatrix();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return this.inMatrix;
    }
    
    /**
     * Counting amount of feature vectors in data file
     */
    private void countVectAmount() {
        featVectAmount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((reader.readLine()) != null) {
                featVectAmount++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }
    
    /**
     * Generates feature-matrix inMatrix, using data from filePath
     */
    private void buildMatrix() throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        for (int i = 0; i < featVectAmount; i++) {
            String line = reader.readLine();
            line = line.substring(0, line.length() - 2); // cutting off ";"
            String[] lines = line.split(" "); // making token array
            for (int j = 0; j < lines.length; j++) {
                double value = Double.parseDouble(lines[j]);
                this.inMatrix.set(i, j, value);
            }
        }
    }
    
    /**
     * Fixed Vector's dimension
     */
    private static final int featVectDimension = 39;
    /**
     * Matrix which will be filled
     */
    
    private Matrix inMatrix;
    
    /**
     * Data file path
     */
    private String filePath;
    
    /**
     * Amount of feature vectors in data file
     */
    private int featVectAmount;
}
