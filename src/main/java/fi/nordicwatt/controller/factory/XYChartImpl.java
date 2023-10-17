package fi.nordicwatt.controller.factory;

import java.util.Map;

import fi.nordicwatt.model.data.AbstractDataModel;
import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.types.AxisType;
import fi.nordicwatt.types.DataType;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

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
        AbstractDataModel<Double> data
    ) {  
        this.request = request;
        this.data = data;
        
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

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

     /**
     * Populates the Chart object with provided data
     * 
     * @param hideNodes  boolean for hiding the datapoints' nodes
     * 
     * //TODO refactor to use Chart instead of XYChart (possibly move this to a XYChartImpl subclass)
     */
    public void populateChartData(Boolean hideNodes)
    {
        try {
            Map<AxisType, DataType> axisMap = request.getAxisMap();
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
        }
        catch (Exception e) {
            // could be InstantiationException, IllegalAccessException, etc.
            e.printStackTrace();
        }
    }    
}
