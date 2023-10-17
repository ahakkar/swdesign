package fi.nordicwatt.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.CustomAlerts;
import javafx.scene.control.Alert.AlertType;


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
        if (dataType == null) {
            CustomAlerts.displayAlert(
                AlertType.ERROR,
                "Error in RequestDispatcher",
                "Validation of dataType failed.\nDataType is null."
                );
            return false;
        }

        if (startTime == null || endTime == null) {
            CustomAlerts.displayAlert(
                AlertType.ERROR,
                "Error in RequestDispatcher",
                "Invalid start or end time."
                );
            return false;
        }

        if (startTime.isEqual(endTime) || startTime.isAfter(endTime)) {
            CustomAlerts.displayAlert(
                AlertType.ERROR,
                "Error in RequestDispatcher",
                "Start date must be at least a day before end date."
                );
            return false;
        }

        // TODO use regexp to support more chars... äöå etc.
        if (location == null || location.trim().isEmpty() || !location.matches("^[a-zA-Z ]+$")) {
            CustomAlerts.displayAlert(
                AlertType.ERROR,
                "Error in RequestDispatcher",
                "Invalid location.\n Location should contain only letters and spaces."
                );
            return false;
        }

        try {
            UUID.fromString(tabId);
        } catch (IllegalArgumentException e) {
            CustomAlerts.displayAlert(
                AlertType.ERROR,
                "Error in RequestDispatcher",
                "Selected tab is invalid.\nTabId is not a valid UUID."
                );
            return false;
        }

        DataRequest dataRequest = new DataRequest(dataType, startTime, endTime, location);
        SessionController sessionController = SessionController.getInstance();
        sessionController.newChartRequest(chartRequest, dataRequest, tabId);
        
        return true;
    }

}
