/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

    private final int INPUT = 14;
    private final int OUTPUT = 1;
    private final DataSet trainingDataSet;
    private final DataSet testingDataSet;
    private final Normalizer normalizer;
    private final static DecimalFormat learningRateDF = new DecimalFormat("0.00");
    private final static DecimalFormat errorDF = new DecimalFormat("0.00000");
    
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
        trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ";");
        testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ";");
        normalizer = norm;
    }

    public void onePlot(int linesNum, Double learningRate, TransferFunctionType transferType, int firstLayer, int secondLayer, int propagationType, boolean block) {
        clearAll();
        for (int i = 0; i < linesNum; i++) {
            train(learningRate, transferType, firstLayer, secondLayer, propagationType);
            graphTestData.add(chartTestData);
            graphTrainData.add(chartTrainData);
        }
        String graphName = "Train TF:" + transferType.toString() + " LR:" + learningRate.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTrainGraph = new LineChartSample((ArrayList<ChartData>) graphTrainData.clone(), graphName, false);

        graphName = "Test TF:" + transferType.toString() + " LR:" + learningRate.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTestGraph = new LineChartSample((ArrayList<ChartData>) graphTestData.clone(), graphName, block);
    }

    public void onePlot(ArrayList<Double> learningRates, TransferFunctionType transferType, int firstLayer, int secondLayer, int propagationType, boolean block) {
        clearAll();
        for (Double learningRate : learningRates) {
            train(learningRate, transferType, firstLayer, secondLayer, propagationType);
            graphTestData.add(chartTestData);
            graphTrainData.add(chartTrainData);

        }
        String graphName = "Train TF:" + transferType.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTrainGraph = new LineChartSample((ArrayList<ChartData>) graphTrainData.clone(), graphName, false);

        graphName = "Test TF:" + transferType.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTestGraph = new LineChartSample((ArrayList<ChartData>) graphTestData.clone(), graphName, block);

    }

    public void onePlot(Double learningRate, ArrayList<TransferFunctionType> transferTypes, int firstLayer, int secondLayer, int propagationType, boolean block) {
        clearAll();
        for (TransferFunctionType transferType : transferTypes) {
            train(learningRate, transferType, firstLayer, secondLayer, propagationType);
            graphTestData.add(chartTestData);
            graphTrainData.add(chartTrainData);

        }
        String graphName = "Train LR:" + learningRate.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTrainGraph = new LineChartSample((ArrayList<ChartData>) graphTrainData.clone(), graphName, false);

        graphName = "Test LR:" + learningRate.toString() + " " + firstLayer + " " + secondLayer;
        LineChartSample lineTestGraph = new LineChartSample((ArrayList<ChartData>) graphTestData.clone(), graphName, block);
    }

    public void createTrainTable(String path) {

        try {
            PrintWriter fullwriter = new PrintWriter(path, "UTF-8");
//            for (int i = 1981; i <= 2011; i++) {
//                    
//                    fullwriter.print(1);
//                    fullwriter.print(";");
//            }
            ArrayList<Integer> columns = new ArrayList<>();
            columns.add(0);
            int column = MAXITERATIONS / 10;
            String firstRow = "Caract\\Error;0;";
            for (int i = 1; i <= 10; i++) {
                int columnNumber=column * (i)-1;
                columns.add(columnNumber);
                firstRow = firstRow + columnNumber + ";";

            }
            fullwriter.println(firstRow);
            
            for (int j = 0; j < graphTrainData.size(); j++) {
                ChartData trainChart = graphTrainData.get(j);
                String nextRow=trainChart.getTransferType().substring(0,2) + " " + trainChart.getLearningRate()+";";
                for (Integer columnIndex: columns){
                    String error = errorDF.format(trainChart.get(columnIndex));
                    nextRow = nextRow + error + ";";
                }
                fullwriter.println(nextRow);
            }
            fullwriter.close();

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
        public void createTestTable(String path) {

        try {
            PrintWriter fullwriter = new PrintWriter(path, "UTF-8");
//            for (int i = 1981; i <= 2011; i++) {
//                    
//                    fullwriter.print(1);
//                    fullwriter.print(";");
//            }
            ArrayList<Integer> columns = new ArrayList<>();
            columns.add(0);
            int column = MAXITERATIONS / 10;
            String firstRow = "Caract\\Error;0;";
            for (int i = 1; i <= 10; i++) {
                int columnNumber=column * (i)-1;
                columns.add(columnNumber);
                firstRow = firstRow + columnNumber + ";";

            }
            fullwriter.println(firstRow);
            
            for (int j = 0; j < graphTestData.size(); j++) {
                ChartData testChart = graphTestData.get(j);
                String nextRow=testChart.getTransferType().substring(0,2) + " " + testChart.getLearningRate()+";";
                for (Integer columnIndex: columns){
                    String error = errorDF.format(testChart.get(columnIndex));
                    nextRow = nextRow + error + ";";
                }
                fullwriter.println(nextRow);
            }
            fullwriter.close();

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
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
        LearningEventListener listener = createListener(neuralNetwork, learningRate, transferType.toString());
        rule.addListener(listener);
        rule.setMaxError(0);
        rule.setMaxIterations(MAXITERATIONS);
        rule.setLearningRate(learningRate);
        neuralNetwork.learn(trainingDataSet, rule);

    }

    private LearningEventListener createListener(NeuralNetwork neuralNetwork, Double learningRate, String transferType) {
        chartTestData = new ChartData(learningRateDF.format(learningRate), transferType);
        chartTrainData = new ChartData(learningRateDF.format(learningRate), transferType);
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
