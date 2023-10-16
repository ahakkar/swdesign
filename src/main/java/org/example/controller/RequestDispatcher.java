package org.example.controller;

import org.example.types.DataType;
import org.example.model.data.ChartRequest;
import org.example.model.data.DataRequest;

import java.time.LocalDateTime;


/**
 * Singleton. Vvalidates data from UI, and returns true/false depending on
 * if data was good. Data good? -> send request to datamanager. Bad? -> complain
 * to user via UI.
 * 
 * @author Antti Hakkarainen
 */
public class RequestDispatcher
{    
    private static RequestDispatcher instance;
    private RequestDispatcher() { }

    /**
     * Creates a new instance if one doesn't exist. Returns it.
     * @return instance of this class
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
     * Validates data from UI and passes it on to sessionmanager if it's good.
     * 
     * @param chartRequest  container for chart creation parameters
     * @param dataType      Which type of data we want to display in chart
     * @param startTime     Start of the range for data we want to display in chart
     * @param endTime       End of the range for data we want to display in chart
     * @param location      Ie. "Helsinki", "Tampere"
     * @param tabId         String representation of UUID of the tab
     * @return              True if data was good, false if not
     */
    public Boolean validateAddChartRequest(
        ChartRequest chartRequest,
        DataType dataType, 
        LocalDateTime startTime,
        LocalDateTime endTime,        
        String location,
        String tabId
    ) {
        // TODO actually validate data and don't just pass it on..
        DataRequest dataRequest = new DataRequest(dataType, startTime, endTime, location);
        SessionController sessionController = SessionController.getInstance();
        sessionController.newChartRequest(chartRequest, dataRequest, tabId);
        
        return true;
    }

/*     private LocalDateTime stringToLocalDateTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(timestamp, formatter);
    } */
}
