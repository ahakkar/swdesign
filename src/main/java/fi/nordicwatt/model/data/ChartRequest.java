package fi.nordicwatt.model.data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fi.nordicwatt.model.datamodel.RequestBundle;
import fi.nordicwatt.types.AxisType;
import fi.nordicwatt.types.ChartType;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.Logger;


/**
 * Stores parameters required to create a JavaFX chart. Data is fetched from DataManager, which gets
 * it either from local storage or over the internet via API calls.
 * 
 * @param chartType Line chart, Area chart, Pie chart etc?
 * @param axisMap A map specifying the axis types and their corresponding data types.
 * @param dataRequest The data request used by DataManager.
 * 
 * @author Antti Hakkarainen
 */
public class ChartRequest {
    private ChartType chartType;
    private Map<AxisType, DataType> axisMap = new HashMap<>();
    private RequestBundle bundle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final String chartId;
    private String location;

    /**
     * Initializes a new ChartRequest.
     */
    public ChartRequest(ChartType chartType, Map<AxisType, DataType> axisMap, RequestBundle bundle,
            String location, LocalDateTime startTime, LocalDateTime endTime) {
        Logger.log("Creating ChartRequest...");
        this.chartType = chartType;
        this.axisMap = axisMap;
        this.bundle = bundle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.chartId = UUID.randomUUID().toString();
        this.location = location; // Lets use this as a placeholder for now
        Logger.log("ChartRequest created: " + this.toString());
    }


    /**
     * Get the axis map of the chart, which contains what axis the chart is made of, and their
     * datatypes.
     * 
     * @return Map<AxisType, DataType> The axis map of the chart
     */
    public Map<AxisType, DataType> getAxisMap() {
        return axisMap;
    }

    public void setRequestBundle(RequestBundle bundle) {
        this.bundle = bundle;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getRequestBundleId() {
        return bundle.getId();
    }


    /**
     * Get the data request of the chart, which contains the data type, start and end times, and
     * location.
     * 
     * @return DataRequest The data request of the chart's data
     */
    public RequestBundle getRequestBundle() {
        return this.bundle;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public String getChartId() {
        return chartId;
    }

    public String toString() {
        return "ChartRequest: " + chartType + ", " + axisMap + ", " + bundle + ", " + startTime
                + ", " + endTime;
    }

    /**
     * Generates a title for the tab that the chart is in. A quick "last minute" solution to get a
     * title for a tab. This assumes a request can hold only one x and only y axis - so it's not
     * very flexible.
     * 
     * Format: dd.MM.yyyy - dd.MM.yyyy / x-axis : y-axis
     * 
     * @return String The title of the tab
     */
    public String generateTabTitle() {
        String title = "";
        title += startTime.getDayOfMonth() + "." + startTime.getMonthValue() + "."
                + startTime.getYear();
        title += " - ";
        title += endTime.getDayOfMonth() + "." + endTime.getMonthValue() + "." + endTime.getYear();
        title += " / ";
        title += axisMap.get(AxisType.X_AXIS) + " : " + axisMap.get(AxisType.Y_AXIS);
        return title;
    }
}
