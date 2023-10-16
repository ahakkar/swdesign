package org.example.model.data;

import java.util.HashMap;
import java.util.Map;

import org.example.types.APIType;
import org.example.types.AxisType;
import org.example.types.ChartType;
import org.example.types.DataType;


/**
 * Stores parameters required to create a JavaFX chart.
 * Data is fetched from DataManager, which gets it either from local storage
 * or over the internet via API calls.
 * 
 * @author Antti Hakkarainen
 */
public class ChartRequest
{
    private APIType apiType;
    private ChartType chartType;
    private Map<AxisType, DataType> axisMap = new HashMap<>();
    private DataRequest dataRequest;

    /**
     * Initializes a new ChartRequest.
     * 
     * @param apiType       FMI, Fingrid, something else?
     * @param chartType     Line chart, Area chart, Pie chart etc?
     * @param axisMap       A map specifying the axis types and their corresponding data types.
     * @param dataRequest   The data request used by DataManager.
     */
    public ChartRequest(
        APIType apiType,
        ChartType chartType,
        Map<AxisType, DataType> axisMap, 
        DataRequest dataRequest
    ) {
        this.apiType = apiType;
        this.chartType = chartType;
        this.axisMap = axisMap;
        this.dataRequest = dataRequest;
    }


    /**
     * Get the APIType of the chart
     * 
     * @return APIType The API used to get the data from.
     */
    public APIType getApiType() {
        return apiType;
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

    public void setDataRequest(DataRequest dataRequest) {
        this.dataRequest = dataRequest;
    }


    /**
     * Get the data request of the chart, which contains the
     * data type, start and end times, and location.
     * 
     * @return DataRequest The data request of the chart's data
     */
    public DataRequest getDataRequest() {
        return dataRequest;
    }

    public ChartType getChartType() {
        return chartType;
    }
}
