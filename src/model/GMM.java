package model;

import Jama.Matrix;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I&V
 */
public class GMM implements Serializable{
    private Matrix[] mMu;
    private Matrix[] mSigma;
    private double[] mP;
    private double[] mMultiplier;
    private Matrix[] mInvSigmas;
    private int mCount;
    private int mDimension;
    
    /**
     * 
     * @param mu - d by 1 matrices
     * @param sigma - d by d covariations matrices
     * @param p  - array of proportions
     */
    public GMM(Matrix[] mu, Matrix[] sigma, double[] p) {
        mMu = mu;
        mSigma = sigma;
        mP = p;
        initialize();
    }
    
    public GMM(File fin) throws FileNotFoundException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fin)));
            mMu = (Matrix[]) ois.readObject();
            mSigma = (Matrix[]) ois.readObject();
            mP = (double[]) ois.readObject();
            ois.close();
            initialize();
        } catch (IOException ex) {
            Logger.getLogger(GMM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GMM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public GMM copy() {
        return new GMM(this.getMuCopy(), this.getSigmaCopy(), this.getPCopy());
    }
    
    public Matrix[] getMu() { return mMu; }
    
    public Matrix[] getMuCopy() {
        Matrix[] copiedMu = new Matrix[mCount];
        for (int i = 0; i < mCount; i++) {
            copiedMu[i] = mMu[i].copy();
        }
        return copiedMu; 
    }
    
    public Matrix[] getSigma() { return mSigma; }
    
    public Matrix[] getSigmaCopy() { 
        Matrix[] copiedSigma = new Matrix[mCount];
        for (int i = 0; i < mCount; i++) {
            copiedSigma[i] = mSigma[i].copy();
        }
        return copiedSigma;
    }
    
    public double[] getP() { return mP; }
    
    public double[] getPCopy() { 
        return Arrays.copyOf(mP, mCount); 
    }
    
    public int getNComponents() { return mCount; }
    
    public int getNDimensions() { return mDimension; }
    
    public double getLogLikelyhood(Matrix input) {
        double result = 0;
        int m = input.getRowDimension();
        
        for (int i = 0; i < input.getColumnDimension(); i++) {
            result += Math.log(prior(input.getMatrix(0, m - 1, i, i)));
        }
        
        return result;
    }
    
    public double prior(Matrix vector) {
        double result = 0;
        double temp;
        for (int i = 0; i < mCount; i ++) {
            temp = componentDensity(i, vector);
            result += temp * mP[i];
        }
        return result;
    }
    
    /**
     * 
     * @param input - features vectors array,
     *      features represented by rows.
     * @return Matrix of posterior probabilities,
     *      which cosist of mCount colums.
     */
    public Matrix[] posterior(Matrix input) {
        Matrix[] result = new Matrix[mCount];
        Matrix temp;
        double value;
        for (int component = 0; component < mCount; component ++) {
            result[component] = new Matrix(1, input.getColumnDimension());
            for (int vector = 0; vector < input.getColumnDimension(); vector ++) {
                temp = input.getMatrix(0, input.getRowDimension() - 1, vector, vector);
                value = mP[component] * componentDensity(component, temp) / prior(temp);
                result[component].set(0, vector, value);
            }
        }
        return result;
    }
    
    public static double posteriorSum(Matrix postVector) {
        double result = 0;
        double[][] value = postVector.getArray();
        for (int i = 0; i < postVector.getColumnDimension(); i ++) {
            result += value[0][i];
        }
        return result;
    }
    
    public static double[] normilize(double[] weights) {
        double sum = 0;
        for (double i : weights) sum += i;
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= sum;
        }
        return weights;
    }
    
    /**
     *
     * @param componentsCount - count of gaussian componanets in GMM
     * @param input - matrix D by N, where D - dimension, N - number of vectors
     * @return GMM generated occording to input appropriate for EM algorithm
     */
    public static GMM generate(int componentsCount, Matrix input) {
        double[] weigths = new double[componentsCount];
        Matrix[] mu = new Matrix[componentsCount];
        Matrix[] sigma = new Matrix[componentsCount];
        Random rd = new Random();
        int index;
        int inputSize = input.getColumnDimension();
        int dimension = input.getRowDimension();
        
        Arrays.fill(weigths, 1 / componentsCount);
        for (int i = 0; i < componentsCount; i++) {
            index = rd.nextInt(inputSize);
            mu[i] = input.getMatrix(0, dimension - 1, index, index);
        }
        // computing Ex^2
        double[] Ex_2 = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < inputSize; j++) {
                Ex_2[i] += input.get(i, j);
            }
            Ex_2[i] /= inputSize;
        }
        // computins sigmas
        for (int i = 0; i < componentsCount; i++) {
            sigma[i] = new Matrix(dimension, dimension);
            for (int j = 0; j < dimension; j++) {
                sigma[i].set(j, j, Ex_2[j] - mu[i].get(j, 0));
            }
        }
        
        GMM result = new GMM(mu, sigma, weigths);
        return result;
    }
    
    private void initialize() {
        mCount = mP.length;
        mDimension = mMu[0].getRowDimension();
        mMultiplier = new double[mCount];
        mInvSigmas = new Matrix[mCount];
        for (int i = 0; i < mCount; i ++) {
            mMultiplier[i] = 1 / (Math.pow(2 * Math.PI, mDimension / 2.0) * Math.sqrt(mSigma[i].det()));
            mInvSigmas[i] = mSigma[i].inverse();
        }
    }
    
    private double componentDensity(int i, Matrix vector) {
        Matrix secondMult = vector.minus(mMu[i]);
        Matrix firstMult = secondMult.transpose();
        double degree = -0.5 * firstMult.times(mInvSigmas[i]).times(secondMult).get(0, 0);
        return Math.exp(degree)  * mMultiplier[i];
    }
    
}