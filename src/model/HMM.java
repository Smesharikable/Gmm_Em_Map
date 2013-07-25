package model;

import Jama.Matrix;

/**
 *
 * @author Snezhana
 */
public class HMM {

    public int N;
    public Matrix A;
    public Matrix B;
    public double[] pi;
    public Matrix[] data;
    public GMM[] gmms;
    public int D;

    /**
     * @param N - count of states
     * @param A - matrix of transitions
     * @param B - matrix of emission probability
     * @param pi - probability vector of initial states
     */
       public HMM(GMM[] gmms, Matrix[] data) {
        this.gmms = gmms;
        this.N = gmms.length;
        this.data = data;
        initA();
        initB();
        initPi();
    }
       
       public HMM(Matrix[] data){
           this.data = data;           
       }

    public final void initA() {
        double[][] A = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j || (j - i) == 1) {
                    A[i][j] = 0.5;
                }
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.println(A[i][j]);
            }
            System.out.println("\n");
        }
        this.A = new Matrix(A);
    }

    public void initB() {
        double[][] B = new double[N][N];

        for (int i = 1; i < N; i++) {
            B[i][i] = gmms[1].prior(data[i]);
            //B[i][i] = B_emis(i, gmms[i], data[i]);
        }
    }
   
    public final void initPi() {
        pi = new double[N];
        pi[0] = 1;
    }

    public Matrix getA() { return A; }

    public Matrix getB() { return B; }

    public double[] getPi() { return pi; }
}



/* public double B_emis(int numComp, GMM gmm, Matrix data) {
        double emis_pr = 0;
        int countFtrComp = gmms.length;
        int countGauss = gmm.getNComponents();

        double[] vecEmis = new double[countFtrComp];
        double[] currEmis = new double[countGauss];
        Matrix currData = data;
        double[] mP = gmm.getP();
        Matrix[] mMu = gmm.getMu();
        Matrix[] mSigma = gmm.getSigma();

        for (int i = 1; i < countFtrComp; i++) {
            for (int j = 1; j < countGauss; j++) {
                currEmis[j] = mP[j] * mvnpdf(getVector(currData, i), mMu[j], mSigma[j]);
            }
            for (int k = 1; k < countGauss; k++) {
                vecEmis[i] = vecEmis[i] + currEmis[k];
            }
        }

        for (int i = 1; i < vecEmis.length; i++) {
            emis_pr = emis_pr + Math.log(vecEmis[i]);
        }
        return emis_pr;
    }

    public double mvnpdf(double[] data, Matrix mu, Matrix sigma) {
        double value = 0;

        value = 1 /(Math.pow(abs(sigma), 0.5)).times((Math.pow(2 * Math.PI, (D/2))));


        return value;
    }

    public double[] getVector(Matrix data, int n) {
        double[] res = new double[D];

        double[][] data2 = new double[gmms.length][gmms[1].getNDimensions()];

        for (int i = 0; i < gmms.length; i++) {
            for (int j = 0; j < gmms[1].getNDimensions(); j++) {
                data2[i][j] = data.get(i, j);
            }
        }
        System.arraycopy(data2[n], 0, res, 0, D);
        return res;
    }

    public Matrix abs(Matrix sigma) {
        double[][] dSigma = sigma.getArray();
        
        for (int i = 0; i < dSigma.length; i++) {
            for (int j = 0; j < dSigma.length; j++) {
                if (dSigma[i][j] < 0) {
                    dSigma[i][j] *= -1 ;
                }
            }
        }
        return new Matrix(dSigma);
    }
*/
