package org.example.types;

import java.util.ArrayList;
import java.util.List;

/**
 * enum for supported axis types
 * 
 * @author Antti Hakkarainen 
 */
public enum AxisType {

    CONSUMPTION("Consumption", "0", true, true, true, true, false, APIType.FINGRID, "Electricity Consumption (1 Hour)"),

    PRODUCTION("Production", "0", true, true, true, true, true, APIType.FINGRID,  "Electricity Production (1 Hour)"),

    PRICE("Price", "0", true, true, true, true, true, null, "Price"),

    TEMPERATURE("Temperature", "0", true, true, true, true, true, APIType.FMI, "Temperature (1 Hour)"),

    WIND("Wind", "Wind", true, true, true, true, true, APIType.FMI, "Wind (1 Hour)"),

    RAIN("Rain", "Rain", true, true, true, true, true, APIType.FMI, "Rain (1 Hour)"),

    HUMIDITY("Humidity","Humidity", true, true, true, true, true, APIType.FMI, "Humidity (1 Hour)"),

    TIME("Time", null, true, false, true, true, true, null, "Time (1 Hour)"),
    
    AIR_PRESSURE("Air pressure", "Air pressure", true, true, true, true, true, APIType.FMI, "Air pressure (1 Hour)");

    private final String label;
    private final String variableId;
    private final boolean xAxisAllowed;
    private final boolean yAxisAllowed;
    private final boolean lineChartAllowed;
    private final boolean scatterChartAllowed;
    private final boolean pieChartAllowed;
    private final APIType api;
    private final String description;

    AxisType(String label, String variableId, boolean xAxisAllowed, boolean yAxisAllowed, boolean lineChartAllowed, boolean scatterChartAllowed, boolean pieChartAllowed, APIType api, String description) {
        this.label = label;
        this.variableId = variableId;
        this.xAxisAllowed = xAxisAllowed;
        this.yAxisAllowed = yAxisAllowed;
        this.lineChartAllowed = lineChartAllowed;
        this.scatterChartAllowed = scatterChartAllowed;
        this.pieChartAllowed = pieChartAllowed;
        this.api = api;
        this.description = description;
    }

    public String getVariableId()
    {
        return variableId;
    }

    public boolean isXAxisAllowed() {
        return xAxisAllowed;
    }

    public boolean isYAxisAllowed() {
        return yAxisAllowed;
    }

    public boolean isLineChartAllowed() {
        return lineChartAllowed;
    }

    public boolean isScatterChartAllowed() {
        return scatterChartAllowed;
    }

    public boolean isPieChartAllowed() {
        return pieChartAllowed;
    }

    public APIType getApi() {
        return api;
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
