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
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
        for (int i = 0; i < data.size(); i++) {
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
    
    
    
    
    
    
    
    
    
    
    
    
    

    private static void initAndShowGUI() {
        // This method is invoked on the EDT thread
        JFrame frame = new JFrame("Swing and JavaFX");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(1000, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
        fxPanel.setVisible(true);
    }

    private static Scene createScene() {
//        Group  root  =  new  Group();
//        Scene  scene  =  new  Scene(root, Color.ALICEBLUE);
//        Text  text  =  new  Text();
//        
//        text.setX(40);
//        text.setY(100);
//        text.setFont(new Font(25));
//        text.setText("Welcome JavaFX!");
//
//        root.getChildren().add(text);

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
        for (int i = 0; i < data.size(); i++) {
            Double d = data.get(i);
            series.getData().add(new XYChart.Data(i, d));
        }

        Scene scene = new Scene(lineChart, 1000, 600);
        lineChart.getData().add(series);

        return (scene);
    }

    public static void main(ArrayList<Double> incomingData, String chartTitle) {
        data = incomingData;
        mseChartTitle = chartTitle;
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }

}
