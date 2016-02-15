/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import static org.neuroph.core.events.LearningEvent.Type.EPOCH_ENDED;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author portatil
 */
public class NeurophModule {

    private static final int INPUT = 14;
    private static final int OUTPUT = 1;
    private static ArrayList<TransferFunctionType> testTipes = new ArrayList<>();
    private static DataSet trainingDataSet;
    private static DataSet testingDataSet;
    private static Normalizer normalizer;

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String PERCEPTRON_SAVE = "PerceptronSaves/Perceptron-";
    private static final String MLPERCEPTRON_SAVE = "MLPerceptronSaves/MLPerceptron-";

    private static double MAXERROR = 0.001;
    private static int MAXITERATIONS = 10000;

    private static ChartData chartData;

    public NeurophModule(ArrayList<TransferFunctionType> arrayTestTipes, String trainingFile, String testingFile, Normalizer norm) {
        testTipes = arrayTestTipes;
        trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ",");
        testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ",");
        normalizer = norm;
    }

    public NeurophModule(TransferFunctionType transferType, String trainingFile, String testingFile, Normalizer norm) {
        testTipes.add(transferType);
        trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ",");
        testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ",");
        normalizer = norm;
    }

    public void test() {
//
//        System.out.println("-----PERCEPTRON-----");
//
//        for (TransferFunctionType type : testTipes) {
//            chartData.clear();
//            System.out.print(ANSI_RED + "TEST " + TransferFunctionType.valueOf(type.toString()) + ". ");
//
//            NeuralNetwork neuralNetwork = new Perceptron(INPUT, OUTPUT, type);
//            long before = System.currentTimeMillis();
//            BackPropagation rule = new BackPropagation();
//            rule.setLearningRate(LEARNINGRATE);
//            rule.setMaxError(MAXERROR);
//            rule.setMaxIterations(MAXITERATIONS);
//            LearningEventListener listener = new LearningEventListener() {
//                @Override
//                public void handleLearningEvent(LearningEvent le) {
//                    if (le.getEventType() == EPOCH_ENDED) {
//                        System.out.println("EPOCH_ENDED");
//                        mseToChart(neuralNetwork, testingDataSet, "TESTING");
//                    } else {
//                        System.out.println("TRAIN ENDED");
//                    }
//                }
//            };
//            rule.addListener(listener);
//            neuralNetwork.learn(trainingDataSet, rule);
//            long time = System.currentTimeMillis() - before;
//
////            System.out.println("Terminado en " + time + " ms." + ANSI_RESET);
//
//            LineChartSample lcs = new LineChartSample(chartData, type.toString());
//
////            testNetwork(neuralNetwork, trainingDataSet);
//            //Para guardar:
////            String saveFile = PERCEPTRON_SAVE + TransferFunctionType.valueOf(type.toString()) + ".nnet";
////            neuralNetwork.save(saveFile);
////Para cargar:
////NeuralNetwork loadedPerceptron = NeuralNetwork.createFromFile("mySamplePerceptron.nnet");
//            try {
//
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(NeurophModule.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
    }

    public void test2() {

        for (TransferFunctionType type : testTipes) {
            ArrayList<ChartData> arrayChartData = new ArrayList<>();
            double learningRate = 0.05;
            for (int i = 0; i < 9; i++) {
                NeuralNetwork neuralNetwork = new Perceptron(INPUT, OUTPUT, type);
                BackPropagation rule = new BackPropagation();
                LearningEventListener listener = new LearningEventListener() {
                    @Override
                    public void handleLearningEvent(LearningEvent le) {
                        if (le.getEventType() == EPOCH_ENDED) {
                            System.out.println("EPOCH ENDED");
                            mseToChartData(neuralNetwork, testingDataSet);
                        } else {
                            System.out.println("TRAIN ENDED");
                        }
                    }
                };
                rule.addListener(listener);
                rule.setMaxError(MAXERROR);
                rule.setMaxIterations(MAXITERATIONS);

                rule.setLearningRate(learningRate);
                learningRate = learningRate - 0.005;
                DecimalFormat df = new DecimalFormat("0.0000");
                chartData = new ChartData(df.format(learningRate));
                neuralNetwork.learn(trainingDataSet, rule);
                arrayChartData.add(chartData.clone());
            }

            (new Thread() {
                public void run() {
                    LineChartSample lcs = new LineChartSample(arrayChartData, type.toString());
                }
            }).start();

            

            //testNetwork(neuralNetwork, trainingDataSet);
            //Para guardar:
            //String saveFile = PERCEPTRON_SAVE + TransferFunctionType.valueOf(type.toString()) + ".nnet";
            //neuralNetwork.save(saveFile);
            //Para cargar:
            //NeuralNetwork loadedPerceptron = NeuralNetwork.createFromFile("mySamplePerceptron.nnet");
            try {

                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(NeurophModule.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private static void mseToChartData(NeuralNetwork nnet, DataSet tset) {

        Double mse = netWorkMSE(nnet, tset);
        chartData.add(mse);

    }

    private static Double netWorkMSE(NeuralNetwork nnet, DataSet tset) {
//        https://github.com/neuroph/neuroph/blob/master/neuroph-2.9/Contrib/src/main/java/org/neuroph/contrib/eval/Evaluation.java
// ellos hacen ((real-calculado)²)/2*n  ¿¿??
        Double sumatorio = 0.0;

        for (DataSetRow dataRow : tset.getRows()) {

            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[] networkOutput = nnet.getOutput();
            double[] desiredOut = dataRow.getDesiredOutput();
            double errorParcial = networkOutput[0] - desiredOut[0];
            double sumaux = errorParcial * errorParcial;
            sumatorio = sumatorio + sumaux;

//            System.out.println("Error parcial: " + errorParcial);
//            System.out.print("Desired: " + desiredOut[0] + " ");
            //System.out.println("Input: " + Arrays.toString(dataRow.getInput()));
//            System.out.println(" Output: " + networkOutput[0]);
            //TODO CALCULAR MSE Y CERCIORARSE DE QUE SEA EL MISMO QUE USA NEUROPH
        }

        return sumatorio / INPUT;

    }

}
