package src;

import Jama.Matrix;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I&V
 */
public class GMM {
    private Matrix[] mMu;
    private Matrix[] mSigma;
    private BigDecimal[] mP;
    private double[] mMultiplier;
    private Matrix[] mInvSigmas;
    private int mCount;
    private int mDimension;
    private final int mMinDegree = -92;
    //private BigDecimal[] mFaultMult;
    
    /**
     * 
     * @param mu - 1 by d matrices
     * @param sigma - d by d covariations matrices
     * @param p  - array of proportions
     */
    public GMM(Matrix[] mu, Matrix[] sigma, BigDecimal[] p)
    {
        mMu = mu;
        mSigma = sigma;
        mP = p;
        initialize();
    }
    
    public GMM(Matrix[] mu, Matrix[] sigma, double[] p)
    {
        mMu = mu;
        mSigma = sigma;
        mP = new BigDecimal[p.length];
        for (int i = 0; i < p.length; i ++) {
            mP[i] = new BigDecimal(p[i]);
        }
        initialize();
    }
    
    public GMM(File fin) throws FileNotFoundException
    {
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fin)));
            mMu = (Matrix[]) ois.readObject();
            mSigma = (Matrix[]) ois.readObject();
            mP = (BigDecimal[]) ois.readObject();
            initialize();
        } catch (IOException ex) {
            Logger.getLogger(GMM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GMM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Matrix[] getMu() { return mMu; }
    
    public Matrix[] getSigma() { return mSigma; }
    
    public BigDecimal[] getP() { return mP; }
    
    public BigDecimal prior(Matrix vector)
    {
        BigDecimal result = new BigDecimal(0, MathContext.DECIMAL128);
        BigDecimal temp;
        for (int i = 0; i < mCount; i ++) {
            temp = componentDensity(i, vector).multiply(mP[i]);
            result = result.add(temp);
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
    public Matrix[] posterior(Matrix[] input)
    {
        Matrix[] result = new Matrix[mCount];
        for (int component = 0; component < mCount; component ++) {
            result[component] = new Matrix(1, input.length);
            for (int vector = 0; vector < input.length; vector ++) {
                // check indexes
                double value = mP[component].multiply(
                            componentDensity(component, input[vector])
                        ).divide(
                            prior(input[vector])
                        ).doubleValue();
                result[component].set(0, vector, value);
            }
        }
        return result;
    }
    
    private void initialize()
    {
        mCount = mP.length;
        mDimension = mMu[0].getRowDimension();
        mMultiplier = new double[mCount];
        mInvSigmas = new Matrix[mCount];
        for (int i = 0; i < mCount; i ++) {
            mMultiplier[i] = 1 / (Math.pow(2 * Math.PI, mDimension / 2.0) * Math.sqrt(mSigma[i].det()));
            mInvSigmas[i] = mSigma[i].inverse();
        }
    }
    
    private BigDecimal componentDensity(int i, Matrix vector)
    {
        Matrix secondMult = vector.minus(mMu[i]);
        Matrix firstMult = secondMult.transpose();
        double degree = -0.5 * firstMult.times(mInvSigmas[i]).times(secondMult).get(0, 0);
        
        if (degree < mMinDegree) return BigDecimal.ONE;
        
        int intDegree = (int) degree;
        BigDecimal bdResult = new BigDecimal(Math.E, MathContext.DECIMAL128);
        bdResult.pow(intDegree, MathContext.DECIMAL128);
        BigDecimal fault = new BigDecimal(Math.exp(intDegree - degree), MathContext.DECIMAL128);
        bdResult.multiply(fault);
        //double mult = Math.exp(-0.5 * degree);
        //double result = mMultiplier[i] * mult;
        
        return bdResult;
    }
    
}
