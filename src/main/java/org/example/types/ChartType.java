package org.example.types;

import java.util.ArrayList;
import java.util.List;

import org.example.controller.factory.AreaChartImpl;
import org.example.controller.factory.ChartParams;
import org.example.controller.factory.LineChartImpl;
import org.example.controller.factory.ScatterChartImpl;

import javafx.scene.chart.XYChart;

/**
 * Enum for supported chart types
 * 
 * @author Antti Hakkarainen
 */
public enum ChartType {
    LINE_CHART("Line chart", params -> new LineChartImpl().createXYChart(params)),
    AREA_CHART("Area chart", params -> new AreaChartImpl().createXYChart(params)),
    SCATTER_DOT_CHART("Scatter chart", params -> new ScatterChartImpl().createXYChart(params)),
    PIE_CHART("Pie chart", null);

    private final String label;
    private final ChartSupplier chartSupplier;

    ChartType(String label, ChartSupplier chartSupplier) {
        this.label = label;
        this.chartSupplier = chartSupplier;
    }

    public XYChart<Number, Number> createChart(ChartParams params) {
        return chartSupplier.create(params);
    }

    @Override
    public String toString() {
        return label;
    }

    public static List<String> stringValues() {
        List<String> stringValues = new ArrayList<>();
        for (ChartType type : ChartType.values()) {
            stringValues.add(type.toString());
        }
        return stringValues;
    }
}
