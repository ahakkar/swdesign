package org.example.controller.factory;

import java.util.Map;

import org.example.types.AxisType;
import org.example.types.DataType;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

/**
 * Class for scatterdot chart specific methods and overrides
 * 
 * @author Antti Hakkarainen
 */
public class ScatterChartImpl extends ChartImpl {

     /**
     * Populates the Chart with provided data     * 

     * @param hideNodes  should we hide the "circle" from each data node
     */
    public XYChart<Number, Number> populateChartData(Boolean hideNodes) {

        Map<AxisType, DataType> axisMap = request.getAxisMap();
        
        try {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            
            series.setName(axisMap.get(AxisType.Y_AXIS).getDescription());
            for (int i = 0; i < 100; i++) {
                Number xValue = Math.random() * 100;
                Number yValue = Math.random() * 100;
                series.getData().add(new XYChart.Data<>(xValue, yValue));
            }

            chart.getData().add(series);

            // Only then you can apparently hide the datapoints' nodes..
            if (hideNodes) {
                for (XYChart.Series<Number, Number> s : chart.getData()) {
                    for (XYChart.Data<Number, Number> d : s.getData()) {
                        d.getNode().setVisible(false);
                    }
                }
            }

            return chart;

        } catch (Exception e) {
            // could be InstantiationException, IllegalAccessException, etc.
            e.printStackTrace();

            // Return an empty LineChart if chart creation fails
            NumberAxis xAxis = new NumberAxis();
            NumberAxis yAxis = new NumberAxis();
            return new ScatterChart<>(xAxis, yAxis);
        }
    }
}
