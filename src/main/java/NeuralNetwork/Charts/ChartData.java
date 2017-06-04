package NeuralNetwork.Charts;

import java.util.ArrayList;

/**
 * @author César Valdés
 */
public class ChartData implements Cloneable {

	private String learningRate;
	private String transferType;
	private int[] layersConf;
	private ArrayList<Double> graphData = new ArrayList<>();

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

	public ArrayList<Double> getGraphData() {
		return graphData;
	}

	public void setGraphData(ArrayList<Double> graphData) {
		this.graphData = graphData;
	}

	public Double get(int index) {
		return graphData.get(index);
	}

	public boolean add(Double e) {
		return graphData.add(e);
	}

	@Override
	public ChartData clone() throws CloneNotSupportedException {

		ChartData clone;
		try {
			clone = (ChartData) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error();
		}

		return clone;

	}

	@Override
	public String toString() {

		String aux = "ChartData: LR: " + learningRate + " TF: " + transferType + "\n";
		aux = aux + graphData.toString();
		return aux;

	}

}
