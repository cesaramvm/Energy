package neuralnetwork;

import global.GlobalConstants;
import util.Normalizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.abs;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

/**
 * @author César Valdés
 */
public class NeurophSolver extends GlobalConstants {

	private final ArrayList<Integer[]> neuronsConfig = new ArrayList<>();
	private NeurophSearch neurophSearch;
	private final static String CSV_SAVES = "NeurophSolutions/";
	private final static String NET_SAVES = "NeurophSolutions/Networks/";

	public void simpleSearch(int iterations, Class<? extends Object> propagationTypeClass, int numLines,
			double learningRate, TransferFunctionType transfer, int[] hiddenLayers, boolean showGraph,
			String fileName) {

		neurophSearch = new NeurophSearch(iterations, propagationTypeClass, showGraph, TRAINPATH, TESTPATH, fileName,
				NET_SAVES, CSV_SAVES);
		neurophSearch.singlePlot(numLines, learningRate, transfer, hiddenLayers);
		try {
			neurophSearch.writeRows();
			neurophSearch.closeTableWriter();
		} catch (Exception ex) {
			Logger.getLogger(NeurophSolver.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void advancedLRSearch(int iterations, Class<? extends Object> propagationTypeClass, List<Double> lrates,
			TransferFunctionType transfer, int maxHiddenLayers, Integer[] neuronsInLayers, boolean showGraph,
			String fileName) {

		for (int i = 1; i <= maxHiddenLayers; i++) {
			neuronsConfig.clear();
			int[] combination = new int[i + 2];
			combination[0] = 14;
			combination[i + 2 - 1] = 1;
			createCombinations(i, neuronsInLayers, new ArrayList<>());
			for (Integer[] comb : neuronsConfig) {
				for (int j = 1; j < i + 2 - 1; j++) {
					combination[j] = comb[j - 1];
				}
				neurophSearch = new NeurophSearch(iterations, propagationTypeClass, showGraph, TRAINPATH, TESTPATH,
						fileName, NET_SAVES, CSV_SAVES);
				neurophSearch.onePlotLRs(lrates, transfer, combination);

				try {
					neurophSearch.writeRows();
				} catch (Exception ex) {
					Logger.getLogger(NeurophSolver.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

		}
		neurophSearch.closeTableWriter();
	}

	public void findBestNetwork() {
		DataSet allDataset = DataSet.createFromFile(FULLPATH, 14, 1, ";");
		File folder = new File(NET_SAVES);
		File[] listOfFiles = folder.listFiles();
		String bestNetworkFile = "";
		Double minMeanError = Double.POSITIVE_INFINITY;
		Double minError = Double.POSITIVE_INFINITY;
		for (File f : listOfFiles) {
			if (f.getName().toLowerCase().endsWith(".nnet")) {
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
		}

		if (bestNetworkFile == "") {
			String resultado = "No se han encontrado archivos de guardado de redes neuronales (.nnet) en la carpeta "
					+ NET_SAVES;
			JOptionPane msg = new JOptionPane(resultado, JOptionPane.INFORMATION_MESSAGE);
			final JDialog dlg = msg.createDialog("Ningúna red encontrada");
			dlg.setVisible(true);
		} else {
			String resultado = "La mejor red es: \n" + bestNetworkFile + "\n" + "Con un error minimo de " + minError
					+ "\n" + "Y un error medio de " + minMeanError;
			JOptionPane msg = new JOptionPane(resultado, JOptionPane.INFORMATION_MESSAGE);
			final JDialog dlg = msg.createDialog("Mejor red neuronal");
			dlg.setVisible(true);
		}

	}

	public void networkTest(String inputFile, String outputFile) {
		if (!inputFile.endsWith(".nnet")) {
			inputFile += ".nnet";
		}
		outputFile = CSV_SAVES + outputFile + ".csv";
		inputFile = NET_SAVES + inputFile;
		DataSet allDataset = DataSet.createFromFile(FULLPATH, 14, 1, ";");
		try {
			FileWriter fw = new FileWriter(outputFile, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println("TABLA FINAL; OBJETIVO; RESULTADO; ERROR");
			NeuralNetwork<?> neuralNetwork = NeuralNetwork.createFromFile(inputFile);
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
