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
    private static ArrayList<Integer[]> neuronsConfig = new ArrayList<>();
    //FALTAN RAMP STEP TRAPEZOID SGN LOG 
    //Linear baja pero se queda en 0.003 COMO MUCHO
    //Sigmoid sube por lo general

    public static void main(String[] args) {
        Problem problem = new Problem("ProjectData/O-data.txt");
        problem.saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);
        Solution sol = new Solution(problem);
        sol.solve();

        fullSearch(problem);
        //testSearch(problem);

//        createCombinations(3, possible3Neurons, new ArrayList<Integer>());
//        for (Integer[] array : neuronsConfig) {
//            System.out.println(Arrays.toString(array));
//        }
        /*
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
         */
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

    private static void fullSearch(Problem problem) {
        boolean showTrainGraph = false;
        boolean showGraphs = false;
        NeurophModule learningModule = new NeurophModule(15000, NeurophModule.RPROP, showTrainGraph, showGraphs, TRAINPATH, TESTPATH, problem.getNormalizer());
//        learningModule.onePlot(5, 0.3, TransferFunctionType.GAUSSIAN, 6, 0, NeurophModule.Rprop);
        ArrayList<Double> lrates = new ArrayList<>(Arrays.asList(0.2, 0.3, 0.4));
        boolean blockWindow = false;
        boolean appendTable = true;

        Integer[] possibleNeurons = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        Integer[] possible3Neurons = {6, 8, 10, 12};
        int i;
        for (i = 1; i <= 3; i++) {
            neuronsConfig.clear();
            
            Integer[] usedNeurons = possibleNeurons;
            if (i == 3) {
                usedNeurons = possible3Neurons;
            }
            createCombinations(i, usedNeurons, new ArrayList<Integer>());
            int[] combination = new int[i + 2];
            combination[0] = 14;
            combination[i + 2 - 1] = 1;
            for (Integer[] comb : neuronsConfig) {
                for (int j = 1; j < i + 2 - 1; j++) {
                    combination[j] = comb[j - 1];
                }
                for (TransferFunctionType type : TYPES) {
                        learningModule.onePlot(lrates, type, combination, blockWindow);
                        learningModule.writeTable(NeurophModule.TEST, "AllTest.csv", appendTable);
                    }
                System.out.println("Finished " + Arrays.toString(combination));

            }

        }
    }

    private static void testSearch(Problem problem) {
        boolean showTrainGraph = false;
        boolean showGraphs = false;
        NeurophModule learningModule = new NeurophModule(15000, NeurophModule.RPROP, showTrainGraph, showGraphs, TRAINPATH, TESTPATH, problem.getNormalizer());
//        learningModule.onePlot(5, 0.3, TransferFunctionType.GAUSSIAN, 6, 0, NeurophModule.Rprop);
        ArrayList<Double> lrates = new ArrayList<>(Arrays.asList(0.2, 0.3, 0.4));
        boolean blockWindow = false;
        boolean appendTable = true;
        int[] combination = {14, 6, 13, 1};
        learningModule.onePlot(20, 0.4, TransferFunctionType.TANH, combination, blockWindow);
        learningModule.writeTable(NeurophModule.TEST, "FullGA.csv", appendTable);

    }

    private static void createCombinations(int maxLength, Integer[] possibleNeurons, ArrayList<Integer> curr) {

        // If the current string has reached it's maximum length
        if (curr.size() == maxLength) {
            Integer[] newConfig = new Integer[maxLength];
            for (int i = 0; i < curr.size(); i++) {
                newConfig[i] = curr.get(i);
            }
            neuronsConfig.add(newConfig);

            // Else add each letter from the alphabet to new strings and process these new strings again
        } else {
            for (int i = 0; i < possibleNeurons.length; i++) {
                ArrayList<Integer> oldCurr = (ArrayList<Integer>) curr.clone();
                curr.add(possibleNeurons[i]);
                createCombinations(maxLength, possibleNeurons, curr);
                curr = oldCurr;
            }
        }
    }
}
