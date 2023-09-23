package org.example.types;

import org.example.factory.ChartParams;

import javafx.scene.chart.XYChart;

@FunctionalInterface
public interface ChartSupplier {
    XYChart<Number, Number> create(ChartParams params);
}