package energytfg;

import java.util.ArrayList;
import java.util.Arrays;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Cesar
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    private static final ArrayList<TransferFunctionType> ALLTYPES = new ArrayList<>(Arrays.asList(
//            TransferFunctionType.SIN
//            , 
//            TransferFunctionType.TANH
//            , 
            TransferFunctionType.GAUSSIAN
    ));
    //FALTAN RAMP STEP TRAPEZOID SGN LOG 
    //Linear baja pero se queda en 0.003 COMO MUCHO
    //Sigmoid sube por lo general
    private static final String FULLPATH = "ProjectData/N-fulldataset.csv";
    private static final String TRAINPATH = "ProjectData/N-train.csv";
    private static final String TESTPATH = "ProjectData/N-test.csv";

    public static void main(String[] args) {
        Problem problem = new Problem("ProjectData/O-data.txt");
        problem.saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);
        Solution sol = new Solution(problem);
        sol.solve();

//        NeurophModule learningModule = new NeurophModule(TransferFunctionType.LINEAR, TRAINPATH, TESTPATH, problem.getNormalizer());
        NeurophModule learningModule = new NeurophModule(ALLTYPES, TRAINPATH, TESTPATH, problem.getNormalizer());
        learningModule.testing();
//        learningModule.testBackprop();

    }

}
