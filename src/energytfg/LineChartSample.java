/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

/**
 *
 * @author sobremesa
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LineChartSample {

    private ArrayList<ChartData> data;
    private String mseChartTitle;
    private JFrame frame;
    private boolean blockSentinel;

    public LineChartSample(ArrayList<ChartData> incomingData, String chartTitle, boolean block) {
        data = incomingData;
        mseChartTitle = chartTitle;
        blockSentinel = block;

        SwingUtilities.invokeLater(() -> {
            initAndShowGUI();
        });

        while (blockSentinel) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LineChartSample.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void initAndShowGUI() {
        // This method is invoked on the EDT thread
        frame = new JFrame(mseChartTitle);
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(600, 360);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                exit(true);
            }

            @Override
            public void windowIconified(WindowEvent evt) {
                exit(false);
            }
        });
        fxPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'q') {
                    exit(true);
                } else if (e.getKeyChar() == 'e') {
                    exit(false);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    exit(false);
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

        lineChart.setTitle("MSE Chart for " + mseChartTitle);
        ArrayList<XYChart.Series<Number, Number>> arraySeries = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            XYChart.Series series = new XYChart.Series();
            ChartData chartD = data.get(i);
            series.setName(chartD.getName());
            //Me quito los 4 primeros epochs porque tienen un error demasiado grande y hace que la gráfica
            //Se vea demasiado pequeña
            if(chartD.getGraphData().size() > 100){
                for (int j = 4; j < chartD.getGraphData().size(); j++) {
                    if (j%(chartD.getGraphData().size()/100) == 0){
                    series.getData().add(new XYChart.Data(j, chartD.get(j)));
                    }
                }
            } else{
                for (int j = 4; j < chartD.getGraphData().size(); j++) {
                    series.getData().add(new XYChart.Data(j, chartD.get(j)));
                }
            }
                
            arraySeries.add(series);

        }

        Scene scene = new Scene(lineChart, 1000, 600);
        lineChart.getData().addAll(arraySeries);

        return (scene);
    }

    private void exit(boolean close) {
        if(blockSentinel){
            blockSentinel = false;
        }
        if (close) {
            frame.dispose();
        }

    }
}
