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

    public Viterbi(GMM[] gmms, Matrix[] passw) {
        this.gmms = gmms;
        hmm = new HMM(gmms, passw);
        algorithmViterbi();
    }
    
    public Viterbi(Matrix[] passw){
        hmm = new HMM(gmms, passw);
        algorithmViterbi();
    }

    private void algorithmViterbi() {
        int T = gmms.length; 
        double[][] Pr = new double[T][T]; 
        int[][] TIndex = new int[T][T];
        double[] temp = new double[T];
        int[] pathViterbi = new int[T];

        for (int i = 0; i < T; i++) {
            Pr[i][0] = hmm.pi[i] * hmm.B.get(i, 0);
            TIndex[0][0] = 0;
        }

        for (int i = 0; i < T; i++) {
            for (int j = 0; j < T; j++) {
                for (int k = 0; k < T; k++) {
                    temp[k] = Pr[k][i - 1] + Math.log10(hmm.A.get(k, j));
                   // temp[k] = Pr[k][i - 1] * hmm.A.get(k, j);
                }
                Pr[j][i] = myMax(temp) + Math.log10(hmm.B.get(i, j)); // mb *
                TIndex[j][i] = argmax(temp);
            }
        }
        for (int k = 0; k < T; k++) {
            temp[k] = Pr[k][T-1];
        }

        pathViterbi[T - 1] = argmax(temp);

        for (int i = T - 2; i > 1; i--) {
            pathViterbi[i] = TIndex[pathViterbi[i + 1]][i + 1];
        }

        System.out.print("States: 1 ");
        for (int n = 0; n < T - 2; n++) {
            System.out.print(temp[n]);
        }
        System.out.print("\n");
        System.out.print("Probability: " + myMax(temp));
    }

    public double myMax(double[] M) {
        double Mmax = -1000000;
        for (int i = 0; i < M.length; i++) {
            if (M[i] > Mmax && M[i] != 0) {
                Mmax = M[i];
            }
        }
        if (Mmax == -1000000) {
            return 0;
        }
        return Mmax;
    }

    public int argmax(double[] M) {
        double Mmax = -1000000;
        int Imax = 0;
        for (int i = 0; i < M.length; i++) {
            if (M[i] > Mmax && M[i] != 0) {
                Mmax = M[i];
                Imax = i;
            }
        }
        return Imax;
    }
}
