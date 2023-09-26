package org.example.controller.factory;

import java.util.List;

import org.example.model.data.DataPoint;

/**
 * Data Transfer Object, DTO to pass params to chart creator classes
 * 
 * @author Antti Hakkarainen
 */
public class ChartParams {

    private final List<DataPoint> dataPoints;
    private final String chartDesc;
    private final String xAxisDesc;
    private final String yAxisDesc;

    public ChartParams(
        List<DataPoint> dataPoints,
        String chartDesc,
        String xAxisDesc,
        String yAxisDesc
    ) {
        
        this.dataPoints = dataPoints;
        this.chartDesc = chartDesc;
        this.xAxisDesc = xAxisDesc;
        this.yAxisDesc = yAxisDesc;
    };

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public String getChartDesc() {
        return chartDesc;
    }

    public String getXAxisDesc() {
        return xAxisDesc;
    }

    public String getYAxisDesc() {
        return yAxisDesc;
    }
    
}
