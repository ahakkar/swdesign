package org.example.model.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.model.api.APIQueue;
import org.example.model.data.*;
import org.example.types.DataType;
import org.example.utils.EnvironmentVariables;

/**
 * 
 * @author Janne Taskinen
 */
public final class DataManager {

    private static DataManager instance;
    private static DataStorage dataStorage;

    private final ArrayList<DataManagerListener> listeners;

    private DataManager() {
        this.listeners = new ArrayList<>();
        dataStorage = DataStorage.getInstance();
    }

    public static DataManager getInstance() {

        synchronized (DataManager.class) {
            if (instance == null) {
                instance = new DataManager();
            }
            return instance;
        }
    }

    public void getData(
        List<DataRequest> queries
    )  {

        if (!validateAllQueries(queries)){
            notifyListeners(null, new IllegalArgumentException("Invalid data query"));
            return;
        }

        List<DataResult> localData = getAllDataFromStorage(queries);

        if (localData == null){
            getDataFromAPIAndNotifyListeners(queries);
        }
        else {
            notifyListeners(localData, null);
        }


    }

    private boolean validateAllQueries(List<DataRequest> queries) {
        for (DataRequest query : queries) {
            if (!validateDataQuery(query)){
                return false;
            }
        }
        return true;
    }

    private void getDataFromAPIAndNotifyListeners(List<DataRequest> apiRequests) {
        List<DataResult> apiResults = new ArrayList<>();
        List<ApiDataRequest> apiDataRequests = new ArrayList<>();

        for (DataRequest query : apiRequests) {
            apiDataRequests.add(new ApiDataRequest(resolveModelClass(query.getDataType()), query));
        }

        APIQueue.getData(apiDataRequests, (results, exception) -> {
            if (exception == null) {
                for (ApiDataResult result : results) {
                    apiResults.add(new DataResult(result.getRequest().getDataRequest(), result.getResult()));
                    dataStorage.addData(result.getResult());
                }
                notifyListeners(apiResults, null);
            } else {
                notifyListeners(null, exception);
            }
        });
    }

    /**
     * Tries to get data from storage based on the queries.
     * If data is found from storage for ALL the queries, a list of DataResults will be returned.
     * If data is not found from storage for at least 1 query in the list, null will be returned.
     * @param queries List<DataRequest>
     * @return List<DataResult> or null
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
    public void registerListener(DataManagerListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            // TODO remove Registered %s as DataManager's listener debug print
            System.out.printf("Registered %s as DataManager's listener\n", listener.toString());
        }
    }


    /**
     * Remove a listener
     * @param listener
     */
    public void removeListener(DataManagerListener listener) {
        listeners.remove(listener);
    }


    /**
     * Notify all registered listeners when something happens
     */
    public void notifyListeners(List<DataResult> data, Exception exception) {
        for (DataManagerListener listener : listeners) {
            listener.onDataReadyForChart(data, exception);
        }
    }

}
