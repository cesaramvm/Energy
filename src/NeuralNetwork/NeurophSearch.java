/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import Util.Normalizers.Normalizer;
import ChartPackage.LineChartSample;
import ChartPackage.ChartData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author portatil
 */
public class NeurophSearch {

    private final static int INPUT = 14;
    private final static int OUTPUT = 1;
    private final static DecimalFormat LEARNING_RATE_DF = new DecimalFormat("0.00");
    private final static DecimalFormat ERROR_DF = new DecimalFormat("0.00000");
    private final String PERCEPTRON_SAVE = "PerceptronSaves/Perceptron-";
    private final String MLPERCEPTRON_SAVE = "MLPerceptronSaves/MLPerceptron-";
    public final static int BPROP = 0;
    public final static int RPROP = 1;
    public final static int TRAINING_GRAPH = 0;
    public final static int TEST_GRAPH = 1;

    private final DataSet trainingDataSet;
    private final DataSet testingDataSet;
    private final Normalizer normalizer;

    private final int MAXITERATIONS;
    private final int propagationType;
    private final boolean showTrainGraph;
    private final boolean showGraph;

    private final ArrayList<ChartData> graphTestData = new ArrayList<>();
    private final ArrayList<ChartData> graphTrainingData = new ArrayList<>();
    private ChartData chartTestData;
    private ChartData chartTrainingData;

