package neuralnetwork.charts;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * @author César Valdés
 */
public class LineChartSample extends Thread {

	private List<ChartData> data;
	private String mseChartTitle;
	private JFrame frame;
	private static final String GRAPH_SAVES = "NeurophSolutions/Graphs/";

	public LineChartSample(List<ChartData> incomingData, String chartTitle) {
		data = incomingData;
		mseChartTitle = chartTitle;
	}

	@Override
	public void run() {
		try {
			SwingUtilities.invokeLater(() -> showGUI());
			Long sleepTime = (long) (1500 + 20 * data.size());
			Thread.sleep(sleepTime);
			makeScreenshot(frame);
			frame.setVisible(false);
			frame.dispose();
		} catch (InterruptedException ex) {
			Logger.getLogger(LineChartSample.class.getName()).log(Level.SEVERE, null, ex);
			Thread.currentThread().interrupt(); 
		}
	}

	private void showGUI() {
		frame = new JFrame(mseChartTitle);
		final JFXPanel fxPanel = new JFXPanel();
		frame.add(fxPanel);
		frame.setSize(600, 360);
		frame.setVisible(true);
		Platform.setImplicitExit(false);
		Platform.runLater(() -> initFX(fxPanel));
	}

	private void initFX(JFXPanel fxPanel) {
		Scene scene = createScene();
		fxPanel.setScene(scene);
		fxPanel.setVisible(true);
	}

	private Scene createScene() {
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Epochs");
		yAxis.setLabel("MSE");
		LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setCreateSymbols(false);
		lineChart.setTitle("MSE Chart for " + mseChartTitle);
		ArrayList<XYChart.Series<Number, Number>> arraySeries = new ArrayList<>();

		for (int i = 0; i < data.size(); i++) {
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			ChartData chart = data.get(i);
			series.setName(chart.getLearningRate() + " " + chart.getTransferType().substring(0, 2));
			if (chart.getGraphData().size() > 20) {
				for (int j = 4; j < chart.getGraphData().size(); j++) {
					if (j % (chart.getGraphData().size() / 100) == 0) {
						series.getData().add(new XYChart.Data<Number, Number>(j, chart.get(j)));
					}
				}
			} else {
				for (int j = 4; j < chart.getGraphData().size(); j++) {
					series.getData().add(new XYChart.Data<Number, Number>(j, chart.get(j)));
				}
			}

			arraySeries.add(series);

		}

		Scene scene = new Scene(lineChart, 1000, 600);
		lineChart.getData().addAll(arraySeries);
		return scene;
	}

	public void makeScreenshot(JFrame argFrame) {
		Rectangle rec = argFrame.getBounds();
		BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
		argFrame.paint(bufferedImage.getGraphics());

		try {
			File file = new File(GRAPH_SAVES + mseChartTitle.replace(":", "-") + ".png");
			ImageIO.write(bufferedImage, "png", file);
		} catch (IOException ex) {
			Logger.getLogger(LineChartSample.class.getName()).log(Level.SEVERE, null, ex);
			
		}
	}
}
