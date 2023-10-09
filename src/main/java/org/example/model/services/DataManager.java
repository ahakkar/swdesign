package org.example.model.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.model.api.APIQueue;
import org.example.model.data.*;

/**
 * 
 * @author Janne Taskinen
 */
public final class DataManager {

    private static DataManager instance;
    private static DataStorage dataStorage;

    private ArrayList<DataManagerListener> listeners;

    private DataManager() {
        this.listeners = new ArrayList<>();
        this.dataStorage = DataStorage.getInstance();
    }

    public static DataManager getInstance() {

        synchronized (DataManager.class) {
            if (instance == null) {
                instance = new DataManager();
            }
            return instance;
        }
    }

    public void getData(List<DataRequest> queries) throws IllegalArgumentException {
        List<DataResult> dataQueryResults = new ArrayList<>();
        List<ApiDataRequest> apiRequests = new ArrayList<>();

        for (DataRequest query : queries) {
            if (!validateDataQuery(query)){
                throw new IllegalArgumentException("Invalid data query");
            }

            AbstractDataModel<Double> model = dataStorage.getData(query);

            if (model == null){
                Class modelClass = resolveModelClass(query.getDataType());
                apiRequests.add(new ApiDataRequest(modelClass, query));
            }
            else {
                dataQueryResults.add(new DataResult(query, model));
            }
        }

        if (!apiRequests.isEmpty()){
            APIQueue.getData(apiRequests, results -> {
                for (ApiDataResult result : results){
                    /*dataQueryResults.add(new DataResult(result.getRequest().getDataRequest(), result.getResult()));
                    dataStorage.addData(result.getResult());*/
                }
                notifyListeners(dataQueryResults);
            });
        }
        else {
            notifyListeners(dataQueryResults);
        }
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

    private Class resolveModelClass(String dataType) {
        // TODO refactor this when we have decided where we are actually maintaining the allowed types...
        try {
             EnergyModel.DataType.parseDataType(dataType);
             return EnergyModel.class;

        }
        catch (IllegalArgumentException ignored) {

        }

        // TODO refactor this when we have decided where we are actually maintaining the allowed types...
        try {
            WeatherModel.DataType.parseDataType(dataType);
            return WeatherModel.class;

        }
        catch (IllegalArgumentException e2) {

        }
        return null;
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
    public void notifyListeners(List<DataResult> data) {
        for (DataManagerListener listener : listeners) {
            listener.onDataReady(data); // TODO pass actual data
        }
    }
    
}
