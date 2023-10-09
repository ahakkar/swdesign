package org.example.controller;

import javafx.scene.chart.XYChart;
import org.example.model.data.DataRequest;
import org.example.model.services.DataManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
     * Validates data from UI and passes it on to datamanager
     * @return
     */
    public Boolean handleDataRequest() {
        // TODO do something with the data
        List<DataRequest> requests = new ArrayList<>();
        requests.add(new DataRequest("TOTAL_CONSUMPTION", stringToLocalDateTime("2023-10-08 15:30:45"), stringToLocalDateTime("2023-10-10 15:30:45"), "location"));
        requests.add(new DataRequest("WIND", stringToLocalDateTime("2023-10-08 15:31:45"), stringToLocalDateTime("2023-10-10 15:32:45"), "location"));

        DataManager.getInstance().getData(requests);
        return false;
    }

    private LocalDateTime stringToLocalDateTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(timestamp, formatter);
    }
}
