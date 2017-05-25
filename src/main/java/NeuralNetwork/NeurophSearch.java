package NeuralNetwork;

import Util.CSVTableWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.TransferFunctionType;

import NeuralNetwork.Charts.ChartData;
import NeuralNetwork.Charts.LineChartSample;

/**
 * @author César Valdés
 */
public class NeurophSearch {

	private final static int INPUT = 14;
	private final static int OUTPUT = 1;
	private final static DecimalFormat LR_FORMAT = new DecimalFormat("0.00");
	private final static DecimalFormat ERROR_FORMAT = new DecimalFormat("0.00000");
	private final static String CSV_SAVES = "NeurophSolutions/";
	private final static String NET_SAVES = "NeurophSolutions/Networks/";

	private final DataSet trainingDataSet;
	private final DataSet testingDataSet;
	private final Class<? extends Object> propagationClass;

	private final int MAXITERATIONS;
	private final boolean showGraph;

	private final ArrayList<ChartData> graphTestData = new ArrayList<>();
	private ChartData chartTestData;
	private CSVTableWriter tableWriter;
	private ArrayList<Integer> columnIndexes = new ArrayList<>();
	private LineChartSample chartTesting;

	public NeurophSearch(int iterations, Class<? extends Object> newPropagationClass,
			boolean graphShow, String trainingFile, String testingFile, String writerPath) {
		this.MAXITERATIONS = iterations;
		this.showGraph = graphShow;
		this.trainingDataSet = DataSet.createFromFile(trainingFile, INPUT, OUTPUT, ";");
		this.testingDataSet = DataSet.createFromFile(testingFile, INPUT, OUTPUT, ";");
		this.propagationClass = newPropagationClass;
		CSVTableWriter writer = initTableWriter(writerPath);
		this.tableWriter = writer;
	}

	public void singlePlot(int linesNum, Double learningRate, TransferFunctionType transferType, int[] layers) {
		clearAll();
		for (int i = 0; i < linesNum; i++) {
			this.train(learningRate, transferType, layers);
			graphTestData.add(chartTestData);
		}

		if (showGraph) {
			String graphName = "Test TF:" + transferType.toString() + " LR:" + learningRate.toString() + " "
					+ Arrays.toString(layers);
			chartTesting = new LineChartSample(new ArrayList<>(graphTestData), graphName);
			chartTesting.start();
		}

	}

	public void onePlotLRs(ArrayList<Double> learningRates, TransferFunctionType transferType, int[] layers) {
		clearAll();
		for (Double learningRate : learningRates) {
			this.train(learningRate, transferType, layers);
			graphTestData.add(chartTestData);

		}
		if (showGraph) {
			String graphName = "Test TF:" + transferType.toString() + " " + Arrays.toString(layers);
			new LineChartSample(new ArrayList<>(graphTestData), graphName);
		}

	}

	public void onePlotTFs(Double learningRate, ArrayList<TransferFunctionType> transferTypes, int[] layers) {
		clearAll();
		for (TransferFunctionType transferType : transferTypes) {
			train(learningRate, transferType, layers);
			graphTestData.add(chartTestData);

		}

		if (showGraph) {
			String graphName = "Test LR:" + learningRate.toString() + " " + Arrays.toString(layers);
			new LineChartSample(new ArrayList<>(graphTestData), graphName);
		}

	}

