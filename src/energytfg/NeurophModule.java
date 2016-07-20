/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author portatil
 */
public class NeurophModule {

    private final static int INPUT = 14;
    private final static int OUTPUT = 1;
    private final static DecimalFormat LEARNING_RATE_DF = new DecimalFormat("0.00");
    private final static DecimalFormat ERROR_DF = new DecimalFormat("0.00000");
    private final String PERCEPTRON_SAVE = "PerceptronSaves/Perceptron-";
    private final String MLPERCEPTRON_SAVE = "MLPerceptronSaves/MLPerceptron-";
    private final String TEST_TABLE_PATH = "ProjectTables/test-table.csv";
    private static final String TRAINING_TABLE_PATH = "ProjectTables/training-table.csv";
    public final static int BPROP = 0;
    public final static int RPROP = 1;
    public final static int TRAINING = 0;
    public final static int TEST = 1;

    private final DataSet trainingDataSet;
    private final DataSet testingDataSet;
    private final Normalizer normalizer;

    private final int MAXITERATIONS;
    private final int propagationType;
    private final boolean showTrainGraph;

    private final ArrayList<ChartData> graphTestData = new ArrayList<>();
    private final ArrayList<ChartData> graphTrainingData = new ArrayList<>();
    private ChartData chartTestData;
    private ChartData chartTrainingData;

    public NeurophModule(int iterations, int propType, boolean trainGraphShow, String trainingFile, String testingFile, Normalizer norm) {
        MAXITERATIONS = iterations;
        propagationType = propType;
        showTrainGraph = trainGraphShow;
        trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ";");
        testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ";");
        normalizer = norm;
    }

    public void onePlot(int linesNum, Double learningRate, TransferFunctionType transferType, int firstLayer, int secondLayer, boolean block) {
        clearAll();
        for (int i = 0; i < linesNum; i++) {
            train(learningRate, transferType, firstLayer, secondLayer, propagationType);
            graphTestData.add(chartTestData);
            graphTrainingData.add(chartTrainingData);
        }

        String graphName = "Test TF:" + transferType.toString() + " LR:" + learningRate.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTestGraph = new LineChartSample((ArrayList<ChartData>) graphTestData.clone(), graphName, block);

        if (showTrainGraph) {
            graphName = "Train TF:" + transferType.toString() + " LR:" + learningRate.toString() + " " + firstLayer + " " + secondLayer;
            LineChartSample lineTrainGraph = new LineChartSample((ArrayList<ChartData>) graphTrainingData.clone(), graphName, false);
        }

    }

    public void onePlot(ArrayList<Double> learningRates, TransferFunctionType transferType, int firstLayer, int secondLayer, boolean block) {
        clearAll();
        for (Double learningRate : learningRates) {
            train(learningRate, transferType, firstLayer, secondLayer, propagationType);
            graphTestData.add(chartTestData);
            graphTrainingData.add(chartTrainingData);

        }

        String graphName = "Test TF:" + transferType.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTestGraph = new LineChartSample((ArrayList<ChartData>) graphTestData.clone(), graphName, block);

        if (showTrainGraph) {
            graphName = "Train TF:" + transferType.toString() + " " + firstLayer + " " + secondLayer;
            LineChartSample lineTrainGraph = new LineChartSample((ArrayList<ChartData>) graphTrainingData.clone(), graphName, false);
        }

    }

    public void onePlot(Double learningRate, ArrayList<TransferFunctionType> transferTypes, int firstLayer, int secondLayer, boolean block) {
        clearAll();
        for (TransferFunctionType transferType : transferTypes) {
            train(learningRate, transferType, firstLayer, secondLayer, propagationType);
            graphTestData.add(chartTestData);
            graphTrainingData.add(chartTrainingData);

        }

        String graphName = "Test LR:" + learningRate.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTestGraph = new LineChartSample((ArrayList<ChartData>) graphTestData.clone(), graphName, block);

        if (showTrainGraph) {
            graphName = "Train LR:" + learningRate.toString() + " " + firstLayer + " " + secondLayer;
            LineChartSample lineTrainGraph = new LineChartSample((ArrayList<ChartData>) graphTrainingData.clone(), graphName, false);
        }

    }

    public void writeTable(int tableType, boolean append) {
        ArrayList<ChartData> graphToBePrinted;
        String path;
        if (tableType == TRAINING) {
            path = TRAINING_TABLE_PATH;
            graphToBePrinted = graphTrainingData;
        } else if (tableType == TEST) {
            path = TEST_TABLE_PATH;
            graphToBePrinted = graphTestData;
        } else {
            throw new Error("Unknown table type");
        }
        try {
            FileWriter fw = new FileWriter(path, append);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            write(pw, graphToBePrinted);
        } catch (IOException ex) {
            Logger.getLogger(NeurophModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void write(PrintWriter fullwriter, ArrayList<ChartData> graphToBePrinted) {

        ArrayList<Integer> columns = new ArrayList<>();
        columns.add(0);
        int column = MAXITERATIONS / 10;
        String firstRow = "Caract\\Error;0;";
        for (int i = 1; i <= 10; i++) {
            int columnNumber = column * (i) - 1;
            columns.add(columnNumber);
            firstRow = firstRow + columnNumber + ";";

        }
        fullwriter.println(firstRow);

        for (int j = 0; j < graphToBePrinted.size(); j++) {
            ChartData trainChart = graphToBePrinted.get(j);
            String nextRow = trainChart.getTransferType().substring(0, 2) + " " + trainChart.getLearningRate() + ";";
            for (Integer columnIndex : columns) {
                String error = ERROR_DF.format(trainChart.get(columnIndex));
                nextRow = nextRow + error + ";";
            }
            fullwriter.println(nextRow);
        }
        fullwriter.close();

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
        if (propagationType == BPROP) {
            rule = new BackPropagation();
        } else if (propagationType == RPROP) {
            rule = new ResilientPropagation();
        } else {
            throw new Error("Unknown propagation type");
        }
        LearningEventListener listener = createListener(neuralNetwork, learningRate, transferType.toString());
        rule.addListener(listener);
        rule.setMaxError(0);
        rule.setMaxIterations(MAXITERATIONS);
        rule.setLearningRate(learningRate);
        neuralNetwork.learn(trainingDataSet, rule);

    }

    private LearningEventListener createListener(NeuralNetwork neuralNetwork, Double learningRate, String transferType) {
        chartTestData = new ChartData(LEARNING_RATE_DF.format(learningRate), transferType);
        chartTrainingData = new ChartData(LEARNING_RATE_DF.format(learningRate), transferType);
        LearningEventListener listener = (LearningEvent le) -> {
            if (le.getEventType() == EPOCH_ENDED) {
                mseToChartData(neuralNetwork, testingDataSet, chartTestData);
                mseToChartData(neuralNetwork, trainingDataSet, chartTrainingData);
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
        chartTrainingData = null;
        graphTestData.clear();
        graphTrainingData.clear();
    }

}
