package fi.nordicwatt.model.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fi.nordicwatt.model.api.APIQueue;
import fi.nordicwatt.model.data.AbstractDataModel;
import fi.nordicwatt.model.data.ApiDataRequest;
import fi.nordicwatt.model.data.ApiDataResult;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.data.DataResult;
import fi.nordicwatt.model.data.EnergyModel;
import fi.nordicwatt.model.data.SettingsData;
import fi.nordicwatt.model.data.WeatherModel;
import fi.nordicwatt.types.DataType;

/**
 * 
 * 
 * @author Janne Taskinen
 */
public final class DataManager {

    private static DataManager instance;
    private static DataStorage dataStorage;
    private static PresetParsing presetParsing;

    private final ArrayList<DataManagerListener> listeners;

    private DataManager() {
        this.listeners = new ArrayList<>();
        DataManager.dataStorage = DataStorage.getInstance();
        DataManager.presetParsing = PresetParsing.getInstance();
    }

    public static DataManager getInstance() {

        synchronized (DataManager.class) {
            if (instance == null) {
                instance = new DataManager();
            }
            return instance;
        }
    }


    /**
     * 
     * 
     * @param queries
     */
    public void getData(List<DataRequest> queries)
    {
        if (!validateAllQueries(queries)){
            notifyListeners(createEmptyDataResultList(queries), new IllegalArgumentException("Invalid data query"));
            return;
        }

        List<DataResult> localData = getAllDataFromStorage(queries);

        if (localData == null){
            getDataFromAPIAndNotifyListeners(queries);
        }
        else {
            notifyListeners(localData, null);
            System.out.println("DATAMANAGER: DATA RETURNED LOCALLY");
        }


    }


    /**
     * 
     * 
     * @param queries
     * @return
     */
    private boolean validateAllQueries(List<DataRequest> queries) {
        for (DataRequest query : queries) {
            if (!validateDataQuery(query)){
                throw new IllegalArgumentException("DataManager: Invalid data query");
            }
        }
        return true;
    }

    private List<DataResult> createEmptyDataResultList(List<DataRequest> requests){
        List<DataResult> results = new ArrayList<>();
        for (DataRequest request : requests) {
            results.add(new DataResult(request, null));
        }
        return results;
    }


    /**
     * 
     * 
     * @param apiRequests
     */
    private void getDataFromAPIAndNotifyListeners(List<DataRequest> apiRequests)
    {
        List<DataResult> apiResults = new ArrayList<>();
        List<ApiDataRequest> apiDataRequests = new ArrayList<>();

        for (DataRequest query : apiRequests) {
            apiDataRequests.add(new ApiDataRequest(query));
        }

        APIQueue.getData(apiDataRequests, (results, exception) -> {
            if (exception == null) {
                for (ApiDataResult result : results) {
                    apiResults.add(new DataResult(result.getRequest().getDataRequest(), result.getResult()));
                    dataStorage.addData(result.getResult());
                }
                notifyListeners(apiResults, null);
                System.out.println("DATAMANAGER: DATA RETURNED FROM API");
            } else {
                notifyListeners(createEmptyDataResultList(apiRequests), exception);
            }
   
        });
    }

    /**
     * Tries to get data from storage based on the queries.
     * If data is found from storage for ALL the queries, a list of DataResults will be returned.
     * If data is not found from storage for at least 1 query in the list, null will be returned.
     * 
     * @param queries To be sent to storage
     * @return        List<DataResult> or null
     */

    private List<DataResult> getAllDataFromStorage(List<DataRequest> queries) {
        List<DataResult> dataQueryResults = new ArrayList<>();
        for (DataRequest query : queries) {
            AbstractDataModel<Double> model = dataStorage.getData(query);
            if (model == null){
                return null;
            }
            else {
                dataQueryResults.add(new DataResult(query, model));
            }
        }
        return dataQueryResults;
    }


    private boolean validateDataQuery(DataRequest query) {
        if (query == null || query.getStarttime() == null || query.getEndtime() == null || query.getDataType() == null) {
            return false;
        }

        if (query.getStarttime().isAfter(query.getEndtime()) || query.getStarttime().equals(query.getEndtime())) {
            return false;
        }

        return resolveModelClass(query.getDataType()) != null;
    }


    /**
     * Resolve the model class based on the datatype
     * 
     * @param dataType Enum DataType
     * @return Class<? extends AbstractDataModel<Double>> Base class for different data models
     */
    private Class<? extends AbstractDataModel<Double>> resolveModelClass(DataType dataType) {
        switch (dataType.getAPIType()) {
            case FINGRID:
                return EnergyModel.class;
            case FMI:
                return WeatherModel.class;
            default:
                return null;
        }
    }


    /**
     * Use to register a class as a listener
     * @param listener
     */
    public boolean registerListener(DataManagerListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            // TODO remove Registered %s as DataManager's listener debug print
            System.out.printf("Registered %s as DataManager's listener\n", listener.toString());
            return true;
        }
        return false;
    }


    /**
     * Remove a listener
     * @param listener
     */
    public boolean removeListener(DataManagerListener listener) {
        return listeners.remove(listener);
    }


    /**
     * Notify all registered listeners when something happens
     * 
     */
    public void notifyListeners(
        List<DataResult> data,
        Exception exception
    ) {
        for (DataManagerListener listener : listeners) {
            listener.onDataReadyForChart(data, exception);
        }
    }

    /**
     * Sends the settings to be saved to PresetParsing.
     * @param xAxis
     * @param yAxis
     * @param starttime
     * @param endtime
     * @param location
     * @throws IOException
     */
    public void savePreset(DataType xAxis, DataType yAxis, LocalDate starttime, LocalDate endtime, String location) throws IOException
    {
        PresetParsing.saveSettings(xAxis, yAxis, starttime, endtime, location);
    }

    /**
     * Loads the setting from PresetParsing.
     * @return The saved settings as SettingsData object.
     * @throws IOException
     */
    public SettingsData loadPreset() throws IOException
    {
        return PresetParsing.loadSettings();
    }

}
