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
    private double mLikelyhoodDelta = -0.01;
    private double mSigmaEpsilon = 0.01;
    private int mMaxIterations = 100;
    private boolean mIsChanges = true;
    
    public EM() {}
    
    public EM(int componentCount) {
        mComponentCount= componentCount;
    };
    
    public EM(GMM model) {
        mInModel = model;
    }
    
    public EM(GMM model, double LikelyhoodDelta) {
        mInModel = model;
        mLikelyhoodDelta = LikelyhoodDelta;
    }
    
    public EM(GMM model, double LikelyhoodDelta, double SigmaEpsilon) {
        mInModel = model;
        mLikelyhoodDelta = LikelyhoodDelta;
        mSigmaEpsilon = SigmaEpsilon;
    }
    
    public EM(GMM model, double LikelyhoodDelta, double SigmaEpsilon, int MaxIterations) {
        mInModel = model;
        mLikelyhoodDelta = LikelyhoodDelta;
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
        Matrix tranInput = input.inverse();
        int inputSize = input.getColumnDimension();
        double newLikelyhood = - Double.MAX_VALUE;
        double oldLikelyhood;
        int iterations = 0;
        
        
        GMM outModel;
        if (mIsChanges) {
            outModel = mInModel;
        } else {
            outModel = mInModel.copy();
        }
        int count = outModel.getNComponents();
        int dimension = outModel.getNDimensions();
        Matrix[] posterior;
        
        Matrix[] newMu = outModel.getMu();
        Matrix[] newSigma = outModel.getSigma();
        double[] newP = outModel.getP();
        
        do {
            oldLikelyhood = newLikelyhood;            
            posterior = outModel.posterior(input);
            
            for (int i = 0; i < count; i++) {
                // p_i
                newP[i] = GMM.posteriorSum(posterior[i]);
                newMu[i] = posterior[i].times(tranInput).times(1 / newP[i]);
                for (int j = 0; j < inputSize; j++) {
                    newSigma[i].plusEquals(
                            // x^2
                            input.getMatrix(0, dimension - 1, i, i).times(
                            tranInput.getMatrix(i, i, 0, dimension - 1)
                            // p_i
                            ).times(posterior[i].get(0, i))
                    );
                }
                newSigma[i].timesEquals(newP[i]).minusEquals(
                        // mu^2
                        newMu[i].times(newMu[i].transpose())
                );
                sigmaCorrection(newSigma[i]);
                newP[i] /= inputSize;
            }
            
            newLikelyhood = outModel.getLogLikelyhood(input);
            iterations ++;
        } while (oldLikelyhood - newLikelyhood < mLikelyhoodDelta && iterations < mMaxIterations);
        
        if (mIsChanges) {
            return outModel.copy();
        }
        return outModel;
    }
    
    private void sigmaCorrection(Matrix sigma) {
        for (int i = 0; i < sigma.getColumnDimension(); i++) {
            if (sigma.get(i, i) < mSigmaEpsilon) {
                sigma.set(i, i, mSigmaEpsilon);
            }
        }
    }
    
}
