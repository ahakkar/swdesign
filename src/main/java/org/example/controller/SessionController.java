package org.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.model.data.ChartRequest;
import org.example.model.data.DataRequest;
import org.example.model.data.DataResult;
import org.example.model.services.DataManager;
import org.example.model.services.DataManagerListener;
import org.example.model.session.SessionChangeData;
import org.example.model.session.TabInfo;
import org.example.model.session.TabManager;
import org.example.types.SessionChangeType;

/**
 * Centralized point of truth for the state of the application. Session is
 * loaded from and saved to model (via datamanager maybe?). Stores instance of
 * a tabmanager, and instances of chartmanagers inside the tabs. Gets updates to
 * the session's state made by user, emits changes to session state to 
 * primarycontroller. Then primarycontroller can make the state changes visible
 * in UI.
 * 
 * @author Antti Hakkarainen
 */
public class SessionController implements DataManagerListener {
    private static SessionController instance;
    private final TabManager tabManager = TabManager.getInstance();
    private final DataManager dataManager = DataManager.getInstance();
    private ArrayList<SessionControllerListener> listeners;

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

    private SessionController() {
        this.listeners = new ArrayList<>();
        dataManager.registerListener(this);
    }

    // TODO implement later
    public void loadSession() {

    }

    // TODO implement later
    public void saveSession() {

    }

    // TODO implement later
    public void resetSession() {
    }
        

    public void addTab() {
        TabInfo info = tabManager.addTab();
        // TODO give error msg to user if tab count is at maximum
        if(info.getId() == null) {
            return;
        }
        SessionChangeData data = new SessionChangeData(SessionChangeType.TAB_ADDED);  
        data.setId(info.getId());
        data.setTitle(info.getTitle());      
        notifyListeners(data);
    }    

    public void removeTab(String tabId) {
        tabManager.removeTab(tabId);
        SessionChangeData data = new SessionChangeData(SessionChangeType.TAB_REMOVED);  
        data.setId(tabId);
        notifyListeners(data);
    }

    /**
     * Called from SessionManager when a new empty chart is added to a tab.
     * Data will be added to the chart later.
     * 
     * @param tabId
     * @param data
     */
    public String addChart(
        String tabId,
        ChartRequest request
    ) {
        String chartId = tabManager.addChartToTab(tabId, request);
        SessionChangeData change = new SessionChangeData(SessionChangeType.CHART_ADDED);
        change.setId(tabId);
        change.setChartRequest(request);

        notifyListeners(change);
        return chartId;
    }


    public void newChartRequest(
        ChartRequest chartRequest,
        DataRequest dataRequest,
        String tabId
    ) {
        String chartId = this.addChart(tabId, chartRequest);

        if (chartId.equals("")) {
            // TODO error msg to user
            return;
        }
        // add tabid and chartid to datarequest so we know later where the 
        // data belongs to, when it's fetched..
        dataRequest.setChartMetadata(
            tabId,
            chartId        
        );
        List<DataRequest> list = new ArrayList<DataRequest>();
        dataManager.getData(list);
    }

    public void onDataReadyForChart(String tabId, ChartRequest request, List<DataResult> data) {

    }

    public ChartRequest getChartRequest(String tabId, String chartId) {
        return tabManager.getChartRequest(tabId, chartId);
    }

   
    /**
     * Use to register a class as a listener
     * @param listener
     */
    public void registerListener(SessionControllerListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            // TODO remove Registered %s as SessionManager's listener debug print
            System.out.printf("Registered %s as SessionManager's listener\n", listener.toString());
        }
    }


    /**
     * Remove a listener
     * @param listener
     */
    public void removeListener(SessionControllerListener listener) {
        listeners.remove(listener);
    }


    /**
     * Notify all registered listeners when something happens
     */
    public void notifyListeners(SessionChangeData data) {
    for (SessionControllerListener listener : listeners) {
        listener.onSessionChange(data);
    }
}
}
