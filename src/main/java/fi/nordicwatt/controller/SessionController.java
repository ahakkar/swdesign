package fi.nordicwatt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.model.service.DataManager;
import fi.nordicwatt.model.service.DataManagerListener;
import fi.nordicwatt.model.session.SessionChangeData;
import fi.nordicwatt.model.session.TabInfo;
import fi.nordicwatt.types.SessionChangeType;

/**
 * Centralized point of truth for the state of the application. Session is loaded from and saved to
 * model (via datamanager maybe?). Stores instance of a tabmanager, and instances of chartmanagers
 * inside the tabs. Gets updates to the session's state made by user, emits changes to session state
 * to primarycontroller. Then primarycontroller can make the state changes visible in UI.
 * 
 * Not going to be implemented apparently, due to time constraints (status of 20.11.2023 - ah):
 * store UI control values with bidirectionalbinding here too somewhere
 * 
 * @author Antti Hakkarainen
 */
public class SessionController implements DataManagerListener {
    private static SessionController instance;
    private final DataManager dataManager = DataManager.getInstance();
    private static ArrayList<SessionControllerListener> listeners;

    // Both of these store an UUID as a string to the key
    private static Map<String, TabInfo> tabMap = new HashMap<>();
    private static Map<String, ChartRequest> chartMap = new HashMap<>();

    private String currentTabId;

    // Pending (not yet displayed, because data retrieval from backend is asynchronous)
    private ChartRequest pendingChartRequest;
    private TabInfo pendingTab;
    private String pendingTabId;

    /**
     * Singleton class
     * 
     * @return SessionManager instance
     */
    public static SessionController getInstance() {
        // for thread safety
        if (instance == null) {
            synchronized (SessionController.class) {
                // check again as multiple threads can reach above step
                if (instance == null) {
                    instance = new SessionController();
                }
            }
        }
        return instance;
    }

    /**
     * Register this class as a listener to DataManager's events
     */
    public SessionController() {
        SessionController.listeners = new ArrayList<>();
        SessionController.tabMap = new HashMap<>();
        SessionController.chartMap = new HashMap<>();
        dataManager.registerListener(this);

        pendingChartRequest = null;
        pendingTab = null;
    }


    /**
     * Request session data from DataManager and recreate UI component selections, tabs and their
     * charts based on the restored session.
     * 
     * Not going to implement these due to time constraints - ah 20.11.2023
     */
    public void loadSession() {

    }


    /**
     * Save session data to DataManager so we can use it to recreate UI component selections, tabs
     * and their charts later.
     * 
     * Not going to implement these due to time constraints - ah 20.11.2023
     */
    public void saveSession() {

    }


    /**
     * Reset all tabs, charts, and UI selections to default values. (=basically removes all tabs and
     * charts)
     * 
     * Not going to implement these due to time constraints - ah 20.11.2023
     */
    public void resetSession() {}


    /**
     *
     */
    public String addTab() {
        SessionChangeData data = new SessionChangeData(SessionChangeType.TAB_ADDED);
        if (pendingTab == null) {
            TabInfo newTab = new TabInfo("Tab");
            data.setTabId(newTab.getId());
            data.setTitle(newTab.getTitle());
            this.currentTabId = newTab.getId();
            tabMap.put(newTab.getId(), newTab);
        } else {
            data.setTabId(pendingTabId);
            data.setTitle(pendingTab.getTitle());
            this.currentTabId = pendingTabId;
            tabMap.put(pendingTabId, pendingTab);
        }

        notifyListeners(data); // Used to notify PrimaryController to add a new Tab to UI

        return currentTabId;
    }


    /**
     * Remove a tab.
     * 
     * @param tabId Unique UUID string for a tab to be removed
     */
    public void removeTab(String tabId) {
        Set<String> storeCharts = tabMap.get(tabId).getStoredCharts();
        tabMap.remove(tabId);

        for (Iterator<String> it = storeCharts.iterator(); it.hasNext();) {
            String chartId = it.next();
            chartMap.remove(chartId);
        }

        SessionChangeData data = new SessionChangeData(SessionChangeType.TAB_REMOVED);
        data.setTabId(tabId);
        notifyListeners(data);
    }


