package fi.nordicwatt.model.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import fi.nordicwatt.Constants;
import fi.nordicwatt.model.api.APIDataListener;
import fi.nordicwatt.model.api.APIQueue;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.data.DataResponse;
import fi.nordicwatt.model.datamodel.AbstractDataModel;
import fi.nordicwatt.model.datamodel.EnergyModel;
import fi.nordicwatt.model.datamodel.RequestBundle;
import fi.nordicwatt.model.datamodel.ResponseBundle;
import fi.nordicwatt.model.datamodel.SettingsData;
import fi.nordicwatt.model.datamodel.WeatherModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.DataNotFoundException;
import fi.nordicwatt.utils.Logger;

/**
 * 
 * 
 * @author Janne Taskinen
 */
public class DataManager implements APIDataListener {

    private static DataManager instance;
    private static DataStorage dataStorage;
    private static PresetManager presetManager;
    private static APIQueue apiQueue;
    private final ArrayList<DataManagerListener> listeners;

    private DataManager() {
        this.listeners = new ArrayList<>();
        DataManager.dataStorage = DataStorage.getInstance();
        DataManager.presetManager = PresetManager.getInstance();
        apiQueue = APIQueue.getInstance();
        apiQueue.registerListener(this);
    }

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }


    /**
     * Processes a bundle of data requests. Gets data from local storage if available, otherwise
     * from API. Emits a success notification if all data was successfully retrieved. Emits a
     * failure notification if even one request failed. Exceptions bubble up to this method, and are
     * finally forwarded to Controller.
     * 
     * @param requests
     */
    public void getData(RequestBundle requests) {
        try {
            validateAllRequests(requests);
            Logger.log("DATAMANAGER: VALIDATION OK");
            ResponseBundle data = getAllDataFromStorage(requests);
            if (data == null) {
                Logger.log("DATAMANAGER: FETCHING DATA FROM API");
                apiQueue.getData(requests);
                return;
            }

            Logger.log("DATAMANAGER: DATA RETURNED LOCALLY");
            notifyDataRequestSuccess(data);
        } catch (Exception e) {
            Logger.log("[DataManager]: getData() failed: " + e.getMessage());
            notifyDataRequestFailure(requests, e);
        }
    }


    /**
     * Tries to get data from storage based on the queries. If data is found from storage for ALL
     * the queries, a list of DataResults will be returned. If data is not found from storage for at
     * least 1 query in the list, null will be returned.
     * 
     * @param queries To be sent to storage
     * @return List<DataResult> or null
     * @throws DataNotFoundException if data is not found from storage
     */

    private ResponseBundle getAllDataFromStorage(RequestBundle requests)
            throws DataNotFoundException {
        ResponseBundle response = new ResponseBundle(requests.getId());

        for (DataRequest request : requests.getItems()) {
            AbstractDataModel<Double> data = dataStorage.getData(request);
            if (data == null) {
                return null;
            } else {
                response.addItem(new DataResponse(request, data));
            }
        }
        return response;
    }


    /**
     * Validates all data requests in a bundle.
     * 
     * @param requests Bundle of datarequests
     * @return true if all requests are valid
     * @throws IllegalArgumentException even a single data request is invalid
     */
    private boolean validateAllRequests(RequestBundle requests) throws IllegalArgumentException {
        for (DataRequest query : requests.getItems()) {
            if (!validateDataRequest(query)) {
                Logger.log(
                        "[DataManager]: validateAllRequests: Invalid DataRequest " + query.getId());
                throw new IllegalArgumentException(
                        "[DataManager]: Invalid DataRequest " + query.getId());
            }
        }
        return true;
    }


    private boolean validateDataRequest(DataRequest request) {
        if (request == null || request.getStarttime() == null || request.getEndtime() == null
                || request.getDataType() == null) {
            return false;
        }

        if (request.getStarttime().isAfter(request.getEndtime())
                || request.getStarttime().equals(request.getEndtime())) {
            return false;
        }

        return checkIfModelClassExists(request.getDataType()) != null;
    }


    /**
     * Resolve the model class based on the datatype
     * 
     * @param dataType Enum DataType
     * @return Class<? extends AbstractModel<Double>> Base class for different data models
     */
    private Class<? extends AbstractDataModel<Double>> checkIfModelClassExists(DataType dataType) {
        switch (dataType.getAPIType()) {
            case FINGRID:
                return EnergyModel.class;
            case FMI:
                return WeatherModel.class;
            default:
                return null;
        }
    }

    @Override
    public void APIDataRequestSuccess(ResponseBundle responses) {
        for (DataResponse response : responses.getItems()) {
            dataStorage.addData(response.getData());
        }
        notifyDataRequestSuccess(responses);
        Logger.log("DATAMANAGER: DATA RETURNED FROM API");
    }

    @Override
    public void APIDataRequestFailure(RequestBundle requests, Exception e) {
        notifyDataRequestFailure(requests, e);
    }

    /**
     * Notify listeners that data request was successful, and provide the responses
     * 
     * @param responses Bundle of responses containing data for each request
     */
    public void notifyDataRequestSuccess(ResponseBundle responses) {
        for (DataManagerListener listener : listeners) {
            listener.dataRequestSuccess(responses);
        }
    }


    /**
     * Notify listeners that data request failed
     * 
     * @param requests Bundle of requests containing their individual Status
     * @param e Exception that caused the failure
     */
    public void notifyDataRequestFailure(RequestBundle requests, Exception e) {
        for (DataManagerListener listener : listeners) {
            listener.dataRequestFailure(requests, e);
        }
    }

    /**
     * Use to register a class as a listener
     * 
     * @param listener
     */
    public boolean registerListener(DataManagerListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            return true;
        }
        return false;
    }

    /**
     * Remove a listener
     * 
     * @param listener
     */
    public boolean removeListener(DataManagerListener listener) {
        return listeners.remove(listener);
    }

    /**
     * Sends the settings to be saved to PresetManager.
     * 
     * @param id
     * @param settingsData
     * @throws IOException
     */
    public void savePreset(String id, SettingsData settingsData) throws IOException {
        presetManager.saveSettingsData(id, settingsData);
    }

    /**
     * Loads the setting from PresetManager.
     * 
     * @param id
     * @return The saved settings as SettingsData object.
     * @throws IOException
     */
    public SettingsData loadPreset(String id) throws IOException {
        return presetManager.loadSettingsData(id);
    }

    /**
     * Loads all the stored presets' ids from PresetManager.
     * 
     * @return The ids in an ArrayList
     */
    public ArrayList<String> getPresetIds() throws IOException {
        return presetManager.getPresetIds();
    }

    public ArrayList<String> loadLocations() {
        ArrayList<String> locations = new ArrayList<>();
        try {
            File file = new File(Constants.LOCATIONS_FILEPATH);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

            String town;
            while ((town = br.readLine()) != null) {
                town = town.trim();
                locations.add(town);
            }
            br.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locations;
    }

}
