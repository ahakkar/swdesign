package fi.nordicwatt.controller;

import java.util.ArrayList;
import java.util.List;

import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.data.DataResult;
import fi.nordicwatt.model.services.DataManager;
import fi.nordicwatt.model.services.DataManagerListener;
import fi.nordicwatt.model.session.SessionChangeData;
import fi.nordicwatt.model.session.TabInfo;
import fi.nordicwatt.model.session.TabManager;
import fi.nordicwatt.types.SessionChangeType;

/**
 * Centralized point of truth for the state of the application. Session is
 * loaded from and saved to model (via datamanager maybe?). Stores instance of
 * a tabmanager, and instances of chartmanagers inside the tabs. Gets updates to
 * the session's state made by user, emits changes to session state to 
 * primarycontroller. Then primarycontroller can make the state changes visible
 * in UI.
 * 
 * TODO store UI control values with bidirectionalbinding here too somewhere
 * 
 * @author Antti Hakkarainen
 */
public class SessionController implements DataManagerListener {
    private static SessionController instance;
    private final TabManager tabManager = TabManager.getInstance();
    private final DataManager dataManager = DataManager.getInstance();
    private ArrayList<SessionControllerListener> listeners;

    // TODO should be somehow synced bidirectionally with UI
    private String currentTabId; 

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
        this.listeners = new ArrayList<>();
        dataManager.registerListener(this);
    }


    /**
     * Request session data from DataManager and recreate UI component
     * selections, tabs and their charts based on the restored session.
     * 
     * TODO implement loadSession functionality
     */
    public void loadSession() {

    }


    /**
     * Save session data to DataManager so we can use it to recreate UI component
     * selections, tabs and their charts later.
     * 
     * TODO implement saveSession functionality
     */
    public void saveSession() {

    }


    /**
     * Reset all tabs, charts, and UI selections to default values.
     * (=basically removes all tabs and charts)
     * 
     * TODO imolement resetSession functionality
     */
    public void resetSession() {
    }


    /**
     * When selected tab changes in UI, this method is called to update
     * currentTabId. This is used to determine which tab is visible for user.
     * 
     * @param newTabId   Unique UUID string for a tab which is selected
     */
    public void setCurrentTabId(String newTabId) {
        this.currentTabId = newTabId;
    }


    /**
     * Called from RequestDispatcher after user submitted chart parameters
     * are validated. Sends a DataRequest to DataManager. DataManager will
     * fetch data from API and notify PrimaryController when data is ready.
     * Unique tabId and chartId are bundled to the DataRequest so we can match
     * received data to the correct chart.
     * 
     * @param chartRequest ChartRequest object
     * @param dataRequest  DataRequest object
     * @param tabId        Unique UUID string for a tab where the chart will go
     */
    public void newChartRequest (
        ChartRequest chartRequest,
        DataRequest dataRequest,
        String tabId
    ) throws IllegalArgumentException {
        if (tabId.equals(null)) {
            throw new IllegalArgumentException("[SessionController]: tabId is null");
        }
        String chartId = this.addChart(tabId, chartRequest);

        if (chartId.equals(null)) {
            System.out.println("[SessionController]: chartId is null");
            return;
        }

        dataRequest.setChartMetadata(tabId, chartId);
        tabManager.addDataRequestToChart(tabId, chartId, dataRequest);
        
        List<DataRequest> list = new ArrayList<DataRequest>();
        list.add(dataRequest);
        dataManager.getData(list);
  
    }


    /**
     * When something needs to know to which tab the chart should go, this
     * method can be used to get the tab's UUID. Behind the scenes the selected
     * tab's id is returned, or a new tab is created if no tabs exist or the
     * current tab is full of charts. 
     * 
     * @return tabId Unique UUID string for a tab where the chart will go
     */
    public String getTabIdForChart() {
        if(currentTabId != null) {
            if(tabManager.isTabFull(currentTabId)) {
                this.addTab();
            }
            return currentTabId;
        }
        return this.addTab();
    }
        

    /**
     * Adds a tab to the session and changes currentTabId to the new tabId.
     * TabManager will create a new tab and returna TabInfo object containing
     * the tab's UUID and title. This info is then used to create a new tab 
     * in UI, by sending a notification to SessionController's listeners.
     * 
     * TODO give error msg to user if tab count is at maximum
     */
    public String addTab() {
        TabInfo info = tabManager.addTab();
        SessionChangeData data = new SessionChangeData(SessionChangeType.TAB_ADDED);  
        data.setTabId(info.getId());
        data.setTitle(info.getTitle());
        this.currentTabId = info.getId();  

        notifyListeners(data);  // Used to notify PrimaryController to add a new Tab to UI

        return currentTabId;
    }    


    /**
     * Asks TabManager to remove a tab, and notifies SessionController's
     * listeners about the removal.
     * 
     * @param tabId     Unique UUID string for a tab to be removed
     */
    public void removeTab(String tabId) {
        tabManager.removeTab(tabId);
        SessionChangeData data = new SessionChangeData(SessionChangeType.TAB_REMOVED);  
        data.setTabId(tabId);
        notifyListeners(data);
    }

    /**
     * Called from SessionManager when a new empty chart is added to a tab.
     * A placeholder tab should be added to UI with the help of the event.
     * Data will be added to the chart later when it's ready.
     * 
     * @param tabId     Unique UUID string for a tab where the chart will go
     * @param request   Parameters which can be used to create a chart with 
     *                  ChartFactory
     */
    private String addChart(
        String tabId,
        ChartRequest request
    ) {       
        String chartId = tabManager.addChartToTab(tabId, request);
        SessionChangeData change = new SessionChangeData(SessionChangeType.CHART_ADDED);
        change.setTabId(tabId);
        change.setChartId(chartId);
        change.setChartRequest(request);

        // TODO use this? to tell PrimaryController to display a spinning widget on UI..
        notifyListeners(change);
        return chartId;
    }


    /**
     * Actually not used for anything. No idea what the idea behind this was -ah
     * 
     * @param tabId
     * @param request
     * @param data
     * 
     * TODO remove when really deemed unneccessary
     */
    public void onDataReadyForChart(String tabId, ChartRequest request, List<DataResult> data) {

    }


    /**
     * Retrieve parameters for creating a chart from the specified tab.
     * 
     * @param tabId   Unique UUID string where to look the chart params from
     * @param chartId Unique UUID string for which chart to choose from the tab
     * @return        Object containing data required for a Chart creation task.
     */
    public ChartRequest getChartRequest(String tabId, String chartId) {
        return tabManager.getChartRequest(tabId, chartId);
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
     * Notify all registered listeners when something happens in
     * SessionController.
     * 
     * @param data Object containing information about the state change
     */
    public void notifyListeners(SessionChangeData data) {
    for (SessionControllerListener listener : listeners) {
        listener.onSessionChange(data);
    }
}
}
