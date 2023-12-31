package fi.nordicwatt.controller.factory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import fi.nordicwatt.model.data.DataResponse;
import fi.nordicwatt.model.datamodel.ResponseBundle;
import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.types.AxisType;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.Logger;
import javafx.collections.ObservableList;
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

        this.populateChartData();
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
     */
    public void populateChartData() {
        try {
            Map<AxisType, DataType> axisMap = request.getAxisMap();
            XYChart.Series<Number, Number> series = null;

            // If data.getItems().size() == 1, then X-axis represents time
            if (data.getItems().size() == 1) {
                series = populateDateNumberChart();
            } else if (data.getItems().size() == 2) {
                series = populateNumberNumberChart(axisMap);
            } else {
                    throw new IllegalArgumentException("XYChartImpl.java: DataResponse size is not 1 or 2");
            }
            series.setName(axisMap.get(AxisType.Y_AXIS).getDescription());
            chart.getData().clear();
            chart.getData().add(series);
            if (!(chart instanceof ScatterChart)) {
                this.hideNodes();
            }

        } catch (Exception e) {
            // could be InstantiationException, IllegalAccessException, etc.
            e.printStackTrace();
        }
    }

    private XYChart.Series<Number, Number> populateDateNumberChart() throws ParseException {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        // Creating ObservableList AND ArrayList because adding single data value to ObservableList is slow
        ObservableList<XYChart.Data<Number, Number>> dataList = series.getData();
        ArrayList<XYChart.Data<Number, Number>> dataPoints = new ArrayList<>();
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        Set<Map.Entry<LocalDateTime, Double>> entries =  data.getItems().get(0).getData().getDataPoints().entrySet();
        for (Map.Entry<LocalDateTime, Double> entry : entries) {
            LocalDateTime localDateTime = entry.getKey();
            ZoneId defaultZoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = localDateTime.atZone(defaultZoneId);
            Date date = Date.from(zonedDateTime.toInstant());
            long epoch = date.getTime();
            if (epoch < min) {
                min = epoch;
            }
            else if (epoch > max) {
                max = epoch;
            }
            Number xValue = epoch;
            Number yValue = entry.getValue();
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(xValue, yValue);
            dataPoints.add(dataPoint);
        }
        dataList.addAll(dataPoints);
        updateXAxisForDateTimeRange(min, max);
        return series;
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

    private XYChart.Series<Number, Number> populateNumberNumberChart(Map<AxisType, DataType> axisMap){
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        Map<LocalDateTime, Double> xDataPoints = null;
        Map<LocalDateTime, Double> yDataPoints = null;
        List<DataResponse> response = data.getItems();

        // Map the data results to correct axises based on the axisMap
        for (DataResponse dataResponse : response) {
            // xDataPoints null check done to prevent yDataPoints not being assigned value in case axisMap has equal DataType values for both axises
            if (dataResponse.getData().getDataType() == axisMap.get(AxisType.X_AXIS) && xDataPoints == null) {
                xDataPoints = dataResponse.getData().getDataPoints();
            } else {
                yDataPoints = dataResponse.getData().getDataPoints();
            }
        }

        List<Map<LocalDateTime, Double>> intersectingValues = getIntersectingValues(xDataPoints, yDataPoints, 2);
        Map<LocalDateTime, Double> xAxisDataPoints = intersectingValues.get(0);
        Map<LocalDateTime, Double> yAxisDataPoints = intersectingValues.get(1);

        for (LocalDateTime entry : xAxisDataPoints.keySet()) {
            Number xValue = xAxisDataPoints.get(entry);
            Number yValue = yAxisDataPoints.get(entry);
            series.getData().add(new XYChart.Data<>(xValue, yValue));
        }

        if (xAxisDataPoints.size() != xDataPoints.size() || yAxisDataPoints.size() != yDataPoints.size()) {
            Logger.log("XYChartImpl: Some data points are not shown due to pair not being found. Original data on x axis: " + xDataPoints.size() + ", original data on y axis: " + yDataPoints.size() + " intersecting data size: " + xAxisDataPoints.size() + "");
        }



        return series;
    }

    protected void hideNodes(){
        for (XYChart.Series<Number, Number> s : chart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                d.getNode().setVisible(false);
            }
        }
    }

    /**
    * Returns the intersection of two maps using the keys. Assumes that the keys are string written as timestamps in form of "yyyy-MM-dd HH:mm:ss"
    * Supports rounding in minutes. E.g. if rounding is set as 2, keys "2019-01-01 12:01" and "2019-01-01 12:02" are considered equal.
    * In case of rounding the key of the returned map is the first key of the intersecting values. "2019-01-01 12:01" in the previous example.
    */
    private List<Map<LocalDateTime, Double>> getIntersectingValues(Map<LocalDateTime, Double> xDataPoints, Map<LocalDateTime, Double> yDataPoints, int roundingInMinutes) {
        List<Map<LocalDateTime, Double>> intersectingValues = new ArrayList<>();
        Map<LocalDateTime, Double> xIntersecting = new HashMap<>();
        Map<LocalDateTime, Double> yIntersecting = new HashMap<>();

        for (LocalDateTime xKey : xDataPoints.keySet()) {
            for (LocalDateTime yKey : yDataPoints.keySet()) {
                try {
                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    ZonedDateTime zonedDateTimex = xKey.atZone(defaultZoneId);
                    ZonedDateTime zonedDateTimey = yKey.atZone(defaultZoneId);
                    Date xDate = Date.from(zonedDateTimex.toInstant());
                    Date yDate = Date.from(zonedDateTimey.toInstant());
                    long timeDiff = Math.abs(xDate.getTime() - yDate.getTime());
                    long minutesDiff = timeDiff / (60 * 1000);

                    if (minutesDiff <= roundingInMinutes) {
                        xIntersecting.put(xKey, xDataPoints.get(xKey));
                        yIntersecting.put(xKey, yDataPoints.get(yKey));
                    }
                } catch (Exception e) {
                    // Handle date parsing exception if necessary
                    e.printStackTrace();
                }
            }
        }

        intersectingValues.add(xIntersecting);
        intersectingValues.add(yIntersecting);

        return intersectingValues;
    }
}