    public NeurophSearch(int iterations, int propType, boolean trainGraphShow, boolean graphShow, String trainingFile, String testingFile, Normalizer norm) {
        MAXITERATIONS = iterations;
        propagationType = propType;
        showTrainGraph = trainGraphShow;
        showGraph = graphShow;
        trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ";");
        testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ";");
        normalizer = norm;
    }

    public NeurophSearch(){
        trainingDataSet = null;
        testingDataSet = null;
        normalizer = null;
        MAXITERATIONS = 0;
        propagationType = 0;
        showGraph = false;
        showTrainGraph = false;               
        
    }

    public void onePlot(int linesNum, Double learningRate, TransferFunctionType transferType, int[] layers) {
        clearAll();
        for (int i = 0; i < linesNum; i++) {
            train(learningRate, transferType, layers, propagationType);
            graphTestData.add(chartTestData);
            graphTrainingData.add(chartTrainingData);
        }

        if (showGraph) {
            String graphName = "Test TF:" + transferType.toString() + " LR:" + learningRate.toString() + " " + Arrays.toString(layers);
            LineChartSample lineTestGraph = new LineChartSample(new ArrayList<>(graphTestData), graphName);

            if (showTrainGraph) {
                graphName = "Train TF:" + transferType.toString() + " LR:" + learningRate.toString() + " " + Arrays.toString(layers);
                LineChartSample lineTrainGraph = new LineChartSample(new ArrayList<>(graphTrainingData), graphName);
            }
        }

    }

    public void onePlot(ArrayList<Double> learningRates, TransferFunctionType transferType, int[] layers) {
        clearAll();
        for (Double learningRate : learningRates) {
            train(learningRate, transferType, layers, propagationType);
            graphTestData.add(chartTestData);
            graphTrainingData.add(chartTrainingData);

        }
        if (showGraph) {
            String graphName = "Test TF:" + transferType.toString() + " " + Arrays.toString(layers);
            LineChartSample lineTestGraph = new LineChartSample(new ArrayList<>(graphTestData), graphName);

            if (showTrainGraph) {
                graphName = "Train TF:" + transferType.toString() + " " + Arrays.toString(layers);
                LineChartSample lineTrainGraph = new LineChartSample(new ArrayList<>(graphTrainingData), graphName);
            }
        }

    }

    public void onePlot(Double learningRate, ArrayList<TransferFunctionType> transferTypes, int[] layers, boolean block) {
        clearAll();
        for (TransferFunctionType transferType : transferTypes) {
            train(learningRate, transferType, layers, propagationType);
            graphTestData.add(chartTestData);
            graphTrainingData.add(chartTrainingData);

        }

        if (showGraph) {
            String graphName = "Test LR:" + learningRate.toString() + " " + Arrays.toString(layers);
            LineChartSample lineTestGraph = new LineChartSample(new ArrayList<>(graphTestData), graphName);

            if (showTrainGraph) {
                graphName = "Train LR:" + learningRate.toString() + " " + Arrays.toString(layers);
                LineChartSample lineTrainGraph = new LineChartSample(new ArrayList<>(graphTrainingData), graphName);
            }
        }

    }

    public void writeTable(int tableType, String path, boolean append) {
        ArrayList<ChartData> graphToBePrinted;
        String realpath = "NetworkSaves/";
        realpath += path;
        switch (tableType) {
            case TRAINING_GRAPH:
                graphToBePrinted = graphTrainingData;
                break;
            case TEST_GRAPH:
                graphToBePrinted = graphTestData;
                break;
            default:
                throw new Error("Unknown table type");
        }
        try {
            boolean existance = false;
            File f = new File(realpath);
            if (f.exists() && !f.isDirectory()) {
                existance = true;
            }
            FileWriter fw = new FileWriter(realpath, append);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            write(pw, graphToBePrinted, existance);
        } catch (IOException ex) {
            Logger.getLogger(NeurophSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void write(PrintWriter fullwriter, ArrayList<ChartData> graphToBePrinted, boolean existance) {

        ArrayList<Integer> columns = new ArrayList<>();
        columns.add(0);
        int column = MAXITERATIONS / 10;
        for (int i = 1; i <= 10; i++) {
            int columnNumber = column * (i) - 1;
            columns.add(columnNumber);
        }
        if (!existance) {
            String firstRow = ";;";
            for (Integer columnIndex : columns) {
                firstRow = firstRow + columnIndex + ";";
            }
            fullwriter.println(firstRow);
        }

        for (int j = 0; j < graphToBePrinted.size(); j++) {
            ChartData trainChart = graphToBePrinted.get(j);
            String nextRow = trainChart.getTransferType().substring(0, 2) + " " + trainChart.getLearningRate() + ";" + Arrays.toString(graphToBePrinted.get(0).getLayersConf()) + ";";
            for (Integer columnIndex : columns) {
                String error = ERROR_DF.format(trainChart.get(columnIndex));
                nextRow += error + ";";
            }
            fullwriter.println(nextRow);
        }
        fullwriter.close();

    }

    private void train(Double learningRate, TransferFunctionType transferType, int[] layers, int propagationType) {
        NeuralNetwork neuralNetwork;

        if (layers.length == 0 || learningRate == 0) {
            throw new Error("Learning rate can't be 0 and layers must contain something");
        }
        neuralNetwork = new MultiLayerPerceptron(transferType, layers);
        LMS rule;
        switch (propagationType) {
            case BPROP:
                rule = new BackPropagation();
                break;
            case RPROP:
                rule = new ResilientPropagation();
                break;
            default:
                throw new Error("Unknown propagation type");
        }
        LearningEventListener listener = createListener(neuralNetwork, learningRate, transferType.toString(), layers);
        rule.addListener(listener);
        rule.setMaxError(0);
        rule.setMaxIterations(MAXITERATIONS);
        rule.setLearningRate(learningRate);
        neuralNetwork.learn(trainingDataSet, rule);
        Double mse = netWorkMSE(neuralNetwork, testingDataSet);
        neuralNetwork.save("NetworkSaves/Networks/" + ERROR_DF.format(mse) + "-Network-" + transferType.toString().substring(0, 2) + "-" + learningRate + "-" + Arrays.toString(layers) + ".nnet");
    }

    private LearningEventListener createListener(NeuralNetwork neuralNetwork, Double learningRate, String transferType, int[] layers) {
        chartTestData = new ChartData(LEARNING_RATE_DF.format(learningRate), transferType, layers);
        chartTrainingData = new ChartData(LEARNING_RATE_DF.format(learningRate), transferType, layers);
        LearningEventListener listener = (LearningEvent le) -> {
            if (le.getEventType() == EPOCH_ENDED) {
                mseToChartData(neuralNetwork, testingDataSet, chartTestData);
                mseToChartData(neuralNetwork, trainingDataSet, chartTrainingData);
            } else {
                //System.out.println("        Finished LR - " + learningRate);
            }
        };
        return listener;
    }

    private void mseToChartData(NeuralNetwork neuralNetwork, DataSet dataSet, ChartData chartData) {

        Double mse = netWorkMSE(neuralNetwork, dataSet);
        chartData.add(mse);

    }

    public Double netWorkMSE(NeuralNetwork nnet, DataSet tset) {
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
        chartTrainingData = null;
        graphTestData.clear();
        graphTrainingData.clear();
    }

}
