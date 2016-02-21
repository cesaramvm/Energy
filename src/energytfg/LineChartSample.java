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
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LineChartSample {

    private static ArrayList<ChartData> data;
    private static String mseChartTitle;
    private static JFrame frame;

    public LineChartSample(ArrayList<ChartData> incomingData, String chartTitle) {
        data = incomingData;
        mseChartTitle = chartTitle;

        SwingUtilities.invokeLater(() -> {
            initAndShowGUI();
        });
        /*same as
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
        */
    }
    
    private static void initAndShowGUI() {
        // This method is invoked on the EDT thread
        frame = new JFrame(mseChartTitle);
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(1000, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Platform.runLater(() -> {
            initFX(fxPanel);
        });
        /* same as
                Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
         */
    }

    private static void initFX(JFXPanel fxPanel) {
        Scene scene = createScene();
        fxPanel.setScene(scene);
        fxPanel.setVisible(true);
    }

    private static Scene createScene() {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epochs");
        yAxis.setLabel("MSE");
        //creating the chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("MSE Chart for " + mseChartTitle);
        ArrayList<XYChart.Series<Number,Number>> arraySeries = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XYChart.Series series = new XYChart.Series();
            ChartData chartD = data.get(i);
            series.setName(chartD.getName());
            for (int j=0; j<chartD.getGraphData().size(); j++){
                series.getData().add(new XYChart.Data(j, chartD.get(j)));
            }
            arraySeries.add(series);
            
            
        }

        Scene scene = new Scene(lineChart, 1000, 600);
        lineChart.getData().addAll(arraySeries);

        return (scene);
    }
    
    private static Scene createOldScene() {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epochs");
        yAxis.setLabel("MSE");
        //creating the chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("MSE Chart for " + mseChartTitle);
        //defining a series
        ArrayList<XYChart.Series<Number,Number>> seriesArray = new ArrayList<>();
        for (ChartData cd: data){
            XYChart.Series series = new XYChart.Series();
            series.setName(cd.getName());
            for (int i=0; i<cd.getGraphData().size(); i++){
                series.getData().add(new XYChart.Data(i, cd.getGraphData().get(i)));
            }
        }

        Scene scene = new Scene(lineChart, 1000, 600);
        lineChart.getData().addAll(seriesArray);

        return (scene);
    }
    
    public void erase (){
        
        if (frame != null){
            
            frame.setVisible(false);
            
        }
        
    }

}
