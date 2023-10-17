package fi.nordicwatt.controller.factory;

import fi.nordicwatt.model.data.AbstractDataModel;
import fi.nordicwatt.model.data.ChartRequest;

import javafx.scene.chart.PieChart;

/**
 * PieChart specific methods
 * 
 * @author Antti Hakkarainen
 */
public class PieChartImpl extends ChartImpl {

    // private PieChart chart;

    public PieChart createChart(ChartRequest request, AbstractDataModel<Double> data) {
        PieChart chart = new PieChart();

        return chart;

    }

    protected void populateChartData(Boolean hidenodes) {
        
    }
    
}
