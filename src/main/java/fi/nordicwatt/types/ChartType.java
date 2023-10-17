package fi.nordicwatt.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum for supported chart types
 * 
 * @author Antti Hakkarainen
 */
public enum ChartType {
    LINE_CHART("Line chart"),
    AREA_CHART("Area chart"),
    SCATTER_DOT_CHART("Scatter chart"),
    PIE_CHART("Pie chart");

    private final String label;

    ChartType(String label) {
        this.label = label;
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
