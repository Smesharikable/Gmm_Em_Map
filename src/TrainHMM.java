
import Jama.Matrix;
import algorithms.Map;
import algorithms.Viterbi;
import com.sun.corba.se.impl.io.IIOPInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.GMM;
import utils.MatrixReader;


/**
 *
 * @author I&V
 */
public class TrainHMM {

    /**
     * @param args the command line arguments
     * args[0] - path to UBM file
     * args[1] - features file
     * args[2] - count of states
     * args[3] - count of iterations
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            FileInputStream fis = new FileInputStream(args[0]);
            ObjectInputStream ois = new ObjectInputStream(fis);
            GMM ubm = (GMM) ois.readObject();
            ois.close();
            fis.close();
            
            fis = new FileInputStream(args[1]);
            ois = new ObjectInputStream(fis);
            Matrix input = (Matrix) ois.readObject();
            fis.close();
            ois.close();
            
            int statesCount = Integer.parseInt(args[2]);
            int iterationsCount = Integer.parseInt(args[3]);
            Matrix[] stateInput = MatrixReader.parseMatrix(input, statesCount);
            Map map = new Map(ubm);
            map.fitByMeans(input, iterationsCount);
            GMM[] stateGMMs = new GMM[statesCount];
            for (int i = 0; i < statesCount; i++) {
                stateGMMs[i] = map.fitByWeights(stateInput[i], iterationsCount);
            }
            
            Viterbi viterbi = new Viterbi(stateGMMs, input);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TrainHMM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TrainHMM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TrainHMM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
