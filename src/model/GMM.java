package model;

import Jama.Matrix;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I&V
 */
public class GMM {
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
            initialize();
        } catch (IOException ex) {
            Logger.getLogger(GMM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GMM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Matrix[] getMu() { return mMu; }
    
    public Matrix[] getSigma() { return mSigma; }
    
    public double[] getP() { return mP; }
    
    public int getNComponents() { return mCount; }
    
    public int getNDimensions() { return mDimension; }
    
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
    
    public static double[] normilize(double[] weights) {
        double sum = 0;
        for (double i : weights) sum += i;
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= sum;
        }
        return weights;
    }
    
    private void initialize() {
        mCount = mP.length;
        mDimension = mMu[0].getRowDimension();
        mMultiplier = new double[mCount];
        mInvSigmas = new Matrix[mCount];
        for (int i = 0; i < mCount; i ++) {
            //double temp = 1 / (Math.pow(2 * Math.PI, mDimension / 2.0) * Math.sqrt(mSigma[i].det()));
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
