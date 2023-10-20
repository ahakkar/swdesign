package fi.nordicwatt.controller.factory;

import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.model.datamodel.ResponseBundle;
import javafx.scene.chart.Chart;

/**
 * Handles actual Chart object creation requests. Uses subclasses of Chart
 * (PieChart, XYChart), with chart type specific methods in specialized classes.
 * 
 * @see fi.nordicwatt.controller.factory.ChartImpl common methods for all Chart types 
 * @see fi.nordicwatt.controller.factory.PieChartImpl PieChart specific methods 
 * @see fi.nordicwatt.controller.factory.XYChartImpl XYChart specific methods
 * 
 * @author Antti Hakkarainen 
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
        ChartRequest request,
        ResponseBundle data
    ) {    
        switch(request.getChartType()) {
            // At this stage first 3 use the same class -ah
            case LINE_CHART:
            case AREA_CHART:
            case SCATTER_DOT_CHART:
                XYChartImpl xyCreator = new XYChartImpl();
                return xyCreator.createChart(request, data);                  
            case PIE_CHART:
                PieChartImpl pieCreator = new PieChartImpl();
                return pieCreator.createChart(request, data);    
            default:
                return null;
        }     
    };   
}