package org.example.contoller.factory;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.example.model.data.DataPoint;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;

public class AreaChartImpl extends AreaChart<Number, Number> {

    private XYChart<Number, Number> chart;
    private ChartParams params;

    public AreaChartImpl() {
        super(new NumberAxis(), new NumberAxis());
    }

    /**
     * Generates a line chart and displays it on screen
     */
    public XYChart<Number, Number> createChart(ChartParams params) {       
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new AreaChart<>(xAxis, yAxis);

        this.params = params;

        // Populate the empty chart with data fetched from API
        chart = populateChartData(true);

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

    System.out.println("calling chart creation method");

        try {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            
            series.setName(params.getChartDesc());
            int i = 0;

            // First populate the chart with datapoints
            for (DataPoint dataPoint : params.getDataPoints()) {
                Number xValue = i;
                Number yValue = dataPoint.getValue();
                series.getData().add(new XYChart.Data<>(xValue, yValue));
                i++;
            }

            // Customize the x-axis labels
            if (chart.getXAxis() instanceof NumberAxis) {
                NumberAxis xAxis = (NumberAxis) chart.getXAxis();

                xAxis.setTickLabelFormatter(new StringConverter<Number>() {
                    @Override
                    public String toString(Number number) {
                        int index = number.intValue();
                        if (index < 0 || index >= params.getDataPoints().size()) {
                            return ""; 
                        }

                        if (index % 2 == 0) {    
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                            OffsetDateTime dateTime = OffsetDateTime.parse(params.getDataPoints().get(index).getStartTime(), formatter);

                            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));   
                        }
                        
                        return "";
                    }

                    @Override
                    public Number fromString(String string) {
                        return 0;
                    }
                });
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
