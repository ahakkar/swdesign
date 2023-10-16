package org.example.controller.factory;

import java.util.Map;

import org.example.model.data.AbstractDataModel;
import org.example.model.data.ChartRequest;
import org.example.types.AxisType;
import org.example.types.DataType;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Common abstract class for chart implementations. Holds common methods for
 * all chart types. XYChart and PieChart specific subclasses extend this class.
 * 
 * @author Antti Hakkarainen
 */
public abstract class ChartImpl
{
    protected XYChart<Number, Number> chart;
    protected AbstractDataModel<Double> data;
    protected ChartRequest request;

    public ChartImpl() {}

    /**
     * Generates a line chart and displays it on screen
     * @param params DTO for data and chart creation parameters
     * @return       Finished Chart object ready to be added to UI  
     * 
     * // TODO refactor to use Chart instead of XYChart  
     */
    public XYChart<Number, Number> createXYChart(
        ChartParams params
    ) {       
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new AreaChart<>(xAxis, yAxis);

        this.data = params.getData();
        this.request = params.getRequest();

        // Populate the empty chart with data fetched from API
        chart = populateXYChartData(true);

        updateXYAxisLabels();   
        
        return chart;
    }

    
    /**
     * Updates chart axis based on user choice
     */
    public void updateXYAxisLabels() {
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        NumberAxis yAxis = (NumberAxis) chart.getYAxis();

        Map<AxisType, DataType> axisMap = request.getAxisMap();
        axisMap.get(AxisType.X_AXIS).toString();

        xAxis.setLabel(axisMap.get(AxisType.X_AXIS).toString());
        yAxis.setLabel(axisMap.get(AxisType.Y_AXIS).toString());
    } 

     /**
     * Populates the Chart object with provided data
     * 
     * @param hideNodes  boolean for hiding the datapoints' nodes
     * 
     * //TODO refactor to use Chart instead of XYChart (possibly move this to a XYChartImpl subclass)
     */
    public XYChart<Number, Number> populateXYChartData(Boolean hideNodes) {

        Map<AxisType, DataType> axisMap = request.getAxisMap();

        try {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            
            // Assume that Y_AXIS is the value we want to plot
            series.setName(axisMap.get(AxisType.Y_AXIS).getDescription());
            int i = 0;

            // First populate the chart with datapoints
            for (Map.Entry<String, Double> entry : data.getDataPoints().entrySet()) {
                Number xValue = i; // TODO use some actual value here
                Number yValue = entry.getValue();
                series.getData().add(new XYChart.Data<>(xValue, yValue));
                i++;
            }

            chart.getData().clear();
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
            return new AreaChart<>(xAxis, yAxis);
        }
    }
}