package org.example.factory;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class ScatterChartImpl extends ScatterChart<Number, Number> {

    private XYChart<Number, Number> chart;
    private ChartParams params;


    public ScatterChartImpl() {
        super(new NumberAxis(), new NumberAxis());
    }

    /**
     * Generates a line chart and displays it on screen
     */
    public XYChart<Number, Number> createChart(ChartParams params) { 
        
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        chart = new ScatterChart<>(xAxis, yAxis);
        this.params = params;

        chart = populateChartData(false);

        updateChartAxisLabels();   
        
        return chart;
    }

    
    /**
     * Updates chart axis based on user choice
     */
    public void updateChartAxisLabels() {
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        NumberAxis yAxis = (NumberAxis) chart.getYAxis();

        xAxis.setLabel(params.getXAxisDesc());
        yAxis.setLabel(params.getYAxisDesc());
    } 

     /**
     * Populates the Chart with provided data
     * 
     * @param chartType  The class of chart to create, e.g., LineChart.class or AreaChart.class
     * @param chartDescription   The type of data, only used for setting the series name
     * @param dataPoints List of data nodes from which the chart is created
     * @param hideNodes  should we hide the "circle" from each data node
     */
    public XYChart<Number, Number> populateChartData(Boolean hideNodes) {

        try {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            
            series.setName(params.getChartDesc());
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
