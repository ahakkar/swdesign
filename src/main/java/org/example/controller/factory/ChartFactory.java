package org.example.controller.factory;

import java.time.LocalDate;
import java.util.List;

import org.example.model.data.DataPoint;
import org.example.model.services.DataManager;
import org.example.types.ChartType;

import javafx.scene.chart.XYChart;


/**
 * Handles chart creation tasks
 * 
 * @author Antti Hakkarainen
 */
public class ChartFactory {

    private final DataManager service;
    private String variableId;
    private LocalDate fromDate;
    private LocalDate toDate;

    public ChartFactory(
        DataManager service,
        String variableId,
        LocalDate fromDate,
        LocalDate toDate
    ) {
        this.service = service;
        this.variableId = variableId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public XYChart<?, ?> generateChart(
        ChartType chartType,
        String xAxisDesc,
        String yAxisDesc
    ) {
        // Get data from AP
        List<DataPoint> dataPoints = 
            service.getFingridFixedData(
                variableId,
                fromDate,
                toDate
            );
        
        // Wrap the params to a DTO class so they are easier to pass around
        ChartParams params = 
            new ChartParams(
                dataPoints, 
                chartType.toString(), 
                xAxisDesc,
                yAxisDesc
            );

        return chartType.createChart(params);        
    };   
}
