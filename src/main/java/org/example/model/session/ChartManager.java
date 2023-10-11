package org.example.model.session;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import org.example.model.data.ChartRequest;

/**
 * Holds the list of ChartRequests, which can be used to get data from the 
 * DataManager in chart creation. Handles changes user makes to the ChartRequests.
 * Multiple ChartManagers can exist, one for each tab.
 * 
 * @author Antti Hakkarainen
 */
public class ChartManager {

    private Map<String, ChartRequest> storedCharts;
    private Integer maxCharts;

    public ChartManager(Integer maxCharts) {
        this.maxCharts = maxCharts;   
        storedCharts = new HashMap<>();
    }

    public ChartRequest getChartRequest(String chartId) {
        return storedCharts.get(chartId);
    }

    /**
     * Adds a new DataRequest to the list of DataRequests. If the list is full,
     * removes the oldest DataRequest and adds the new one.
     * TODO while adding a new chart, what should we do if the screen is full? Ask the user?
     */
    public String addChart(ChartRequest chartRequest) {
        if (storedCharts.size() < maxCharts) {
            String chartId = UUID.randomUUID().toString();
            storedCharts.put(chartId, chartRequest);
            return chartId;
        } else {
            // TODO ask user that you can't add more charts to the tab
            // TODO or ideally open a new tab and put the chart there.. (complex!)
        }
        return "";
    }

    /**
     * Removes a Chart from the chart map.
     */
    public void removeChart(String chartId) {
        storedCharts.remove(chartId);
    }

    
    
    
}
