package algorithms;

import Jama.Matrix;
import java.util.Arrays;
import model.GMM;

/**
 *
 * @author I&V
 */
public class Map {
    private GMM mInModel;
    private double mRelativeCoeff = 16;
    private boolean mIsChanges = true;
    
    public Map(GMM inModel) {
        mInModel = inModel;
    }
    
    public Map(GMM inModel, double relativeCoeff) {
        mInModel = inModel;
        mRelativeCoeff = relativeCoeff;
    }
    
    public void setIsChanges(boolean isChanges) { mIsChanges = isChanges; }
    
    public boolean getIsChanges() { return mIsChanges; }
    
    public GMM fitByMeans(Matrix input, int iterations) {
        Matrix[] oldMu = mInModel.getMu();
        Matrix[] oldSigma = mInModel.getSigma();
        double[] oldP = mInModel.getP();
        int count = mInModel.getNComponents();
        GMM OutModel = mInModel;
        Matrix trInput = input.transpose();
        Matrix[] newMu = new Matrix[count];
        double midWeight;
        double coeff;
        
        
        for (int t = 0; t < iterations; t ++) {
            Matrix[] posterior = OutModel.posterior(input);
            for (int i = 0; i < count; i ++) {
                midWeight = GMM.posteriorSum(posterior[i]);
                coeff = midWeight / (midWeight + mRelativeCoeff);
                // computing midMu
                newMu[i] = posterior[i].times(trInput).times(1 / midWeight).transpose();
                // computing exactly newMu
                newMu[i] = newMu[i].times(coeff).plus(
                        oldMu[i].times(1 - coeff)
                        );
                // update Mu for next iteration
                oldMu[i] = newMu[i].copy();
            }
            OutModel = new GMM(newMu, oldSigma, oldP);
        }
        
        if (mIsChanges) mInModel = OutModel;
        
        return OutModel;
    }
    
    public GMM[] fitByWeights(Matrix input, int iterations, int states) {
        GMM[] result = new GMM[states];
        int chunk = input.getColumnDimension() / states;
        int low = 0;
        
        for (int i = 0; i < states - 1 ; i++) {
            result[i] = fitByWeights(
                    input.getMatrix(0, input.getRowDimension() - 1, low, low + chunk - 1), 
                    iterations
                    );
            low += chunk;
        }
        
        result[states - 1] = fitByWeights(
                    input.getMatrix(0, input.getRowDimension() - 1, low, input.getColumnDimension() - 1), 
                    iterations
                    );
        
        return result;
    }
    
    private GMM fitByWeights(Matrix input, int iterations) {
        Matrix[] oldMu = mInModel.getMu();
        Matrix[] oldSigma = mInModel.getSigma();
        double[] P = mInModel.getP();
        int count = mInModel.getNComponents();
        GMM OutModel = mInModel;
        int vectorCount = input.getColumnDimension();
        double[] oldP = new double[count];
        double[] newP = new double[count];
        
        double midWeight;
        double coeff;
        System.arraycopy(P, 0, oldP, 0, count);
        
        for (int t = 0; t < iterations; t ++) {
            Matrix[] posterior = OutModel.posterior(input);
            for (int i = 0; i < count; i ++) {
                midWeight = GMM.posteriorSum(posterior[i]);
                coeff = midWeight / (midWeight + mRelativeCoeff);
                newP[i] = midWeight * coeff / vectorCount + oldP[i] * (1 - coeff);
            }
            newP = GMM.normilize(newP);
            oldP = Arrays.copyOf(oldP, count);
            OutModel = new GMM(oldMu, oldSigma, newP);
        }
        
        
        return OutModel;
    }  
    
}
