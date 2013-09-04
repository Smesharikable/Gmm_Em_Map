
import Jama.Matrix;
import algorithms.EM;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.GMM;
import utils.MatrixReader;

/**
 *
 * @author I&V
 */
public class CreateUBM {

    /**
     * @param args the command line arguments
     * args[0] - path to UBM CreateUBM data
     * args[1] - number of gaussian
     * args[2] - output file name
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MatrixReader mr = new MatrixReader(args[0]);
        Matrix input = mr.getMatrix();
        EM em = new EM(Integer.parseInt(args[1]));
        GMM ubm = em.doEM(input.transpose());
        try {
            FileOutputStream fos = new FileOutputStream(args[2]);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ubm);
            oos.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreateUBM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CreateUBM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