	private void train(Double learningRate, TransferFunctionType transferType, int[] layers) {
		try {

			if (layers.length == 0 || learningRate == 0) {
				throw new Error("Learning rate can't be 0 and layers must contain something");
			}
			NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(transferType, layers);

			Constructor<?> cons = propagationClass.getConstructor();
			LMS rule = (LMS) cons.newInstance();
			LearningEventListener listener = createListener(neuralNetwork, learningRate, transferType.toString(),
					layers);
			rule.addListener(listener);
			rule.setMaxError(0);
			rule.setMaxIterations(MAXITERATIONS);
			rule.setLearningRate(learningRate);
			neuralNetwork.learn(trainingDataSet, (BackPropagation) rule);
			Double mse = netWorkMSE(neuralNetwork, testingDataSet);
			neuralNetwork.save(NET_SAVES + ERROR_FORMAT.format(mse) + "-Network-"
					+ transferType.toString().substring(0, 2) + "-" + learningRate + "-" + Arrays.toString(layers)
					+ ".nnet");

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException ex) {
			Logger.getLogger(NeurophSearch.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private LearningEventListener createListener(NeuralNetwork<? extends LearningRule> neuralNetwork,
			Double learningRate, String transferType, int[] layers) {
		chartTestData = new ChartData(LR_FORMAT.format(learningRate), transferType, layers);
		LearningEventListener listener = (LearningEvent le) -> {
			if (le.getEventType() == EPOCH_ENDED) {
				mseToChartData(neuralNetwork, testingDataSet, chartTestData);
			} else {
			}
		};
		return listener;
	}

	private void mseToChartData(NeuralNetwork<? extends LearningRule> neuralNetwork, DataSet dataSet,
			ChartData chartData) {

		Double mse = netWorkMSE(neuralNetwork, dataSet);
		chartData.add(mse);

	}

	public Double netWorkMSE(NeuralNetwork<? extends LearningRule> nnet, DataSet tset) {
		// https://github.com/neuroph/neuroph/blob/master/neuroph-2.9/Core/src/main/java/org/neuroph/core/learning/error/MeanSquaredError.java
		// ellos hacen ((real-calculado)Â²)/2*n Â¿Â¿??
		Double sumatorio = 0.0;

		for (DataSetRow dataRow : tset.getRows()) {

			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			double[] desiredOut = dataRow.getDesiredOutput();
			double errorParcial = networkOutput[0] - desiredOut[0];
			// System.out.println("Output: " +
			// normalizer.denormalizeObjective(networkOutput[0]));
			// System.out.println("Desired: " +
			// normalizer.denormalizeObjective(desiredOut[0]));
			// System.out.println("Error: " + errorParcial);
			// double errorParcial =
			// normalizer.denormalizeObjective(networkOutput[0]) -
			// normalizer.denormalizeObjective(desiredOut[0]);

			double sumaux = errorParcial * errorParcial;
			sumatorio = sumatorio + sumaux;

			// System.out.println("Error parcial: " + errorParcial);
			// System.out.print("Desired: " + desiredOut[0] + " ");
			// System.out.println("Input: " +
			// Arrays.toString(dataRow.getInput()));
			// System.out.println(" Output: " + networkOutput[0]);
		}

		return sumatorio / (2 * INPUT);

	}

	private void clearAll() {

		chartTestData = null;
		graphTestData.clear();
	}

	public void writeRows() throws Exception {
		for (int j = 0; j < graphTestData.size(); j++) {
			ChartData trainChart = graphTestData.get(j);
			ArrayList<String> nextRow = new ArrayList<>();
			nextRow.add(trainChart.getTransferType().substring(0, 2) + " " + trainChart.getLearningRate());
			nextRow.add(Arrays.toString(graphTestData.get(0).getLayersConf()));
			
			for (Integer columnIndex : columnIndexes) {
				String error = ERROR_FORMAT.format(trainChart.get(columnIndex));
				nextRow.add(error);
			}
			this.writeRow(nextRow);
		}
	}

	public void closeTableWriter() {
		tableWriter.close();

	}

	private void writeRow(ArrayList<String> nextRow) throws Exception {
		if (tableWriter == null) {
			throw new Exception("tableWriter cannot be null");
		}
		try {
			tableWriter.printRow(nextRow);
		} catch (Exception e) {
			System.err.println("EXCEPCION CAPTURADA");
		}
	}

	public CSVTableWriter initTableWriter(String path) {
		String realpath = CSV_SAVES;
		realpath += path;
		ArrayList<String> tableHeaders = new ArrayList<>();
		tableHeaders.addAll(Arrays.asList("Conf", "Neurons"));
		tableHeaders.add(Integer.toString(0));
		columnIndexes.add(0);
		int column = MAXITERATIONS / 10;
		for (int i = 1; i <= 10; i++) {
			int columnNumber = column * (i) - 1;
			tableHeaders.add(Integer.toString(columnNumber));
			columnIndexes.add(columnNumber);
		}
		CSVTableWriter tw = null;
		try {
			tw = new CSVTableWriter(realpath, tableHeaders);
		} catch (Exception e) {

		}

		return tw;
	}


}
