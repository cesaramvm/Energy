/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import static org.neuroph.core.events.LearningEvent.Type.EPOCH_ENDED;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author portatil
 */
public class NeurophModule {

    private static ArrayList<TransferFunctionType> testTipes = new ArrayList<>();
    private static DataSet trainingDataSet;
    private static DataSet testingDataSet;
    private static final int INPUT = 14;
    private static final int OUTPUT = 1;
    private static double MAXERROR = 0.01;
    private static int MAXITERATIONS = 5000;
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String PERCEPTRON_SAVE = "PerceptronSaves/Perceptron-";
    private static final String MLPERCEPTRON_SAVE = "MLPerceptronSaves/MLPerceptron-";

    public NeurophModule(ArrayList<TransferFunctionType> arrayTestTipes, String trainingFile, String testingFile) {
        testTipes = arrayTestTipes;
        trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ",");
        testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ",");
        clearTransferFunctionsNotWorking();
    }

    public NeurophModule(TransferFunctionType transferType, String trainingFile, String testingFile) {
        testTipes.add(transferType);
        trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ",");
        testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ",");
        clearTransferFunctionsNotWorking();
    }

    public void testPerceptron() {

        System.out.println("-----PERCEPTRON-----");

        for (TransferFunctionType type : testTipes) {
            System.out.print(ANSI_RED + "TEST " + TransferFunctionType.valueOf(type.toString()) + ". ");

            NeuralNetwork neuralNetwork = new Perceptron(INPUT, OUTPUT, type);

            long before = System.currentTimeMillis();
            neuralNetwork.learn(trainingDataSet);
            long time = System.currentTimeMillis() - before;

            System.out.println("Terminado en " + time + " ms." + ANSI_RESET);

            testNetwork(neuralNetwork, trainingDataSet);

            //Para guardar:
            String saveFile = PERCEPTRON_SAVE + TransferFunctionType.valueOf(type.toString()) + ".nnet";
            neuralNetwork.save(saveFile);
            //Para cargar:
            //NeuralNetwork loadedPerceptron = NeuralNetwork.createFromFile("mySamplePerceptron.nnet");

        }

    }

    public void testMultiLayerPerceptron() {

        System.out.println("-----MULTI PERCEPTRON-----");

        for (TransferFunctionType type : testTipes) {
            System.out.print(ANSI_RED + "TEST " + TransferFunctionType.valueOf(type.toString()) + ". ");

            NeuralNetwork neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.GAUSSIAN, INPUT, OUTPUT);

            long before = System.currentTimeMillis();
            neuralNetwork.learn(trainingDataSet);
            long time = System.currentTimeMillis() - before;

            System.out.println("Terminado en " + time + " ms." + ANSI_RESET);

            testNetwork(neuralNetwork, trainingDataSet);

            //Para guardar:
            String saveFile = MLPERCEPTRON_SAVE + TransferFunctionType.valueOf(type.toString()) + ".nnet";
            neuralNetwork.save(saveFile);
            //Para cargar:
            //NeuralNetwork loadedPerceptron = NeuralNetwork.createFromFile("mySamplePerceptron.nnet");

        }

    }

    public static void testNetwork(NeuralNetwork nnet, DataSet tset) {

        for (DataSetRow dataRow : tset.getRows()) {

            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[] networkOutput = nnet.getOutput();
            double[] desiredOut = dataRow.getDesiredOutput();
            double errorParcial = networkOutput[0] - desiredOut[0];
            System.out.println("Error parcial: " + errorParcial);
            System.out.print("Desired: " + desiredOut[0] + " ");
//            System.out.println("Input: " + Arrays.toString(dataRow.getInput()));
            System.out.println(" Output: " + networkOutput[0]);

            //TODO CALCULAR MSE Y CERCIORARSE DE QUE SEA EL MISMO QUE USA NEUROPH
            //LISTENERS NEUROPH
        }

        System.out.println("\n\n");

    }

    public void test() {

        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.GAUSSIAN, 14, 2, 1);
        MomentumBackpropagation rule = new MomentumBackpropagation();
        rule.setLearningRate(0.5);
        rule.setMomentum(0.5);
        rule.setMaxError(MAXERROR);
        rule.setMaxIterations(MAXITERATIONS);
        //new LMS(); <-SupervisedLearning<-IterativeLearning<-LearningRule
        //new SigmoidDeltaRule();         new SupervisedHebbianLearning();
        //new BackPropagation();
        //new MomentumBackpropagation();
        neuralNetwork.learn(trainingDataSet, rule);

    }

    public void test2() {
//        LineChartSample lcs = new LineChartSample();
//        ArrayList<Double> test = new ArrayList<>();
//        test.add(91.24);
//        test.add(44.14);
//        test.add(34.17);
//        test.add(41.49);
//        test.add(18.58);
//
//        Thread thread = new Thread() {
//            public void run() {
//                      lcs.execute(test, "BackProp + Gaussian + lr = 0.4 + maxep = 10000 + maxerr = 15+ 3 2 2 1");
//            }
//        };
//
//        thread.start();

  

        System.out.println("-----PERCEPTRON-----");

        for (TransferFunctionType type : testTipes) {
            System.out.print(ANSI_RED + "TEST " + TransferFunctionType.valueOf(type.toString()) + ". ");

            NeuralNetwork neuralNetwork = new Perceptron(INPUT, OUTPUT, type);
            long before = System.currentTimeMillis();
            BackPropagation rule = new BackPropagation();
            rule.setMaxError(0.001);
            LearningEventListener lel = new LearningEventListener() {
                @Override
                public void handleLearningEvent(LearningEvent le) {
                    if (le.getEventType() == EPOCH_ENDED) {
                        System.out.println("EPOCH_ENDED");
                    }
                }
            };
            rule.addListener(lel);
            neuralNetwork.learn(trainingDataSet, rule);
            long time = System.currentTimeMillis() - before;

            System.out.println("Terminado en " + time + " ms." + ANSI_RESET);

            testNetwork(neuralNetwork, trainingDataSet);

            //Para guardar:
//            String saveFile = PERCEPTRON_SAVE + TransferFunctionType.valueOf(type.toString()) + ".nnet";
//            neuralNetwork.save(saveFile);
            //Para cargar:
            //NeuralNetwork loadedPerceptron = NeuralNetwork.createFromFile("mySamplePerceptron.nnet");

        }
    }

    private void clearTransferFunctionsNotWorking() {
//        testTipes.remove(TransferFunctionType.STEP);
//        testTipes.remove(TransferFunctionType.SGN);
//        testTipes.remove(TransferFunctionType.LOG);
    }

}
