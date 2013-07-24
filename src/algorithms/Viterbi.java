package algorithms;

import model.GMM;
import model.HMM;
import Jama.Matrix;

/**
 *
 * @author Snezhana
 */
public class Viterbi {

    public GMM[] gmms;
    public HMM hmm;
    public Matrix[] data;

    /**
     * @param gmms - list of gmm
     */
    public Viterbi(GMM[] gmms, Matrix[] passw) {
        this.gmms = gmms;
        hmm = new HMM(gmms, passw);
    }

  
}
