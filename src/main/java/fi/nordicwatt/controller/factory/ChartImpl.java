package fi.nordicwatt.controller.factory;

import fi.nordicwatt.model.datamodel.ResponseBundle;
import fi.nordicwatt.model.data.ChartRequest;

import javafx.scene.chart.Chart;

/**
 * Common abstract class for chart implementations. Holds common methods for
 * all chart types. XYChart and PieChart specific subclasses extend this class.
 * 
 * @author Antti Hakkarainen
 */
public abstract class ChartImpl
{
    protected Chart chart;
    protected ResponseBundle data;
    protected ChartRequest request;

    public ChartImpl() {}

    public abstract Chart createChart(ChartRequest request, ResponseBundle data);

    protected abstract void populateChartData();

    protected abstract void hideNodes();
}