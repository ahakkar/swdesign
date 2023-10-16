package org.example.types;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Enum for supported data types
 * 
 * @param label         The label shown in the UI
 * @param variableId    The variableId used in the API call
 * @param APIType       The API used to get the data from
 * @param unit          The unit of measurement (Celisus, m/s, € etc.)
 * @param interval      ... 
 * @param chartTitle    ...
 * @param description   The longer description shown in the UI
 * @param allowedAxes   The axes allowed for this data type
 * @param allowedCharts The charts allowed for this data type 
 * 
 * @see org.example.types.AxisType  supported axes
 * @see org.example.types.ChartType supported chart types
 * 
 * @author Antti Hakkarainen 
 */
public enum DataType {
    CONSUMPTION(
        "Consumption",
        "124",
        APIType.FINGRID, 
        "Electricity Consumption (1 Hour)",
        EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
        EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.PIE_CHART)
    ),
    PRODUCTION(
        "Production",
        "74",
        APIType.FINGRID, 
        "Electricity Production (1 Hour)",
        EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
        EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.PIE_CHART)
    ),
    PRICE(
        "Price", 
        "0",
        null,
        "Price",
        EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
        EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.PIE_CHART)
    ),
    TEMPERATURE(
        "Temperature",
        "0",
        APIType.FMI,
        "Temperature (1 Hour)",
        EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
        EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.PIE_CHART)
    ),
    WIND(
        "Wind",
        "Wind", 
        APIType.FMI,
        "Wind (1 Hour)",
        EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
        EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.PIE_CHART)
    ),
    RAIN(
        "Rain",
        "Rain", 
        APIType.FMI,
        "Rain (1 Hour)",
        EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
        EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.PIE_CHART)
    ),
    HUMIDITY(
        "Humidity",
        "Humidity",
        APIType.FMI,
        "Humidity (1 Hour)",
        EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
        EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.PIE_CHART)
    ),
    TIME(
        "Time",
        null,
        null,
        "Time (1 Hour)",
        EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
        EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.PIE_CHART)

    ),    
    AIR_PRESSURE(
        "Air pressure",
        "Air pressure",
        APIType.FMI,
        "Air pressure (1 Hour)",
        EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
        EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.PIE_CHART)
    );

    private final String label;
    private final String variableId;
    private final APIType api;
    private final String description;
    private final EnumSet<AxisType> allowedAxes;
    private final EnumSet<ChartType> allowedCharts;

    DataType(
        String label,
        String variableId,
        APIType api,
        String description,
        EnumSet<AxisType> allowedAxes,
        EnumSet<ChartType> allowedCharts
    ) {
        this.label = label;
        this.variableId = variableId;
        this.api = api;
        this.description = description;
        this.allowedAxes = allowedAxes;
        this.allowedCharts = allowedCharts;        
    }

    public String getVariableId() { return variableId; }
    public APIType getAPIType() { return api; }
    public String getDescription() { return description;}

    /**
     * Check if the axis is allowed for this data type
     * 
     * @param axis  AxisType enum to check
     * @return      True if allowed, false if not
     */
    public boolean isAxisAllowed(AxisType axis) {
        return allowedAxes.contains(axis);
    }


    /**
     * Check if the chart type is allowed for this data type
     * 
     * @param chartType ChartType enum to check
     * @return          True if allowed, false if not
     */
    public boolean isChartAllowed(ChartType chartType) {
        return allowedCharts.contains(chartType);
    }

    /**
     * Get the label of the data type
     */
    @Override
    public String toString() {
        return label;
    }


    /**
     * Get a list of all the string values of the enum
     * @return List of string values
     */
    public static List<String> stringValues() {
        List<String> stringValues = new ArrayList<>();
        for (DataType type : DataType.values()) {
            stringValues.add(type.toString());
        }
        return stringValues;
    }
}
