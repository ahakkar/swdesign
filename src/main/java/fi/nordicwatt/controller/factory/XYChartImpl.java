package fi.nordicwatt.controller.factory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import fi.nordicwatt.model.datamodel.ResponseBundle;
import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.types.AxisType;
import fi.nordicwatt.types.DataType;
import javafx.scene.chart.*;
import javafx.util.converter.NumberStringConverter;

/**
 * XYChart specific methods
 * 
 * @author Antti Hakkarainen
 */
public class XYChartImpl extends ChartImpl {

    private XYChart<Number, Number> chart;

    /**
     * Generates a XYChart chart and displays it on screen
     * 
     * @param request ChartRequest containing the parameters for chart creation
     * @param data    DataModel containing the data to be displayed
     * @return        Finished Chart object ready to be added to UI  
     */
    public XYChart<Number, Number> createChart(
        ChartRequest request,
        ResponseBundle data
    ) {  
        this.request = request;
        this.data = data;

        ValueAxis<Number> xAxis = new NumberAxis();
        ValueAxis<Number> yAxis = new NumberAxis();

        switch(request.getChartType()) {
            case LINE_CHART:            
                this.chart = new LineChart<>(xAxis, yAxis);
                break;
            case AREA_CHART:
                this.chart = new AreaChart<>(xAxis, yAxis);
                break;
            case SCATTER_DOT_CHART:
                this.chart = new ScatterChart<>(xAxis, yAxis);
                break;
            default:
                System.err.println("[XYChartImpl] Invalid chart type from ChartParams");
                break;   
        }

        this.populateChartData(true);
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

    private void updateXAxisForDateTimeRange(long min, long max){
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        xAxis.setAutoRanging(false);
        long tickUnit = calculateTickUnit(min, max);
        xAxis.setTickUnit(tickUnit);
        xAxis.setLowerBound(min);
        xAxis.setUpperBound(max);
        xAxis.setTickLabelRotation(75);
        xAxis.setTickLabelFormatter(new NumberStringConverter() {
            @Override
            public String toString(Number object) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date(object.longValue());
                return dateFormat.format(date);
            }
        });
    }

    private long calculateTickUnit(long min, long max){
        long diff = max - min;
        long tickUnit = diff / 25;
        return tickUnit == 0 ? 1 : tickUnit;
    }

     /**
     * Populates the Chart object with provided data
     * 
     * @param hideNodes  boolean for hiding the datapoints' nodes
     * 
     * //TODO refactor to use Chart instead of XYChart (possibly move this to a XYChartImpl subclass)
     */
    public void populateChartData(Boolean hideNodes) {
        try {
            Map<AxisType, DataType> axisMap = request.getAxisMap();
            XYChart.Series<Number, Number> series = new XYChart.Series<>();

            // Assume that Y_AXIS is the value we want to plot
            series.setName(axisMap.get(AxisType.Y_AXIS).getDescription());

            // Check if there is only one DataResponse object
            if (data.getItems().size() == 1) {
                // Use the placeholder value for the x-axis
                long min = Long.MAX_VALUE;
                long max = Long.MIN_VALUE;
                for (Map.Entry<String, Double> entry : data.getItems().get(0).getData().getDataPoints().entrySet()) {
                    String dateString = entry.getKey();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateFormat.parse(dateString);
                    long epoch = date.getTime();
                    if (epoch < min) {
                        min = epoch;
                    }
                    else if (epoch > max) {
                        max = epoch;
                    }
                    Number xValue = epoch;
                    Number yValue = entry.getValue();
                    series.getData().add(new XYChart.Data<>(xValue, yValue));
                }
                updateXAxisForDateTimeRange(min, max);

            } else if (data.getItems().size() == 2) {
                // Use the first DataResponse object for the x-axis and the second for the y-axis
                Map<String, Double> xDataPoints = data.getItems().get(0).getData().getDataPoints();
                Map<String, Double> yDataPoints = data.getItems().get(1).getData().getDataPoints();
                for (Map.Entry<String, Double> entry : xDataPoints.entrySet()) {
                    Number xValue = entry.getValue();
                    Number yValue = yDataPoints.get(entry.getKey());
                    series.getData().add(new XYChart.Data<>(xValue, yValue));
                }
            } else {
                // Handle the case where there are more than two DataResponse objects
                // ...
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
        } catch (Exception e) {
            // could be InstantiationException, IllegalAccessException, etc.
            e.printStackTrace();
        }
    } 
}
