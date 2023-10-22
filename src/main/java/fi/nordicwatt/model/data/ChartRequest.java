package fi.nordicwatt.model.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fi.nordicwatt.model.datamodel.RequestBundle;
import fi.nordicwatt.types.AxisType;
import fi.nordicwatt.types.ChartType;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.Logger;


/**
 * Stores parameters required to create a JavaFX chart.
 * Data is fetched from DataManager, which gets it either from local storage
 * or over the internet via API calls.
 * 
 * @param chartType     Line chart, Area chart, Pie chart etc?
 * @param axisMap       A map specifying the axis types and their corresponding data types.
 * @param dataRequest   The data request used by DataManager.
 * 
 * @author Antti Hakkarainen
 */
public class ChartRequest
{
    private ChartType chartType;
    private Map<AxisType, DataType> axisMap = new HashMap<>();
    private RequestBundle bundle;
    private String startTime;
    private String endTime;
    private final String chartId;

    /**
     * Initializes a new ChartRequest.
     */
    public ChartRequest(
        ChartType chartType,
        Map<AxisType, DataType> axisMap, 
        RequestBundle bundle,
        String startTime,
        String endTime
    ) {
        this.chartType = chartType;
        this.axisMap = axisMap;
        this.bundle = bundle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.chartId = UUID.randomUUID().toString();
        Logger.log("ChartRequest created: " + this.toString());
    }


    /**
     * Get the axis map of the chart, which contains what axis
     * the chart is made of, and their datatypes.
     * 
     * @return Map<AxisType, DataType> The axis map of the chart
     */
    public Map<AxisType, DataType> getAxisMap() {
        return axisMap;
    }

    public void setRequestBundle(RequestBundle bundle) {
        this.bundle = bundle;
    }

    public String getRequestBundleId() {
        return bundle.getId();
    }


    /**
     * Get the data request of the chart, which contains the
     * data type, start and end times, and location.
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
        return "ChartRequest: " + chartType + ", " + axisMap + ", " + bundle + ", " + startTime + ", " + endTime;
    }
}
