package fi.nordicwatt.types;

import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Enum for supported data types
 * 
 * @param label The label shown in the UI
 * @param variableId The variableId used in the API call
 * @param APIType The API used to get the data from
 * @param unit The unit of measurement (Celisus, m/s, € etc.)
 * @param interval ...
 * @param chartTitle ...
 * @param description The longer description shown in the UI
 * @param allowedAxes The axes allowed for this data type
 * @param allowedCharts The charts allowed for this data type
 * 
 * @see fi.nordicwatt.types.AxisType supported axes
 * @see fi.nordicwatt.types.ChartType supported chart types
 * 
 * @author Antti Hakkarainen
 */
public enum DataType {
    CONSUMPTION("Total Electricity Consumption", "124", APIType.FINGRID,
            MeasurementUnit.MEGA_WATT_HOUR, Duration.ofHours(1),
            "Total Electricity Consumption (1 Hour)", "Total electricity consumption in Finland",
            EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.AREA_CHART),
            false),

    PRODUCTION("Total Electricity Production", "74", APIType.FINGRID,
            MeasurementUnit.MEGA_WATT_HOUR, Duration.ofHours(1),
            "Total Electricity Production (1 Hour)", "Total electricity production in Finland",
            EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.AREA_CHART),
            false),

    HYDRO("Hydro Production", "191", APIType.FINGRID, MeasurementUnit.MEGA_WATT,
            Duration.ofMinutes(3), "Hydro Production (3 minutes)", "Hydro Production in Finland",
            EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.AREA_CHART),
            false),

    NUCLEAR("Nuclear Production", "188", APIType.FINGRID, MeasurementUnit.MEGA_WATT,
            Duration.ofMinutes(3), "Nuclear Production (3 minutes)",
            "Nuclear Production in Finland", EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.AREA_CHART),
            false),

    WINDPOWER("Wind power generation", "75", APIType.FINGRID, MeasurementUnit.MEGA_WATT_HOUR,
            Duration.ofMinutes(3), "Wind Production (3 minutes)",
            "Wind power production in Finland", EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.AREA_CHART),
            false),

    TEMPERATURE("Temperature", "Temperature", APIType.FMI, MeasurementUnit.CELSIUS,
            Duration.ofHours(1), "Temperature (1 Hour)", "Temperature in selected location",
            EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.AREA_CHART),
            true),

    WIND("Wind", "WindSpeedMS", APIType.FMI, MeasurementUnit.METERS_PER_SECOND, Duration.ofHours(1),
            "Wind (1 Hour)", "Wind speed in selected location",
            EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.AREA_CHART),
            true),

    HUMIDITY("Humidity", "Humidity", APIType.FMI, MeasurementUnit.HUMIDITY, Duration.ofHours(1),
            "Humidity (1 Hour)", "Humidity in selected location",
            EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.AREA_CHART),
            true),

    AIR_PRESSURE("Air pressure", "Pressure", APIType.FMI, MeasurementUnit.HPA, Duration.ofHours(1),
            "Air pressure (1 Hour)", "Air pressure in selected location",
            EnumSet.of(AxisType.X_AXIS, AxisType.Y_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.SCATTER_DOT_CHART, ChartType.AREA_CHART),
            true),

    TIME("Time", "Time", APIType.NOAPI, MeasurementUnit.HOUR, Duration.ofHours(1), "Time (1 Hour)",
            "Time used on the x-axis", EnumSet.of(AxisType.X_AXIS),
            EnumSet.of(ChartType.LINE_CHART, ChartType.AREA_CHART), false

    );

    private final String label;
    private final String variableId;
    private final APIType api;
    private final MeasurementUnit unit;
    private final Duration interval;
    private final String chartTitle;
    private final String description;
    private final EnumSet<AxisType> allowedAxes;
    private final EnumSet<ChartType> allowedCharts;
    private final boolean isLocationRequired;

    DataType(String label, String variableId, APIType api, MeasurementUnit unit, Duration interval,
            String chartTitle, String description, EnumSet<AxisType> allowedAxes,
            EnumSet<ChartType> allowedCharts, boolean isLocationRequired) {
        this.label = label;
        this.variableId = variableId;
        this.api = api;
        this.unit = unit;
        this.interval = interval;
        this.chartTitle = chartTitle;
        this.description = description;
        this.allowedAxes = allowedAxes;
        this.allowedCharts = allowedCharts;
        this.isLocationRequired = isLocationRequired;
    }

    public String getVariableId() {
        return variableId;
    }

    public APIType getAPIType() {
        return api;
    }

    public MeasurementUnit getUnit() {
        return unit;
    }

    public Duration getInterval() {
        return interval;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLocationRequired() {
        return isLocationRequired;
    }

    /**
     * Check if the axis is allowed for this data type
     * 
     * @param axis AxisType enum to check
     * @return True if allowed, false if not
     */
    public boolean isAxisAllowed(AxisType axis) {
        return allowedAxes.contains(axis);
    }


    /**
     * Check if the chart type is allowed for this data type
     * 
     * @param chartType ChartType enum to check
     * @return True if allowed, false if not
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
     * 
     * @return List of string values
     */
    public static List<String> stringValues() {
        List<String> stringValues = new ArrayList<>();
        for (DataType type : DataType.values()) {
            stringValues.add(type.toString());
        }
        return stringValues;
    }

    /**
     * Get the API type of the data type
     */
    public APIType getAPI() {
        return api;
    }

}
