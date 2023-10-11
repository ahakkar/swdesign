package org.example.controller;

import org.example.types.DataType;
import org.example.model.data.ChartRequest;
import org.example.model.data.DataRequest;

import java.time.LocalDateTime;


/**
 * Singleton. Probably validates data from UI, and returns true/false depending on
 * if data was good. Data good? -> send request to datamanager
 * 
 * @author Antti Hakkarainen
 */
public class RequestDispatcher {
    
    private static RequestDispatcher instance;

    private RequestDispatcher() { }


    /**
     * Creates a new instance if one doesn't exist. Returns it.
     * @return RequestDispatcher instance
     */
    public static RequestDispatcher getInstance() {
        synchronized (RequestDispatcher.class) {
            if (instance == null) {
                instance = new RequestDispatcher();
            }
            return instance;
        }
    }


    /**
     * Validates data from UI and passes it on to sessionmanager
     * @return
     */
    public Boolean validateAddChartRequest(
        ChartRequest chartRequest,
        DataType dataType, 
        LocalDateTime startTime,
        LocalDateTime endTime,        
        String location,
        String tabId
    ) {
        // TODO validate data
        DataRequest dataRequest = new DataRequest(dataType, startTime, endTime, location);
        SessionController sessionManager = SessionController.getInstance();
        sessionManager.newChartRequest(chartRequest, dataRequest, tabId);
        
        return true;
    }

/*     private LocalDateTime stringToLocalDateTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(timestamp, formatter);
    } */
}
