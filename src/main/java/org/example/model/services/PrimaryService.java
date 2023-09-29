package org.example.model.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.model.api.APIDataListener;
import org.example.model.api.APIQueue;
import org.example.model.data.*;

public class PrimaryService implements APIDataListener {

    private static PrimaryService instance;

    private APIQueue apiQueue;
    private final ObservableList<PrimaryModel> primaryModelList;

    private PrimaryService() {
        primaryModelList = FXCollections.observableArrayList();
        apiQueue = APIQueue.getInstance();
        apiQueue.addListener(this);
    }

    public static PrimaryService getInstance(){

        synchronized (PrimaryService.class){
            if (instance == null){
                instance = new PrimaryService();
            }
            return instance;
        }
    }


    /**
     * Fetch electricity consumption data from Fingrid 
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


    public void getSomething(){
        int i;
        for (i = 1; i <= 10; i++){
            System.out.println("PrimaryService: starting api call " + i);
            apiQueue.newDataRequired(new ApiDataRequest(Weather.class));
        }
        System.out.println("PrimaryService: called the apiqueue " + (i - 1) + " times");
    }


    @Override
    public void newApiDataAvailable() {
        System.out.println("PrimaryService got notification about new API data available");
        ApiDataResult result = apiQueue.getApiDataResult();
        System.out.println("Result data: " + result.test);



    }
}
