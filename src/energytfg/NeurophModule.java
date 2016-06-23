/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import static org.neuroph.core.events.LearningEvent.Type.EPOCH_ENDED;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
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
    private static int MAXITERATIONS = 60 * 1000;

    private static ChartData chartData;

    public NeurophModule(String trainingFile, String testingFile, Normalizer norm) {
        trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ",");
        testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ",");
        normalizer = norm;
    }
    
    public void setTransferTypes (ArrayList<TransferFunctionType> arrayTestTipes){
        testTipes = arrayTestTipes;
    }
    
    void start() {
        
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

    public void testRprop() {

//        ArrayList<Double> learningRates
//                = new ArrayList<>(Arrays.asList(0.02, 0.01, 0.005, 0.001));
        ArrayList<Double> learningRates
                = new ArrayList<>(Arrays.asList(0.01, 0.05, 0.1, 0.2, 0.3, 0.5));

        for (TransferFunctionType type : testTipes) {
            System.out.println(type.toString());
            ArrayList<ChartData> arrayChartData = new ArrayList<>();
            for (int i = 0; i < learningRates.size(); i++) {
                NeuralNetwork neuralNetwork = new Perceptron(INPUT, OUTPUT, type);

                Double newLearningRate = learningRates.get(i);
                ResilientPropagation rule = new ResilientPropagation();
                LearningEventListener listener = new LearningEventListener() {
                    int j = 0;

                    @Override
                    public void handleLearningEvent(LearningEvent le) {
                        if (le.getEventType() == EPOCH_ENDED) {
                            mseToChartData(neuralNetwork, testingDataSet);
                            j++;
                        } else {
                            System.out.println(newLearningRate + " " + j);
                        }
                    }
                };
                rule.addListener(listener);
                rule.setMaxError(0);
                rule.setMaxIterations(MAXITERATIONS);
                rule.setLearningRate(newLearningRate);

                DecimalFormat df = new DecimalFormat("0.000");
                chartData = new ChartData(df.format(newLearningRate));
                neuralNetwork.learn(trainingDataSet, rule);
                arrayChartData.add(chartData.clone());
            }

            boolean block = false;
            LineChartSample lcs = new LineChartSample(arrayChartData, type.toString(), block);

        }
    }

    public void testBackprop() {
        ArrayList<Double> learningRates
                = new ArrayList<>(Arrays.asList(0.02, 0.01, 0.005, 0.001));

        for (TransferFunctionType type : testTipes) {
            //Poner unos cuantos parámetros de la red... Capas ocultas nº neuronas, learningRate y nada más
            ArrayList<ChartData> arrayChartData = new ArrayList<>();
            for (int i = 0; i < learningRates.size(); i++) {
                NeuralNetwork neuralNetwork = new Perceptron(INPUT, OUTPUT, type);

                Double newLearningRate = learningRates.get(i);
                BackPropagation rule = new BackPropagation();
                LearningEventListener listener = new LearningEventListener() {
                    int j = 0;

                    @Override
                    public void handleLearningEvent(LearningEvent le) {
                        if (le.getEventType() == EPOCH_ENDED) {
                            mseToChartData(neuralNetwork, testingDataSet);
                            j++;
                        } else {
                            System.out.println(newLearningRate + " " + j);
                        }
                    }
                };
                rule.addListener(listener);
                rule.setMaxError(MAXERROR);
                rule.setMaxIterations(MAXITERATIONS);
                rule.setLearningRate(newLearningRate);

                DecimalFormat df = new DecimalFormat("0.000");
                chartData = new ChartData(df.format(newLearningRate));
                neuralNetwork.learn(trainingDataSet, rule);
                arrayChartData.add(chartData.clone());
            }
            boolean block = false;
            LineChartSample lcs = new LineChartSample(arrayChartData, type.toString(), block);

        }
    }

    public void testing() {
        ArrayList<Double> learningRates
                = new ArrayList<>(Arrays.asList(0.1, 0.2, 0.3, 0.5));
        for (TransferFunctionType type : testTipes) {
            System.out.println(type.toString());
            for (int i = 6; i <= 6; i++) {

                System.out.println("    " + i);
                ArrayList<ChartData> arrayChartData = new ArrayList<>();
                Iterator it = learningRates.iterator();
                while (it.hasNext()) {
                    NeuralNetwork neuralNetwork = new MultiLayerPerceptron(type, INPUT, i, OUTPUT);
                    Double newLearningRate = (Double) it.next();
                    ResilientPropagation rule = new ResilientPropagation();
                    LearningEventListener listener = new LearningEventListener() {
                        int j = 0;

                        @Override
                        public void handleLearningEvent(LearningEvent le) {
                            if (le.getEventType() == EPOCH_ENDED) {
                                mseToChartData(neuralNetwork, testingDataSet);
                                j++;
                            } else {
                                System.out.println("        " + newLearningRate + " " + j);
                            }
                        }
                    };
                    rule.addListener(listener);
                    rule.setMaxError(0);
                    rule.setMaxIterations(MAXITERATIONS);
                    rule.setLearningRate(newLearningRate);

                    DecimalFormat df = new DecimalFormat("0.000");
                    chartData = new ChartData(df.format(newLearningRate));
                    neuralNetwork.learn(trainingDataSet, rule);
                    arrayChartData.add(chartData.clone());

                }
                boolean block = false;
                LineChartSample lcs = new LineChartSample(arrayChartData, type.toString() + " " + i + " ", block);

                for (int j = 3; j <= 6; j++) {
                    System.out.println("    " + i + " " + j);
                    ArrayList<ChartData> arrayChartData2 = new ArrayList<>();
                    Iterator it2 = learningRates.iterator();
                    while (it2.hasNext()) {
                        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(type, INPUT, i, j, OUTPUT);
                        Double newLearningRate = (Double) it2.next();
                        ResilientPropagation rule = new ResilientPropagation();
                        LearningEventListener listener = new LearningEventListener() {
                            int j = 0;

                            @Override
                            public void handleLearningEvent(LearningEvent le) {
                                if (le.getEventType() == EPOCH_ENDED) {
                                    mseToChartData(neuralNetwork, testingDataSet);
                                    j++;
                                } else {
                                    System.out.println("        " + newLearningRate + " " + j);
                                }
                            }
                        };
                        rule.addListener(listener);
                        rule.setMaxError(0);
                        rule.setMaxIterations(MAXITERATIONS);
                        rule.setLearningRate(newLearningRate);

                        DecimalFormat df = new DecimalFormat("0.000");
                        chartData = new ChartData(df.format(newLearningRate));
                        neuralNetwork.learn(trainingDataSet, rule);
                        arrayChartData2.add(chartData.clone());

                    }
                    LineChartSample lcs2 = new LineChartSample(arrayChartData2, type.toString() + " " + i + " " + j, block);
                }
            }
        }
    }

    public void simple() {
//        ArrayList<Double> learningRates
//                = new ArrayList<>(Arrays.asList(0.1, 0.2, 0.3, 0.5));
        ArrayList<Double> learningRates
                = new ArrayList<>(Arrays.asList(0.6, 0.7, 0.8));
        for (TransferFunctionType type : testTipes) {
            Iterator it = learningRates.iterator();
            while (it.hasNext()) {
                Double newLearningRate = (Double) it.next();
                System.out.println(type.toString());
                int i = 6;
                int j = 3;
                ArrayList<ChartData> arrayChartData = new ArrayList<>();

                boolean block = false;
                System.out.println("    " + i + " " + j);
                
                
                for (int z = 0; z < 5; z++) {
                    NeuralNetwork neuralNetwork = new MultiLayerPerceptron(type, INPUT, i, j, OUTPUT);
                    ResilientPropagation rule = new ResilientPropagation();
                    LearningEventListener listener = new LearningEventListener() {
                        int j = 0;

                        @Override
                        public void handleLearningEvent(LearningEvent le) {
                            if (le.getEventType() == EPOCH_ENDED) {
                                mseToChartData(neuralNetwork, testingDataSet);
                                j++;
                            } else {
                                System.out.println("        " + newLearningRate + " " + j);
                            }
                        }
                    };
                    rule.addListener(listener);
                    rule.setMaxError(0);
                    rule.setMaxIterations(MAXITERATIONS);
                    rule.setLearningRate(newLearningRate);

                    DecimalFormat df = new DecimalFormat("0.000");
                    chartData = new ChartData(df.format(newLearningRate));
                    neuralNetwork.learn(trainingDataSet, rule);
                    arrayChartData.add(chartData.clone());

                }
                LineChartSample lcs2 = new LineChartSample(arrayChartData, type.toString() + " " + i + " " + j, block);

            }

        }

    }

    public void oneGraph(ArrayList<Double> learningRates, int firstLayer, int secondLayer){
        
        for (TransferFunctionType type : testTipes) {
            Iterator it = learningRates.iterator();
            while (it.hasNext()) {
                Double newLearningRate = (Double) it.next();
                System.out.println(type.toString());
                ArrayList<ChartData> arrayChartData = new ArrayList<>();

                boolean block = false;
                System.out.println("    " + firstLayer + " " + secondLayer);
                
                
                for (int z = 0; z < 5; z++) {
                    NeuralNetwork neuralNetwork = new MultiLayerPerceptron(type, INPUT, firstLayer, secondLayer, OUTPUT);
                    ResilientPropagation rule = new ResilientPropagation();
                    LearningEventListener listener = new LearningEventListener() {
                        int j = 0;

                        @Override
                        public void handleLearningEvent(LearningEvent le) {
                            if (le.getEventType() == EPOCH_ENDED) {
                                mseToChartData(neuralNetwork, testingDataSet);
                                j++;
                            } else {
                                System.out.println("        " + newLearningRate + " " + j);
                            }
                        }
                    };
                    rule.addListener(listener);
                    rule.setMaxError(0);
                    rule.setMaxIterations(MAXITERATIONS);
                    rule.setLearningRate(newLearningRate);

                    DecimalFormat df = new DecimalFormat("0.000");
                    chartData = new ChartData(df.format(newLearningRate));
                    neuralNetwork.learn(trainingDataSet, rule);
                    arrayChartData.add(chartData.clone());

                }
                LineChartSample lcs2 = new LineChartSample(arrayChartData, type.toString() + " " + firstLayer + " " + secondLayer, block);

            }

        }
        
    }
    
    private static void mseToChartData(NeuralNetwork nnet, DataSet tset) {

        Double mse = netWorkMSE(nnet, tset);
        chartData.add(mse);

    }

    private static Double netWorkMSE(NeuralNetwork nnet, DataSet tset) {
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

        return sumatorio / (2*INPUT); //Así lo hace neuroph
//        return sumatorio / (INPUT);

    }

}
