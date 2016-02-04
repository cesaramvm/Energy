/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.util.ArrayList;
import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
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
            
            NeuralNetwork neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.GAUSSIAN, INPUT,OUTPUT);
            
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
            System.out.println("Input: " + Arrays.toString(dataRow.getInput()));
            System.out.println(" Output: " + Arrays.toString(networkOutput));

        }
        
        System.out.println("\n\n");

    }
    
    public void test(){
        
         NeuralNetwork neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.GAUSSIAN, 14,2,1);
         MomentumBackpropagation rule = new MomentumBackpropagation();
         rule.setLearningRate(0.5);
         rule.setMomentum(0.5);
                                    //new LMS(); <-SupervisedLearning<-IterativeLearning<-LearningRule
            //new SigmoidDeltaRule();         new SupervisedHebbianLearning();
            //new BackPropagation();
            //new MomentumBackpropagation();
         neuralNetwork.learn(trainingDataSet, rule);
         
         
    }

    private void clearTransferFunctionsNotWorking() {
        testTipes.remove(TransferFunctionType.STEP);
        testTipes.remove(TransferFunctionType.SGN);
        testTipes.remove(TransferFunctionType.LOG);
    }

}