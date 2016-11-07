/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author sobremesa
 */
public class help {

//    public void testPerceptron() {
//
//        System.out.println("-----PERCEPTRON-----");
//
//        for (TransferFunctionType type : testTipes) {
//            System.out.print(ANSI_RED + "TEST " + TransferFunctionType.valueOf(type.toString()) + ". ");
//
//            NeuralNetwork neuralNetwork = new Perceptron(INPUT, OUTPUT, type);
//
//            long before = System.currentTimeMillis();
//            neuralNetwork.learn(trainingDataSet);
//            long time = System.currentTimeMillis() - before;
//
//            System.out.println("Terminado en " + time + " ms." + ANSI_RESET);
//
//            testNetwork(neuralNetwork, trainingDataSet);
//
//            //Para guardar:
//            String saveFile = PERCEPTRON_SAVE + TransferFunctionType.valueOf(type.toString()) + ".nnet";
//            neuralNetwork.save(saveFile);
//            //Para cargar:
//            //NeuralNetwork loadedPerceptron = NeuralNetwork.createFromFile("mySamplePerceptron.nnet");
//
//        }
//
//    }
//
//    public void testMultiLayerPerceptron() {
//
//        System.out.println("-----MULTI PERCEPTRON-----");
//
//        for (TransferFunctionType type : testTipes) {
//            System.out.print(ANSI_RED + "TEST " + TransferFunctionType.valueOf(type.toString()) + ". ");
//
//            NeuralNetwork neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.GAUSSIAN, INPUT, OUTPUT);
//
//            long before = System.currentTimeMillis();
//            neuralNetwork.learn(trainingDataSet);
//            long time = System.currentTimeMillis() - before;
//
//            System.out.println("Terminado en " + time + " ms." + ANSI_RESET);
//
//            testNetwork(neuralNetwork, trainingDataSet);
//
//            //Para guardar:
//            String saveFile = MLPERCEPTRON_SAVE + TransferFunctionType.valueOf(type.toString()) + ".nnet";
//            neuralNetwork.save(saveFile);
//            //Para cargar:
//            //NeuralNetwork loadedPerceptron = NeuralNetwork.createFromFile("mySamplePerceptron.nnet");
//
//        }
//
//    }
}
