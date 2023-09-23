package org.example.types;

import java.util.ArrayList;
import java.util.List;

/**
 * enum for supported axis types
 * 
 * @author Antti Hakkarainen 
 */
public enum AxisType {
    CONSUMPTION("Consumption", "124", "Electricity Consumption (1 Hour)"),
    PRODUCTION("Production", "74", "Electricity Production (1 Hour)"),
    PRICE("Price", "00", ""),
    TEMPERATURE("Temperature", "00", ""),
    WIND("Wind", "00", "");

    private final String label;
    private final String variableId;
    private final String description;

    AxisType(String label, String variableId, String description) {
        this.label = label;
        this.variableId = variableId;
        this.description = description;
    }

    public String getVariableId() {
        return variableId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return label;
    }

    public static List<String> stringValues() {
        List<String> stringValues = new ArrayList<>();
        for (AxisType type : AxisType.values()) {
            stringValues.add(type.toString());
        }
        return stringValues;
    }
}
