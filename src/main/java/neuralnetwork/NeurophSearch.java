package neuralnetwork;

import static org.neuroph.core.events.LearningEvent.Type.EPOCH_ENDED;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.util.TransferFunctionType;

import neuralnetwork.charts.ChartData;
import neuralnetwork.charts.LineChartSample;
import util.CSVTableWriter;

/**
 * @author César Valdés
 */
public class NeurophSearch {

	private static final int INPUT = 14;
	private static final int OUTPUT = 1;
	private static final DecimalFormat LR_FORMAT = new DecimalFormat("0.00");
	private static final DecimalFormat ERROR_FORMAT = new DecimalFormat("0.00000");
	

	private String csvSaves;
	private String netSaves;
	private DataSet trainingDataSet;
	private DataSet testingDataSet;
	private Class<? extends Object> propagationClass;

	private int maxIterations;
	private boolean showGraph;

	private ArrayList<ChartData> graphTestData = new ArrayList<>();
	private ChartData chartTestData;
	private CSVTableWriter tableWriter;
	private ArrayList<Integer> columnIndexes = new ArrayList<>();
	private LineChartSample chartTesting;

	public NeurophSearch(int iterations, Class<? extends Object> newPropagationClass, boolean graphShow, 
			String trainingFile, String testingFile, String writerPath, String netSaves, String csvSaves) {
		this.netSaves = netSaves;
		this.csvSaves = csvSaves;
		this.maxIterations = iterations;
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
		}
		String graphName = "Test TF:" + transferType.toString() + " LR:" + learningRate.toString() + " "
				+ Arrays.toString(layers);
		this.graphProcedure(graphName);
	}

	public void onePlotLRs(List<Double> learningRates, TransferFunctionType transferType, int[] layers) {
		clearAll();
		for (Double learningRate : learningRates) {
			this.train(learningRate, transferType, layers);
		}
		String graphName = "Test TF:" + transferType.toString() + " " + Arrays.toString(layers);
		this.graphProcedure(graphName);
	}

	public void onePlotTFs(Double learningRate, List<TransferFunctionType> transferTypes, int[] layers) {
		clearAll();
		for (TransferFunctionType transferType : transferTypes) {
			train(learningRate, transferType, layers);
		}
		String graphName = "Test LR:" + learningRate.toString() + " " + Arrays.toString(layers);
		this.graphProcedure(graphName);
	}
	
	private void graphProcedure(String title){
		
		if (showGraph) {
			chartTesting = new LineChartSample(new ArrayList<>(graphTestData), title);
			chartTesting.start();
		}
		
	}

	private void train(Double learningRate, TransferFunctionType transferType, int[] layers) {
		try {
			NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(transferType, layers);

			Constructor<?> cons = propagationClass.getConstructor();
			LMS rule = (LMS) cons.newInstance();
			LearningEventListener listener = createListener(neuralNetwork, learningRate, transferType.toString(),
					layers);
			rule.addListener(listener);
			rule.setMaxError(0);
			rule.setMaxIterations(maxIterations);
			rule.setLearningRate(learningRate);
			neuralNetwork.learn(trainingDataSet, (BackPropagation) rule);
			Double mse = netWorkMSE(neuralNetwork, testingDataSet);
			neuralNetwork.save(netSaves + ERROR_FORMAT.format(mse) + "-Network-"
					+ transferType.toString().substring(0, 2) + "-" + learningRate + "-" + Arrays.toString(layers)
					+ ".nnet");

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException ex) {
			Logger.getLogger(NeurophSearch.class.getName()).log(Level.SEVERE, null, ex);
		}
		graphTestData.add(chartTestData);
	}

	private LearningEventListener createListener(NeuralNetwork<? extends LearningRule> neuralNetwork,
			Double learningRate, String transferType, int[] layers) {
		chartTestData = new ChartData(LR_FORMAT.format(learningRate), transferType, layers);
		return (LearningEvent le) -> {
			if (le.getEventType() == EPOCH_ENDED) {
				mseToChartData(neuralNetwork, testingDataSet, chartTestData);
			}
		};
	}

	private void mseToChartData(NeuralNetwork<? extends LearningRule> neuralNetwork, DataSet dataSet,
			ChartData chartData) {

		Double mse = netWorkMSE(neuralNetwork, dataSet);
		chartData.add(mse);

	}

	public Double netWorkMSE(NeuralNetwork<? extends LearningRule> nnet, DataSet tset) {
		// https://github.com/neuroph/neuroph/blob/master/neuroph-2.9/Core/src/main/java/org/neuroph/core/learning/error/MeanSquaredError.java
		Double sumatorio = 0.0;

		for (DataSetRow dataRow : tset.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			double[] desiredOut = dataRow.getDesiredOutput();
			double errorParcial = networkOutput[0] - desiredOut[0];
			double sumaux = errorParcial * errorParcial;
			sumatorio = sumatorio + sumaux;
		}

		return sumatorio / (2 * INPUT);

	}

	private void clearAll() {

		chartTestData = null;
		graphTestData.clear();
	}

	public void writeRows() {
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

	private void writeRow(ArrayList<String> nextRow) {
		try {
			tableWriter.printRow(nextRow);
		} catch (Exception ex) {
			Logger.getLogger(NeurophSearch.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public CSVTableWriter initTableWriter(String path) {
		String realpath = csvSaves;
		realpath += path;
		ArrayList<String> tableHeaders = new ArrayList<>();
		tableHeaders.addAll(Arrays.asList("Conf", "Neurons"));
		tableHeaders.add(Integer.toString(0));
		columnIndexes.add(0);
		int column = maxIterations / 10;
		for (int i = 1; i <= 10; i++) {
			int columnNumber = column * (i) - 1;
			tableHeaders.add(Integer.toString(columnNumber));
			columnIndexes.add(columnNumber);
		}
		CSVTableWriter tw = null;
		try {
			tw = new CSVTableWriter(realpath, tableHeaders);
		} catch (Exception ex) {
			Logger.getLogger(NeurophSearch.class.getName()).log(Level.SEVERE, null, ex);
		}

		return tw;
	}


}
