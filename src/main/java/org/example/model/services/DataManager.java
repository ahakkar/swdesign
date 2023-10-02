package org.example.model.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.model.api.APIDataListener;
import org.example.model.api.APIQueue;
import org.example.model.data.*;

/**
 * 
 * @author Janne Taskinen
 */
public class DataManager implements APIDataListener {

    private static DataManager instance;
    private final ArrayList<AbstractDataModel<Double>> dataModels;

    private APIQueue apiQueue;
    private ArrayList<DataManagerListener> listeners;

    private DataManager() {
        apiQueue = APIQueue.getInstance();
        apiQueue.addListener(this);
        dataModels = new ArrayList<>();
        this.listeners = new ArrayList<>();
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
     * Fetch electricity consumption data from Fingrid
     * 
     * @return
     */
    public List<DataPoint> getFingridFixedData(String dataType, LocalDate start, LocalDate end) {
        List<DataPoint> results = new ArrayList<>();

        FingridApiDataSource ds = new FingridApiDataSource();
        try {
            results = ds.getFixedData(dataType, start, end);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return results;
    }


    /**
     * TEST / DEBUGGING METHOD
     */
    public void getSomething() {
        int i;
        for (i = 1; i <= 10; i++) {
            System.out.println("PrimaryService: starting api call " + i);
            apiQueue.newDataRequired(new ApiDataRequest(WeatherModel.class, null, null, null, null, null));
        }
        System.out.println("PrimaryService: called the apiqueue " + (i - 1) + " times");
    }

    public void getDataFromDataManager(List<DataQuery> queries) throws IllegalArgumentException {
        for (DataQuery query : queries) {
            if (!validateDataQuery(query)){
                throw new IllegalArgumentException("Invalid data query");
            }

            // For each query check if we have the data in our models

            AbstractDataModel<Double> model = getDataFromInternalStorage(query);

            // If we have data in our models, create proper abstactdatamodels and notify listeners

            if (model == null){

            }
            // If we miss data make necessary API calls and notify listeners when data is available



        }




    }

    private AbstractDataModel<Double> getDataFromInternalStorage(DataQuery query) {

        AbstractDataModel<Double> model = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (AbstractDataModel<Double> dataModel : dataModels) {
            if (dataModel.getDataType().equals(query.getDataType())) {
                model = dataModel;
                break;
            }
        }

        if (model != null){


            String start = query.getStarttime().format(formatter);
            String end = query.getEndtime().format(formatter);

            Map<String, Double> dataPoints = model.getDataPointsWithRange(start, end);
            Double[] values = new Double[dataPoints.size()];

            int i = 0;
            for (Double val : dataPoints.values()){
                values[i] = val;
                ++i;
            }

            if (dataPoints.containsKey(start) && dataPoints.containsKey(end)){
                if (model instanceof EnergyModel){
                    return new EnergyModel(model.getDataType(), model.getUnit(), start, model.getInterval(), values);
                }
                else if (model instanceof WeatherModel){
                    return new WeatherModel(model.getDataType(), model.getUnit(), start, model.getInterval(), ((WeatherModel) model).getLocation(), values);
                }
            }


        }

        return null;
    }

    private boolean validateDataQuery(DataQuery query) {
        if (query == null || query.getStarttime() == null || query.getEndtime() == null || query.getDataType() == null) {
            return false;
        }

        if (query.getStarttime().isAfter(query.getEndtime()) || query.getStarttime().equals(query.getEndtime())) {
            return false;
        }

        // TODO refactor this when we have decided where we are actually maintaining the allowed types...
        try {
            EnergyModel.DataType.parseDataType(query.getDataType());
        }
        catch (IllegalArgumentException ignored) {

        }

        // TODO refactor this when we have decided where we are actually maintaining the allowed types...
        try {
            WeatherModel.DataType.parseDataType(query.getDataType());
        }
        catch (IllegalArgumentException e2) {
            return false;
        }

        return true;

    }

    @Override
    public void newApiDataAvailable() {
        System.out.println("PrimaryService got notification about new API data available");
        ApiDataResult result = apiQueue.getApiDataResult();
        System.out.println("Result data: " + result.test);

    }


    /**
     * Use to register a class as a listener
     * @param listener
     */
    public void registerListener(DataManagerListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            // TODO remove Registered %s as DataManager's listener debug print
            System.out.printf("Registered %s as DataManager's listener", listener.toString());
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
    public void notifyListeners() {
        for (DataManagerListener listener : listeners) {
            listener.onDataReady(null, null); // TODO pass actual data
        }
    }
    
}
