package fi.nordicwatt.model.data;

import java.time.LocalDateTime;

import fi.nordicwatt.types.DataType;

/**
 * @author Janne Taskinen
 */
public class DataRequest {

    private final DataType dataType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String location;
    private ChartMetadata chartMetadata;   

    public DataRequest(
        DataType dataType, 
        LocalDateTime startTime,
        LocalDateTime endTime,        
        String location
    ) {
        this.dataType = dataType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    /**
     * Stores the tab and chart id of the chart that requested the data.
     * This is used to identify the chart where the data should be used.
     * 
     * @param tabId The id of the tab.
     * @param chartId The id of the chart.
     * @author Antti Hakkarainen
     */
    public static class ChartMetadata {
        private final String tabId;
        private final String chartId;

        public ChartMetadata(String tabId, String chartId) {
            this.tabId = tabId;
            this.chartId = chartId;
        }

        public String getTabId() {
            return tabId;
        }

        public String getChartId() {
            return chartId;
        }
    }

    public DataType getDataType() {
        return dataType;
    }

    public LocalDateTime getStarttime() {
        return startTime;
    }

    public LocalDateTime getEndtime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setChartMetadata(String tabId, String chartId) {
        this.chartMetadata = new ChartMetadata(tabId, chartId);
    }

    public ChartMetadata getChartMetadata() {
        return chartMetadata;
    }
}
