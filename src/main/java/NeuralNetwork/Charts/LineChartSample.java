package NeuralNetwork.Charts;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author César Valdés
 */
public class LineChartSample extends Thread {

    private static ArrayList<ChartData> data;
    private static String mseChartTitle;
    private static JFrame frame;
    private static final String GRAPH_SAVES = "NeurophSolutions/Graphs/";

    public LineChartSample(ArrayList<ChartData> incomingData, String chartTitle) {

        data = incomingData;
        mseChartTitle = chartTitle;
    }
    
    public void run(){
    	
    	try {

            SwingUtilities.invokeLater(() -> {
                showGUI();

            });

            Thread.sleep(1500);
            makeScreenshot(frame);
            frame.setVisible(false);
            frame.dispose();
        } catch (InterruptedException ex) {
            Logger.getLogger(LineChartSample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void showGUI() {
        // This method is invoked on the EDT thread
        frame = new JFrame(mseChartTitle);
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(600, 360);
        frame.setVisible(true);
        fxPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'q') {
                    frame.dispose();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
        Platform.runLater(() -> {
            initFX(fxPanel);
        });
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
        //creating the chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setCreateSymbols(false);
        lineChart.setTitle("MSE Chart for " + mseChartTitle);
        ArrayList<XYChart.Series<Number, Number>> arraySeries = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            ChartData chart = data.get(i);
            series.setName(chart.getLearningRate() + " " + chart.getTransferType().substring(0, 2));
            //Me quito los 4 primeros epochs porque tienen un error demasiado grande y hace que la grÃ¡fica
            //Se vea demasiado pequeÃ±a
            if (chart.getGraphData().size() > 20) {
                for (int j = 4; j < chart.getGraphData().size(); j++) {
                    if (j % (chart.getGraphData().size() / 100) == 0) {
						Data <Number,Number> data= new XYChart.Data<Number,Number>(j, chart.get(j));
                        series.getData().add(data);
                    }
                }
            } else {
                for (int j = 4; j < chart.getGraphData().size(); j++) {
					Data<Number, Number> data = new XYChart.Data<Number,Number>(j, chart.get(j));
                    series.getData().add(data);
                }
            }

            arraySeries.add(series);

        }

        Scene scene = new Scene(lineChart, 1000, 600);
        lineChart.getData().addAll(arraySeries);

        return (scene);
    }

    public static final void makeScreenshot(JFrame argFrame) {
        Rectangle rec = argFrame.getBounds();
       /* while(argFrame.getState()==0){

            System.out.println(argFrame.getState());
        }*/
        BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        argFrame.paint(bufferedImage.getGraphics());

        try {
            // Create temp file.
            File file = new File(GRAPH_SAVES + mseChartTitle.replace(":", "-") + ".png");
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException ioe) {
        } // catch
    }
}
