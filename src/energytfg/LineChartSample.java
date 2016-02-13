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
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class LineChartSample extends Application {
    
    private static ArrayList<Double> data;
    private static String mseChartTitle;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart");
        //defining the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epochs");
        yAxis.setLabel("MSE");
        //creating the chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("MSE Chart for " + mseChartTitle);
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        for(int i=0; i<data.size();i++){
            Double d = data.get(i);
            series.getData().add(new XYChart.Data(i, d));
        }

        Scene scene = new Scene(lineChart, 1000, 600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    public void execute(ArrayList<Double> incomingData, String chartTitle) {
        data = incomingData;
        mseChartTitle = chartTitle;
        launch();
    }

}
