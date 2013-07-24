package algorithms;

import Jama.Matrix;
import model.GMM;

/**
 *
 * @author I&V
 */
public class EM {
    private GMM mInModel;
    private double mLikelyhoodDelta = -0.01;
    private double mSigmaEpsilon = 0.001;
    private int mMaxIterations = 100;
    
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
    
    public GMM doEM(Matrix input) {
        GMM outModel = mInModel;
        return outModel;
    }
    
    public double getLikelyhood() {
        double result = 0;
        
        return result;
    }
    
}
