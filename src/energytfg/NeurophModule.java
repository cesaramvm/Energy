/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.text.DecimalFormat;
import java.util.ArrayList;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import static org.neuroph.core.events.LearningEvent.Type.EPOCH_ENDED;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author portatil
 */
public class NeurophModule {

    private final int INPUT = 14;
    private final int OUTPUT = 1;
    private final DataSet trainingDataSet;
    private final DataSet testingDataSet;
    private final Normalizer normalizer;

    private final String PERCEPTRON_SAVE = "PerceptronSaves/Perceptron-";
    private final String MLPERCEPTRON_SAVE = "MLPerceptronSaves/MLPerceptron-";

    private final int MAXITERATIONS;
    public static int Bprop = 0;
    public static int Rprop = 1;

    private ArrayList<ChartData> graphTestData = new ArrayList<>();
    private ArrayList<ChartData> graphTrainData = new ArrayList<>();
    private ChartData chartTestData;
    private ChartData chartTrainData;

    public NeurophModule(int iterations, String trainingFile, String testingFile, Normalizer norm) {
        MAXITERATIONS = iterations;
        trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ",");
        testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ",");
        normalizer = norm;
    }

    public void onePlot(int linesNum, Double learningRate, TransferFunctionType transferType, int firstLayer, int secondLayer, int propagationType, boolean block) {
        clearAll();
        for (int i = 0; i < linesNum; i++) {
            train(learningRate, transferType, firstLayer, secondLayer, propagationType);
            graphTestData.add(chartTestData);
            graphTrainData.add(chartTrainData);
        }
        String graphName = "Train " + transferType.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTrainGraph = new LineChartSample((ArrayList<ChartData>) graphTrainData.clone(), graphName, false);

        graphName = "Test " + transferType.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTestGraph = new LineChartSample((ArrayList<ChartData>) graphTestData.clone(), graphName, block);
    }

    public void onePlot(ArrayList<Double> learningRates, TransferFunctionType transferType, int firstLayer, int secondLayer, int propagationType, boolean block) {
        clearAll();
        for (Double learningRate : learningRates) {
            train(learningRate, transferType, firstLayer, secondLayer, propagationType);
            graphTestData.add(chartTestData);
            graphTrainData.add(chartTrainData);
            String graphName = "Train " + transferType.toString() + " " + firstLayer + " " + secondLayer;
            LineChartSample lineTrainGraph = new LineChartSample((ArrayList<ChartData>) graphTrainData.clone(), graphName, false);

            graphName = "Test " + transferType.toString() + " " + firstLayer + " " + secondLayer;
            LineChartSample lineTestGraph = new LineChartSample((ArrayList<ChartData>) graphTestData.clone(), graphName, block);
        }

    }

    public void onePlot(Double learningRate, ArrayList<TransferFunctionType> transferTypes, int firstLayer, int secondLayer, int propagationType, boolean block) {
        clearAll();
        for (TransferFunctionType transferType : transferTypes) {
            train(learningRate, transferType, firstLayer, secondLayer, propagationType);
            graphTestData.add(chartTestData);
            graphTrainData.add(chartTrainData);
            String graphName = "Train " + transferType.toString() + " " + firstLayer + " " + secondLayer;
            LineChartSample lineTrainGraph = new LineChartSample((ArrayList<ChartData>) graphTrainData.clone(), graphName, false);

            graphName = "Test " + transferType.toString() + " " + firstLayer + " " + secondLayer;
            LineChartSample lineTestGraph = new LineChartSample((ArrayList<ChartData>) graphTestData.clone(), graphName, block);
        }

    }

    private void train(Double learningRate, TransferFunctionType transferType, int firstLayer, int secondLayer, int propagationType) {
        NeuralNetwork neuralNetwork;
        if (firstLayer == 0 || learningRate == 0) {
            throw new Error("First layer or learning rate can't be 0");
        }
        if (secondLayer != 0) {
            neuralNetwork = new MultiLayerPerceptron(transferType, INPUT, firstLayer, secondLayer, OUTPUT);
        } else {
            neuralNetwork = new MultiLayerPerceptron(transferType, INPUT, firstLayer, OUTPUT);
        }
        LMS rule;
        if (propagationType == Bprop) {
            rule = new BackPropagation();
        } else if (propagationType == Rprop) {
            rule = new ResilientPropagation();
        } else {
            throw new Error("Unknown propagation type");
        }
        LearningEventListener listener = createListener(neuralNetwork, learningRate);
        rule.addListener(listener);
        rule.setMaxError(0);
        rule.setMaxIterations(MAXITERATIONS);
        rule.setLearningRate(learningRate);
        neuralNetwork.learn(trainingDataSet, rule);

    }

    private LearningEventListener createListener(NeuralNetwork neuralNetwork, Double learningRate) {
        DecimalFormat df = new DecimalFormat("0.00");
        chartTestData = new ChartData(df.format(learningRate));
        chartTrainData = new ChartData(df.format(learningRate));
        LearningEventListener listener = (LearningEvent le) -> {
            if (le.getEventType() == EPOCH_ENDED) {
                mseToChartData(neuralNetwork, testingDataSet, chartTestData);
                mseToChartData(neuralNetwork, trainingDataSet, chartTrainData);
            } else {
                System.out.println("        Finished LR - " + learningRate);
            }
        };
        return listener;
    }

    private void mseToChartData(NeuralNetwork neuralNetwork, DataSet dataSet, ChartData chartData) {

        Double mse = netWorkMSE(neuralNetwork, dataSet);
        chartData.add(mse);

    }

    private Double netWorkMSE(NeuralNetwork nnet, DataSet tset) {
//https://github.com/neuroph/neuroph/blob/master/neuroph-2.9/Core/src/main/java/org/neuroph/core/learning/error/MeanSquaredError.java
// ellos hacen ((real-calculado)²)/2*n  ¿¿??
        Double sumatorio = 0.0;

        for (DataSetRow dataRow : tset.getRows()) {

            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[] networkOutput = nnet.getOutput();
            double[] desiredOut = dataRow.getDesiredOutput();
            double errorParcial = networkOutput[0] - desiredOut[0];
//            System.out.println("Output: " + normalizer.denormalizeObjective(networkOutput[0]));
//            System.out.println("Desired: " + normalizer.denormalizeObjective(desiredOut[0]));
//            System.out.println("Error: " + errorParcial);
//            double errorParcial = normalizer.denormalizeObjective(networkOutput[0]) - normalizer.denormalizeObjective(desiredOut[0]);

            double sumaux = errorParcial * errorParcial;
            sumatorio = sumatorio + sumaux;

//            System.out.println("Error parcial: " + errorParcial);
//            System.out.print("Desired: " + desiredOut[0] + " ");
            //System.out.println("Input: " + Arrays.toString(dataRow.getInput()));
//            System.out.println(" Output: " + networkOutput[0]);
            //TODO CALCULAR MSE Y CERCIORARSE DE QUE SEA EL MISMO QUE USA NEUROPH
        }

        return sumatorio / (2 * INPUT); //Así lo hace neuroph
//        return sumatorio / (INPUT);

    }

    private void clearAll() {

        chartTestData = null;
        chartTrainData = null;
        graphTestData.clear();
        graphTrainData.clear();
    }

}
