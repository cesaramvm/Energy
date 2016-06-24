package energytfg;

import java.util.ArrayList;
import java.util.Arrays;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Cesar
 */
public class Main {

    private static final String FULLPATH = "ProjectData/N-fulldataset.csv";
    private static final String TRAINPATH = "ProjectData/N-train.csv";
    private static final String TESTPATH = "ProjectData/N-test.csv";
    private static final ArrayList<TransferFunctionType> TYPES = new ArrayList<>(Arrays.asList(
            //        TransferFunctionType.SIN
            //        , 
            //        TransferFunctionType.TANH
            //        , 
            //        TransferFunctionType.GAUSSIAN
            TransferFunctionType.GAUSSIAN
    ));
    //FALTAN RAMP STEP TRAPEZOID SGN LOG 
    //Linear baja pero se queda en 0.003 COMO MUCHO
    //Sigmoid sube por lo general

    public static void main(String[] args) {
        Problem problem = new Problem("ProjectData/O-data.txt");
        problem.saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);
        Solution sol = new Solution(problem);
        sol.solve();

//        NeurophModule learningModule = new NeurophModule(TransferFunctionType.LINEAR, TRAINPATH, TESTPATH, problem.getNormalizer());
        NeurophModule learningModule = new NeurophModule(50000, TRAINPATH, TESTPATH, problem.getNormalizer());
//        learningModule.onePlot(5, 0.3, TransferFunctionType.GAUSSIAN, 6, 0, NeurophModule.Rprop);
        ArrayList<Double> lrates = new ArrayList<>(Arrays.asList(0.2,0.3,0.4));
        learningModule.onePlot(1, 0.3, TransferFunctionType.GAUSSIAN, 6, 0, NeurophModule.Rprop);

    }

}
