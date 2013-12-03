package algorithms;

import Jama.Matrix;
import model.GMM;

/**
 *
 * @author I&V
 * 
 * To use this algorithms, covariance matrix must be diagonal.
 */
public class EM {
    private GMM mInModel;
    private int mComponentCount = 32;
    private double mLikelihoodDelta = -0.01;
    private double mSigmaEpsilon = 0.01;
    private int mMaxIterations = 100;
    private boolean mIsChanges = true;
    
    private double[][] mPosterior;
    private double[] mPosteriorSum;
    private double[] mDensity;
    private double mLogLikelihood;
    
    public EM() {}
    
    public EM(int componentCount) {
        mComponentCount= componentCount;
    };
    
    public EM(GMM model) {
        mInModel = model;
    }
    
    public EM(GMM model, double LikelyhoodDelta) {
        mInModel = model;
        mLikelihoodDelta = LikelyhoodDelta;
    }
    
    public EM(GMM model, double LikelyhoodDelta, double SigmaEpsilon) {
        mInModel = model;
        mLikelihoodDelta = LikelyhoodDelta;
        mSigmaEpsilon = SigmaEpsilon;
    }
    
    public EM(GMM model, double LikelyhoodDelta, double SigmaEpsilon, int MaxIterations) {
        mInModel = model;
        mLikelihoodDelta = LikelyhoodDelta;
        mSigmaEpsilon = SigmaEpsilon;
        mMaxIterations = MaxIterations;
    }
    
    /**
     * Get the value of mIsChanges
     *
     * @return the value of mIsChanges
     */
    public boolean isIsChanges() {
        return mIsChanges;
    }

    /**
     * Set the value of mIsChanges
     *
     * @param IsChanges new value of mIsChanges
     */
    public void setIsChanges(boolean IsChanges) {
        mIsChanges = IsChanges;
    }
    
    public GMM doEM(Matrix input) {
        if (mInModel == null) mInModel = GMM.generate(mComponentCount, input);
        //Matrix tranInput = input.inverse();
        int inputSize = input.getRowDimension();
        double newLikelihood = - Double.MAX_VALUE;
        double oldLikelihood;
        int iterations = 0;
        
        GMM outModel;
        if (mIsChanges) {
            outModel = mInModel;
        } else {
            outModel = mInModel.copy();
        }
        int count = outModel.getNComponents();
        int dimension = outModel.getNDimensions();
        //Matrix[] posterior = new Matrix[count];
        Matrix[] newMu = outModel.getMu();
        Matrix[] newSigma = outModel.getSigma();
        double[] newP = outModel.getP();
        mDensity = new double[inputSize];
        mPosterior = new double[inputSize][count];
        mPosteriorSum = new double[mComponentCount];
        double[][] in = input.getArray();
        double[][] Mu;
        double[][] Sigma;
        
        do {
            // Estimation step
            // computing posterior and log-likelihood
            oldLikelihood = newLikelihood;
            estimation(input, newMu, newSigma, newP);
            newLikelihood = mLogLikelihood;
            
            if (oldLikelihood + newLikelihood > mLikelihoodDelta) break;
            
            // Maximization step
            for (int i = 0; i < count; i++) {
                // p_i
                newP[i] = mPosteriorSum[i];
                // compute Mu and Sigma
                Mu = newMu[i].getArray();
                Sigma = newSigma[i].getArray();
                for (int j = 0; j < dimension; j++) {
                    for (int k = 0; k < inputSize; k++) {
                        Mu[j][0] += mPosterior[k][i] * in[k][j];
                        Sigma[j][j] += Mu[j][0] * in[k][j];
                    }
                    Mu[j][0] /= newP[i];
                }
                // normalize
                //newMu[i].timesEquals(1 / newP[i]);
                for (int j = 0; j < dimension; j++) {
                    Sigma[j][j] = Sigma[j][j] / newP[i] - Mu[j][0] * Mu[j][0];
                    if (Sigma[j][j] < mSigmaEpsilon) Sigma[j][j] = mSigmaEpsilon;
                }
                newP[i] /= inputSize;
            }
            iterations ++;
        } while (iterations < mMaxIterations);
        
        return outModel;
    }
    
    /*
     * mPosterior[j][i] - posterior probability of vector j belonging to cluster i
     */
    private void estimation(Matrix input, Matrix[] mu, Matrix[] sigma, double[] p) {
        // compute loglikelihood of each component
        int dimension = mInModel.getNDimensions();
        // TODO these arrays will be enormous
        double[] maxLL = new double[input.getRowDimension()];
        double[][] vector;
        double sigmaDet = 1.0;
        double mahalanSqr = 0;
        
        for (int i = 0; i < maxLL.length; i++) {
            maxLL[i] = Double.NEGATIVE_INFINITY;
        }
        
        vector = input.getArray();
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < dimension; j++) {
                sigmaDet *= sigma[i].get(j, j);
            }
            for (int j = 0; j < input.getRowDimension(); j++) {
                for (int k = 0; k < dimension; k++) {
                    mahalanSqr += Math.pow(vector[j][k] - mu[i].get(k, 0), 2) / sigma[i].get(k, k);
                }
                // log prior probability
                mPosterior[j][i] = (dimension * Math.log(2 * Math.PI) 
                        - Math.log(sigmaDet) - mahalanSqr) / 2 + Math.log(p[i]);
                //if (mPosterior[j][i] > 0) System.out.println("Problem");
                if (mPosterior[j][i] > maxLL[j]) {
                    maxLL[j] = mPosterior[j][i];
                }
                mahalanSqr = 0;
            }
            sigmaDet = 1.0;
            mPosteriorSum[i] = 0;
        }
        
        mLogLikelihood = 0;
        for (int i = 0; i < input.getRowDimension(); i++) {
            mDensity[i] = 0;
            for (int j = 0; j < p.length; j++) {
                mPosterior[i][j] = Math.exp(mPosterior[i][j] - maxLL[i]);
                mDensity[i] += mPosterior[i][j];
            }
            mLogLikelihood += Math.log(mDensity[i]) + maxLL[i];
            // finishing compute posterior probability
            for (int j = 0; j < p.length; j++) {
                mPosterior[i][j] /= mDensity[i];
                mPosteriorSum[j] += mPosterior[i][j];
            }
        }
    }
    
}