    /**
     * 
     * @param chartRequest ChartRequest object
     * @param toNewTab
     */
    public void newPendingChartRequest(ChartRequest chartRequest, boolean toNewTab)
            throws IllegalArgumentException {
        this.pendingChartRequest = chartRequest;

        if (toNewTab) {
            pendingTab = new TabInfo("Tab");
            pendingTabId = pendingTab.getId();
        } else {
            pendingTab = null;
            pendingTabId = currentTabId;
        }
    }


    /**
     * Retrieve parameters for creating a chart from the specified tab.
     * 
     * @param chartId Unique UUID string for which chart to choose from the tab
     * @return Object containing data required for a Chart creation task.
     */
    public ChartRequest getPendingChartRequest() {
        // search chartmap for chartrequest with matching requestbundleid
        if (pendingChartRequest != null) {
            return pendingChartRequest;
        }

        return null;
    }

    /**
     * Retrieve pending tab id
     * 
     * @return pending tab id or null
     */
    public String getPendingTabId() {
        if (pendingTabId != null) {
            return pendingTabId;
        }

        return null;
    }


    /**
     * Moves pending tab and chart info to maps, if operation is successful. Otherwise discards
     * pending data.
     * 
     * @param isSuccessful success of chart generation operation
     */
    public void pendingChartRequestSuccess(boolean isSuccessful) {
        if (isSuccessful) {
            // tab doesn't exist?
            if (!tabMap.containsKey(pendingTabId)) {
                this.addTab();
            }
            chartMap.put(pendingChartRequest.getChartId(), pendingChartRequest);
        }

        pendingChartRequest = null;
        pendingTab = null;
        pendingTabId = null;
    }


    /**
     * Remove a chart from a tab. This is called from PrimaryController when user clicks the remove
     * button on a chart. Or if a DataManager returns and exception and a chart can't be created.
     * 
     * @param chartId Unique UUID string for a chart to be removed
     */
    public void removeChart(String chartId) {
        String tabId = "";
        for (Map.Entry<String, TabInfo> entry : tabMap.entrySet()) {
            if (entry.getValue().removeChart(chartId)) {
                tabId = entry.getKey();
                break;
            }
        }

        SessionChangeData data = new SessionChangeData(SessionChangeType.CHART_REMOVED);
        data.setTabId(tabId);
        data.setChartId(chartId);

        chartMap.remove(chartId);
        notifyListeners(data);
    }


    /**
     * Retrieve parameters for creating a chart from the specified tab.
     * 
     * @param chartId Unique UUID string for which chart to choose from the tab
     * @return Object containing data required for a Chart creation task.
     */
    public ChartRequest getChartRequest(String requestBundleId) {
        // search chartmap for chartrequest with matching requestbundleid
        ChartRequest chartRequest = null;
        for (Map.Entry<String, ChartRequest> entry : chartMap.entrySet()) {
            if (entry.getValue().getRequestBundleId().equals(requestBundleId)) {
                chartRequest = entry.getValue();
                break;
            }
        }

        return chartRequest;
    }



    public String getTabIdForChart(String chartId) {
        String tabId = "";
        for (Map.Entry<String, TabInfo> entry : tabMap.entrySet()) {
            if (entry.getValue().getStoredCharts().contains(chartId)) {
                tabId = entry.getKey();
                break;
            }
        }
        return tabId;
    }


    /**
     * When selected tab changes in UI, this method is called to update currentTabId. This is used
     * to determine which tab is visible for user.
     * 
     * @param newTabId Unique UUID string for a tab which is selected
     */
    public void setCurrentTabId(String newTabId) {
        this.currentTabId = newTabId;
    }


    /**
     * Use to register a class as a listener to SessionController
     * 
     * @param listener Class which implements SessionControllerListener interface
     */
    public void registerListener(SessionControllerListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }


    /**
     * Remove a listener from the list of listeners
     * 
     * @param listener Class which implements SessionControllerListener interface
     */
    public void removeListener(SessionControllerListener listener) {
        listeners.remove(listener);
    }


    /**
     * Notify all registered listeners when something happens in SessionController.
     * 
     * @param data Object containing information about the state change
     */
    public void notifyListeners(SessionChangeData data) {
        for (SessionControllerListener listener : listeners) {
            listener.onSessionChange(data);
        }
    }
}
