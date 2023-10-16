package org.example.model.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.example.model.data.ChartRequest;
import org.example.model.data.DataRequest;


/**
 * A singleton, only one tabmanager can exist. Holds the list of tabs, each tab
 * has its own ChartManager. The TabManager is responsible for creating and
 * deleting tabs, and moving charts between tabs.
 * 
 * @author Antti Hakkarainen
 */
public class TabManager {

    private static TabManager instance;
    private Map<TabInfo, ChartManager> tabs;
    private static int currentTab = 1;
    private static final Integer MAX_TABS = 20;

    /**
     * Singleton class  
     *   
     * @return SessionManager instance
     */
    public static TabManager getInstance() {
        // for thread safety
        if (instance == null) {
            synchronized (TabManager.class) {
                // check again as multiple threads can reach above step
                if (instance == null) {
                    instance = new TabManager();
                }            
            }
        }
        return instance;
    }

    public TabManager() {
        tabs = new HashMap<>();
    }

    public void addDataRequestToChart(
        String tabId,
        String chartId,
        DataRequest dataRequest
    ) {
        for (TabInfo key : tabs.keySet()) {
            if (key.getId().equals(tabId)) {
                tabs.get(key).addDataRequest(chartId, dataRequest);
            }
        }
    }

    public ChartRequest getChartRequest(String tabId, String chartId) {
        for (TabInfo key : tabs.keySet()) {
            if (key.getId().equals(tabId)) {
                return tabs.get(key).getChartRequest(chartId);
            }
        }

        return null;
    }

    public boolean isTabFull(String tabId) {
        for (TabInfo key : tabs.keySet()) {
            if (key.getId().equals(tabId)) {
                return tabs.get(key).isAtMaxCharts();
            }
        }

        return false;
    }

    public TabInfo addTab () {
        if (tabs.size() < MAX_TABS) {
            TabInfo newTab = new TabInfo(UUID.randomUUID().toString(), "Tab " + currentTab);
            ChartManager chartManager = new ChartManager(4);
            tabs.put(newTab, chartManager);

            currentTab++;
            return newTab;
        } 
        // TODO ask user if they want to close a tab
        // if yes, close the oldest tab and add the new one
        // if no, do nothing

        return new TabInfo(null, null);
        
    }

    public void removeTab (String tabId) {
        TabInfo keyToRemove = null;

        for (TabInfo key : tabs.keySet()) {
            if (key.getId().equals(tabId)) {
                keyToRemove = key;
                break;
            }
        }
    
        if (keyToRemove != null) {
            System.out.println("Removed tab with id: " + keyToRemove.getId());
            tabs.remove(keyToRemove);
        }
    }    

    public String addChartToTab(String tabId, ChartRequest request) {
        // TODO handle case when tab is not found
        for (TabInfo key : tabs.keySet()) {
            if (key.getId().equals(tabId)) {
                String chartId = tabs.get(key).addChart(request);   
                return chartId;             
            }
        }

        return "";
    }
}
