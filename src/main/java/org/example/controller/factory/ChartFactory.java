package org.example.controller.factory;


import org.example.model.data.AbstractDataModel;
import org.example.model.data.ChartRequest;

import org.example.types.ChartType;

import javafx.scene.chart.Chart;

/**
 * Handles actual Chart object creation requests. Uses subclasses of Chart
 * (PieChart, XYChart), with chart type specific methods in specialized classes.
 * 
 * @see org.example.controller.factory.ChartImpl common methods for all Chart types 
 * @see org.example.controller.factory.PieChartImpl PieChart specific methods 
 * @see org.example.controller.factory.XYChartImpl XYChart specific methods
 * @author Antti Hakkarainen
 * 
 * // TODO PieChart specific class not actually implemented yet
 * // TODO XYChart specific class not actually implemented yet
 */
public class ChartFactory {

    private static ChartFactory instance;

    /**
     * Singleton class  
     *   
     * @return instance of the class
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

    /** Empty constructor */
    public ChartFactory() { }

    /**
     * Generates a chart based on the given data and request.
     * 
     * @param data      data for the chart
     * @param request   parameters needed for chart creation (axis etc)
     * @return          Finished Chart object ready to be added to UI
     */
    public Chart generateChart(
        AbstractDataModel<Double> data,
        ChartRequest request
    ) {    
        ChartParams params = new ChartParams(data, request);
        ChartType chartType = request.getChartType();

        return chartType.createChart(params);        
    };   
}