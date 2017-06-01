package NeuralNetwork;

import Util.Normalizer;

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
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.TransferFunctionType;

import Global.GlobalConstants;

/**
 * @author César Valdés
 */
public class NeurophSolver extends GlobalConstants {

	private static final ArrayList<Integer[]> neuronsConfig = new ArrayList<>();
	private static final ArrayList<TransferFunctionType> TYPES = new ArrayList<>(
			Arrays.asList(TransferFunctionType.SIN, TransferFunctionType.TANH, TransferFunctionType.GAUSSIAN));
	private NeurophSearch neurophSearch;

	public void simpleSearch(int iterations, Class<? extends Object> propagationTypeClass,
			int numLines, double learningRate, TransferFunctionType transfer, int[] hiddenLayers, boolean showGraph, String fileName) {
		
		neurophSearch = new NeurophSearch(iterations, propagationTypeClass, showGraph,
				TRAINPATH, TESTPATH, fileName);
		neurophSearch.singlePlot(numLines, learningRate, transfer, hiddenLayers);
		try {
			neurophSearch.writeRows();
			neurophSearch.closeTableWriter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fullSearch() {
		boolean showGraph = true;
		// BackPropagation or ResilientPropagation
		neurophSearch = new NeurophSearch(15000, ResilientPropagation.class, showGraph,
				TRAINPATH, TESTPATH, "AllTest.csv");
		// neurophSearch.onePlot(5, 0.3, TransferFunctionType.GAUSSIAN, 6, 0,
		// NeurophModule.Rprop);
		ArrayList<Double> lrates = new ArrayList<>(Arrays.asList(0.2, 0.3, 0.4));

		Integer[] possibleNeurons = { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
		Integer[] possible3Neurons = { 6, 7, 8, 9, 10, 11, 12, 13, 14 };
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
						neurophSearch.onePlotLRs(lrates, type, combination);
						try {
							neurophSearch.writeRows();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					System.out.println("Finished " + Arrays.toString(combination));

				}

			}

		}
	}


	public void findBestNetwork() {
		DataSet allDataset = DataSet.createFromFile(FULLPATH, 14, 1, ";");
		String networkDir = "TemporalNets/";
		File folder = new File(networkDir);
		File[] listOfFiles = folder.listFiles();
		String bestNetworkFile = "";
		Double minMeanError = Double.POSITIVE_INFINITY;
		Double minError = Double.POSITIVE_INFINITY;
		for (File f : listOfFiles) {
			Double networkSumError = 0.0;
			Double maxErrorNetwork = Double.NEGATIVE_INFINITY;
			NeuralNetwork<?> neuralNetwork = NeuralNetwork.createFromFile(f.toString());
			// Para sacar el test de cada uno
			// networkTest(f.toString(), problem, f.getName().substring(0,
			// f.getName().length()-5) + ".csv");
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

			// if (maxErrorNetwork < minError) {
			// minError = maxErrorNetwork;
			// minMeanError = meanError;
			// bestNetworkFile = f.toString();
			// }
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

	public void networkTest(String fileRoute, String saveRoute) {
		DataSet allDataset = DataSet.createFromFile(FULLPATH, 14, 1, ";");

		try {
			FileWriter fw = new FileWriter(saveRoute, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println("TABLA FINAL; OBJETIVO; RESULTADO; ERROR");
			NeuralNetwork<?> neuralNetwork = NeuralNetwork.createFromFile(fileRoute);
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
				pw.println(";" + ERROR_DF.format(deseado) + ";" + ERROR_DF.format(calculado) + ";"
						+ ERROR_DF.format(error) + ";");

			}

			pw.close();
		} catch (IOException ex) {
			Logger.getLogger(NeurophSolver.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void createCombinations(int maxLength, Integer[] possibleNeurons, ArrayList<Integer> curr) {
		if (curr.size() == maxLength) {
			Integer[] newConfig = new Integer[maxLength];
			for (int i = 0; i < curr.size(); i++) {
				newConfig[i] = curr.get(i);
			}
			neuronsConfig.add(newConfig);
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
