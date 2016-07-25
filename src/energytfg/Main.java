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
            TransferFunctionType.SIN,
            TransferFunctionType.TANH,
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

        boolean showTrainGraph = false;
        NeurophModule learningModule = new NeurophModule(15000, NeurophModule.RPROP, showTrainGraph, TRAINPATH, TESTPATH, problem.getNormalizer());
//        learningModule.onePlot(5, 0.3, TransferFunctionType.GAUSSIAN, 6, 0, NeurophModule.Rprop);
        ArrayList<Double> lrates = new ArrayList<>(Arrays.asList(0.2, 0.3, 0.4));
        boolean blockWindow = false;
        boolean appendTable = true;
        int[] layersNeurons = new int[4];
        layersNeurons[0] = 14;
        layersNeurons[3] = 1;
        int[] possibleNeurons = {4, 6};
        for (int i : possibleNeurons) {
            for (int j : possibleNeurons) {
                layersNeurons[1] = i;
                layersNeurons[2] = j;
                System.out.println(Arrays.toString(layersNeurons));
                learningModule.onePlot(lrates, TransferFunctionType.GAUSSIAN, layersNeurons, blockWindow);
                learningModule.writeTable(NeurophModule.TEST, "test.csv", appendTable);
            }
        }
        //BY LEARNING RATE
        /*for (int i=5; i<14; i++){
            for (Double d: lrates){
                learningModule.onePlot(d, TYPES, i, 0, blockWindow);
            }
        }
        for (int i = 5; i < 10; i++) {
            for (int j = 3; j < 5; j++) {
                for (Double d : lrates) {
                    learningModule.onePlot(d, TYPES, i, j, blockWindow);
                }
            }

        }*/

        //BY TRANSFER FUCTION
        /*for (int i = 5; i < 14; i++) {
            for (TransferFunctionType t : TYPES) {
                learningModule.onePlot(lrates, t, i, 0, blockWindow);
            }
        }

        /*for (int i = 6; i < 10; i++) {
            for (int j = 3; j < 8; j++) {
                for (TransferFunctionType t : TYPES) {
                    learningModule.onePlot(lrates, t, i, j, blockWindow);
                }
            }

        }*/
//        learningModule.onePlot(1, 0.3, TransferFunctionType.GAUSSIAN, 6, 3, NeurophModule.RPROP, blockWindow);
        //        learningModule.createTrainingTable(TRAININGTABLEPATH);
        //        learningModule.createTestTable(TESTTABLEPATH);
        //        learningModule.appendTestTable();
        //        boolean append = false;
        //        learningModule.writeTable(NeurophModule.TRAINING, append);        
//        learningModule.onePlot(10, 0.3, TransferFunctionType.GAUSSIAN, 6, 3, NeurophModule.RPROP, blockWindow);
//        learningModule.onePlot(10, 0.2, TransferFunctionType.GAUSSIAN, 10, 0, NeurophModule.RPROP, blockWindow);
//        learningModule.onePlot(10, 0.3, TransferFunctionType.GAUSSIAN, 10, 0, NeurophModule.RPROP, blockWindow);
//        learningModule.onePlot(10, 0.4, TransferFunctionType.GAUSSIAN, 10, 0, NeurophModule.RPROP, blockWindow);
//        learningModule.onePlot(10, 0.2, TransferFunctionType.GAUSSIAN, 14, 0, NeurophModule.RPROP, blockWindow);
//        learningModule.onePlot(10, 0.3, TransferFunctionType.GAUSSIAN, 14, 0, NeurophModule.RPROP, blockWindow);
    }

}
