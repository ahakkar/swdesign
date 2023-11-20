package fi.nordicwatt.controller;

import java.time.LocalDateTime;

import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.datamodel.RequestBundle;
import fi.nordicwatt.model.service.DataManager;
import fi.nordicwatt.types.AxisType;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.CustomAlerts;
import fi.nordicwatt.utils.Logger;
import javafx.scene.control.Alert.AlertType;


/**
 * Singleton. Vvalidates data from UI, and returns true/false depending on if data was good. Data
 * good? -> send request to datamanager. Bad? -> complain to user via UI.
 * 
 * @author Antti Hakkarainen
 */
public final class RequestDispatcher {
    private final SessionController sessionController = SessionController.getInstance();
    private final DataManager dataManager = DataManager.getInstance();
    private static RequestDispatcher instance;

    private RequestDispatcher() {}

    /**
     * Creates a new instance if one doesn't exist. Returns it.
     * 
     * @return instance of this class
     */
    public static RequestDispatcher getInstance() {
        if (instance == null) {
            synchronized (RequestDispatcher.class) {
                if (instance == null) {
                    instance = new RequestDispatcher();
                }
            }
        }
        return instance;
    }


    /**
     * Validates data from UI and passes it on to sessionmanager if it's good.
     * 
     * @param chartRequest container for chart creation parameters
     * @return True if data was good, false if not
     */
    public Boolean validateAddChartRequest(ChartRequest chartRequest) {
        // Check that the keys araxisMap are not null
        for (AxisType axisType : chartRequest.getAxisMap().keySet()) {
            if (axisType == null) {
                CustomAlerts.displayAlert(AlertType.ERROR, "Error in RequestDispatcher",
                        "Validation of axisMap failed.\nAxisMap contains null keys.");
                return false;
            }
        }

        // Check that values in axisMap are not null
        for (DataType dataType : chartRequest.getAxisMap().values()) {
            if (dataType == null) {
                CustomAlerts.displayAlert(AlertType.ERROR, "Error in RequestDispatcher",
                        "Validation of axisMap failed.\nAxisMap contains null values.");
                return false;
            }
        }

        LocalDateTime startTime = chartRequest.getStartTime();
        LocalDateTime endTime = chartRequest.getEndTime();

        if (startTime == null || endTime == null) {
            CustomAlerts.displayAlert(AlertType.ERROR, "Error in RequestDispatcher",
                    "Invalid start or end time.");
            return false;
        }

        if (startTime.isEqual(endTime) || startTime.isAfter(endTime)) {
            CustomAlerts.displayAlert(AlertType.ERROR, "Error in RequestDispatcher",
                    "Start date must be at least a day before end date.");
            return false;
        }


        String location = chartRequest.getLocation();
        if (location == null || location.trim().isEmpty()
                || !location.matches("^[a-zA-ZäöåÄÖÅ ]+$")) {
            CustomAlerts.displayAlert(AlertType.ERROR, "Error in RequestDispatcher",
                    "Invalid location.\n Location should contain only letters and spaces.");
            return false;
        }

        return true;

    }

    public Boolean dispatchRequest(ChartRequest chartRequest, boolean toNewTab) {
        RequestBundle bundle = new RequestBundle();

        for (AxisType axisType : chartRequest.getAxisMap().keySet()) {
            // If value in axisMap is Time then skip it
            if (chartRequest.getAxisMap().get(axisType) == DataType.TIME) {
                continue;
            }
            DataType dataType = chartRequest.getAxisMap().get(axisType);
            DataRequest dataRequest = new DataRequest(dataType, chartRequest.getStartTime(),
                    chartRequest.getEndTime(), chartRequest.getLocation());
            bundle.addItem(dataRequest);
        }
        chartRequest.setRequestBundle(bundle);
        Logger.log("Bundle is built. Dispatching chartrequest: " + chartRequest.toString());

        sessionController.newPendingChartRequest(chartRequest, toNewTab);
        dataManager.getData(chartRequest.getRequestBundle());

        return true;
    }

}
