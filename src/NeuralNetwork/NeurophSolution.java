/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import Models.Problem;
import Util.Normalizers.Normalizer;
import energytfg.Main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.abs;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Usuario
 */
public class NeurophSolution {

    private static final ArrayList<Integer[]> neuronsConfig = new ArrayList<>();
    private static final ArrayList<TransferFunctionType> TYPES = new ArrayList<>(Arrays.asList(
            TransferFunctionType.SIN,
            TransferFunctionType.TANH,
            TransferFunctionType.GAUSSIAN
    ));
    private final String fullPath;
    private final String trainPath;
    private final String testPath;

    public NeurophSolution(String FULLPATH, String TRAINPATH, String TESTPATH) {
        this.fullPath = FULLPATH;
        this.trainPath = TRAINPATH;
        this.testPath = TESTPATH;
    }

    public void fullSearch(Problem problem) {
        boolean showTrainGraph = false;
        boolean showGraphs = true;
        NeurophSearch learningModule = new NeurophSearch(15000, NeurophSearch.RPROP, showTrainGraph, showGraphs, trainPath, testPath, problem.getNormalizer());
//        learningModule.onePlot(5, 0.3, TransferFunctionType.GAUSSIAN, 6, 0, NeurophModule.Rprop);
        ArrayList<Double> lrates = new ArrayList<>(Arrays.asList(0.2, 0.3, 0.4));
        boolean blockWindow = false;
        boolean appendTable = true;

        Integer[] possibleNeurons = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        Integer[] possible3Neurons = {6, 7, 8, 9, 10, 11, 12, 13, 14};
        int i;
        for (i = 1; i <= 3; i++) {
            neuronsConfig.clear();

            Integer[] usedNeurons = possibleNeurons;
            if (i == 3) {
                usedNeurons = possible3Neurons;
            }
            createCombinations(i, usedNeurons, new ArrayList<>());
            int[] combination = new int[i + 2];
            combination[0] = 14;
            combination[i + 2 - 1] = 1;
            for (Integer[] comb : neuronsConfig) {
                for (int j = 1; j < i + 2 - 1; j++) {
                    combination[j] = comb[j - 1];
                }
                if (combination[1] == 14) {
                    for (TransferFunctionType type : TYPES) {
                        learningModule.onePlot(lrates, type, combination);
                        learningModule.writeTable(NeurophSearch.TEST, "AllTest.csv", appendTable);
                    }
                    System.out.println("Finished " + Arrays.toString(combination));

                }

            }

        }
    }

    public void testSearch(Problem problem) {
        boolean showTrainGraph = false;
        boolean showGraphs = false;
        NeurophSearch learningModule = new NeurophSearch(15000, NeurophSearch.RPROP, showTrainGraph, showGraphs, trainPath, testPath, problem.getNormalizer());
//        learningModule.onePlot(5, 0.3, TransferFunctionType.GAUSSIAN, 6, 0, NeurophModule.Rprop);
        boolean blockWindow = false;
        boolean appendTable = true;
        int[] combination = {14, 7, 7, 1};
        learningModule.onePlot(20, 0.3, TransferFunctionType.TANH, combination);
        learningModule.writeTable(NeurophSearch.TEST, "FullTA77.csv", appendTable);

    }

