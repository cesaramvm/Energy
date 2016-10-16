/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChartPackage;

/**
 *
 * @author sobremesa
 */
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
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LineChartSample {

    private static ArrayList<ChartData> data;
    private static String mseChartTitle;
    private static JFrame frame;

    public LineChartSample(ArrayList<ChartData> incomingData, String chartTitle) {
        try {
            data = incomingData;
            mseChartTitle = chartTitle;

            SwingUtilities.invokeLater(() -> {
                initAndShowGUI();
            });

            Thread.sleep(2000);
            makeScreenshot(frame);
            frame.dispose();
        } catch (InterruptedException ex) {
            Logger.getLogger(LineChartSample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initAndShowGUI() {
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
                }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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
            XYChart.Series series = new XYChart.Series();
            ChartData chart = data.get(i);
            series.setName(chart.getLearningRate() + " " + chart.getTransferType().substring(0, 2));
            //Me quito los 4 primeros epochs porque tienen un error demasiado grande y hace que la gráfica
            //Se vea demasiado pequeña
            if (chart.getGraphData().size() > 20) {
                for (int j = 4; j < chart.getGraphData().size(); j++) {
                    if (j % (chart.getGraphData().size() / 100) == 0) {
                        series.getData().add(new XYChart.Data(j, chart.get(j)));
                    }
                }
            } else {
                for (int j = 4; j < chart.getGraphData().size(); j++) {
                    series.getData().add(new XYChart.Data(j, chart.get(j)));
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
        BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        argFrame.paint(bufferedImage.getGraphics());

        try {
            // Create temp file.
            File file = new File("NetworkSaves/Graphs/" + mseChartTitle.replace(":", "-") + ".png");
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException ioe) {
        } // catch
    }
}
