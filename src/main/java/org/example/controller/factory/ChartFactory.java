package org.example.controller.factory;


import org.example.model.data.AbstractDataModel;
import org.example.model.data.ChartRequest;

import org.example.types.ChartType;

import javafx.scene.chart.Chart;

/**
 * Handles chart creation tasks
 * 
 * @author Antti Hakkarainen
 */
public class ChartFactory {

    private static ChartFactory instance;

    /**
     * Singleton class  
     *   
     * @return ChartFactory instance
     */
    public static ChartFactory getInstance() {
        // for thread safety
        if (instance == null) {
            synchronized (ChartFactory.class) {
                // check again as multiple threads can reach above step
                if (instance == null) {
                    instance = new ChartFactory();
                }            
            }
        }
        return instance;
    }


    public ChartFactory() { }

    public Chart generateChart(
        AbstractDataModel<Double> data,
        ChartRequest request
    ) {    
        ChartParams params = new ChartParams(data, request);
        ChartType chartType = request.getChartType();

        return chartType.createChart(params);        
    };   
}