    public void lastMain() {
//        Problem problem = new Problem("ProjectData/O-data.txt");
//        problem.saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);
//        HeuristicModule sol = new HeuristicModule(problem);
//        sol.solve();
//        boolean showTrainGraph = false;
//        boolean showGraphs = false;
//        NeurophModule learningModule = new NeurophModule(15000, NeurophModule.RPROP, showTrainGraph, showGraphs, TRAINPATH, TESTPATH, problem.getNormalizer());
////        learningModule.onePlot(5, 0.3, TransferFunctionType.GAUSSIAN, 6, 0, NeurophModule.Rprop);
//        ArrayList<Double> lrates = new ArrayList<>(Arrays.asList(0.2, 0.3, 0.4));
//        boolean blockWindow = false;
//        boolean appendTable = true;
//        Integer[] possible3Neurons = {6, 8, 10, 12};
//        createCombinations(3, possible3Neurons, new ArrayList<Integer>());
//        for (Integer[] array : neuronsConfig) {
//            System.out.println(Arrays.toString(array));
//        }
//
//        int[] layersNeurons = new int[4];
//        layersNeurons[0] = 14;
//        layersNeurons[3] = 1;
//        int[] possibleNeurons = {4, 6};
//        for (int i : possibleNeurons) {
//            for (int j : possibleNeurons) {
//                layersNeurons[1] = i;
//                layersNeurons[2] = j;
//                System.out.println(Arrays.toString(layersNeurons));
//                learningModule.onePlot(lrates, TransferFunctionType.GAUSSIAN, layersNeurons, blockWindow);
//                learningModule.writeTable(NeurophModule.TEST, "test.csv", appendTable);
//            }
//        }
//
//        //BY LEARNING RATE
//        for (int i = 5; i < 14; i++) {
//            for (Double d : lrates) {
//                learningModule.onePlot(d, TYPES, i, 0, blockWindow);
//            }
//        }
//        for (int i = 5; i < 10; i++) {
//            for (int j = 3; j < 5; j++) {
//                for (Double d : lrates) {
//                    learningModule.onePlot(d, TYPES, i, j, blockWindow);
//                }
//            }
//
//        }
//        //BY TRANSFER FUCTION
//        for (int i = 5; i < 14; i++) {
//            for (TransferFunctionType t : TYPES) {
//                learningModule.onePlot(lrates, t, i, 0, blockWindow);
//            }
//        }
//
//        for (int i = 6; i < 10; i++) {
//            for (int j = 3; j < 8; j++) {
//                for (TransferFunctionType t : TYPES) {
//                    learningModule.onePlot(lrates, t, i, j, blockWindow);
//                }
//            }
//
//        }
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

    public void findBestNetwork1(Problem problem) {
        NeurophSearch learningModule = new NeurophSearch();
        DataSet allDataset = DataSet.createFromFile(fullPath, 14, 1, ";");
        String networkDir = "TemporalNets/";
        File folder = new File(networkDir);
        File[] listOfFiles = folder.listFiles();
        String bestNetworkFile = "";
        Double minMeanError = Double.POSITIVE_INFINITY;
        Double minError = Double.POSITIVE_INFINITY;
        for (File f : listOfFiles) {
            Double networkSumError = 0.0;
            Double maxErrorNetwork = Double.NEGATIVE_INFINITY;
            NeuralNetwork neuralNetwork = NeuralNetwork.load(f.toString());
            //Para sacar el test de cada uno
            //networkTest(f.toString(), problem, f.getName().substring(0, f.getName().length()-5) + ".csv");
            for (DataSetRow dataRow : allDataset.getRows()) {
                neuralNetwork.setInput(dataRow.getInput());
                neuralNetwork.calculate();
                double[] networkOutput = neuralNetwork.getOutput();
                double[] desiredOut = dataRow.getDesiredOutput();
                Normalizer n = problem.getNormalizer();
                Double calculado = n.denormalizeObjective(networkOutput[0]);
                Double deseado = n.denormalizeObjective(desiredOut[0]);
                Double error = abs(calculado - deseado);
                networkSumError += error;
                if (error > maxErrorNetwork) {
                    maxErrorNetwork = error;
                }
            }
            Double meanError = networkSumError / (allDataset.getRows().size());

//            if (maxErrorNetwork < minError) {
//                minError = maxErrorNetwork;
//                minMeanError = meanError;
//                bestNetworkFile = f.toString();
//            }
            if (meanError < minMeanError) {
                minError = maxErrorNetwork;
                minMeanError = meanError;
                bestNetworkFile = f.toString();
            }

        }

        System.out.println(bestNetworkFile);
        System.out.println(minError);
        System.out.println(minMeanError);
    }

    public void networkTest(String fileRoute, Problem problem, String saveRoute) {
        NeurophSearch learningModule = new NeurophSearch();
        DataSet allDataset = DataSet.createFromFile(fullPath, 14, 1, ";");

        try {
            FileWriter fw = new FileWriter(saveRoute, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println("TABLA FINAL; OBJETIVO; RESULTADO; ERROR");
            NeuralNetwork neuralNetwork = NeuralNetwork.load(fileRoute);
            for (DataSetRow dataRow : allDataset.getRows()) {
                neuralNetwork.setInput(dataRow.getInput());
                neuralNetwork.calculate();
                double[] networkOutput = neuralNetwork.getOutput();
                double[] desiredOut = dataRow.getDesiredOutput();
                Normalizer n = problem.getNormalizer();
                Double calculado = n.denormalizeObjective(networkOutput[0]);
                Double deseado = n.denormalizeObjective(desiredOut[0]);
                Double error = calculado - deseado;
                System.out.println("Calculado: " + calculado);
                System.out.println("Deseado: " + deseado);
                System.out.println("Error: " + error + "\n");
                DecimalFormat ERROR_DF = new DecimalFormat("0.00000");
                pw.println(";" + ERROR_DF.format(deseado) + ";" + ERROR_DF.format(calculado) + ";" + ERROR_DF.format(error) + ";");

            }

            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    private void createCombinations(int maxLength, Integer[] possibleNeurons, ArrayList<Integer> curr) {

        // If the current string has reached it's maximum length
        if (curr.size() == maxLength) {
            Integer[] newConfig = new Integer[maxLength];
            for (int i = 0; i < curr.size(); i++) {
                newConfig[i] = curr.get(i);
            }
            neuronsConfig.add(newConfig);

            // Else add each letter from the alphabet to new strings and process these new strings again
        } else {
            for (Integer possibleNeuron : possibleNeurons) {
                ArrayList<Integer> oldCurr = new ArrayList<>(curr);
                curr.add(possibleNeuron);
                createCombinations(maxLength, possibleNeurons, curr);
                curr = oldCurr;
            }
        }
    }

}
