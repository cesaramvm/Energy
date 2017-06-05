package neuralnetwork.charts;

import java.util.ArrayList;
import java.util.List;

/**
 * @author César Valdés
 */
public class ChartData {

	private String learningRate;
	private String transferType;
	private int[] layersConf;
	private List<Double> graphData = new ArrayList<>();

	public ChartData(String learningRate, String transferType, int[] layersConfiguration) {
		this.learningRate = learningRate;
		this.transferType = transferType;
		this.layersConf = layersConfiguration;
	}

	public String getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(String learningRate) {
		this.learningRate = learningRate;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public int[] getLayersConf() {
		return layersConf;
	}

	public void setLayersConf(int[] layersConf) {
		this.layersConf = layersConf;
	}

	public List<Double> getGraphData() {
		return graphData;
	}

	public void setGraphData(List<Double> graphData) {
		this.graphData = graphData;
	}

	public Double get(int index) {
		return graphData.get(index);
	}

	public boolean add(Double e) {
		return graphData.add(e);
	}

	@Override
	public String toString() {

		String aux = "ChartData: LR: " + learningRate + " TF: " + transferType + "\n";
		aux = aux + graphData.toString();
		return aux;

	}

}
