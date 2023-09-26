package org.example.model.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.model.data.DataPoint;
import org.example.model.data.PrimaryModel;

public class PrimaryService {

    private static PrimaryService instance;
    private final ObservableList<PrimaryModel> primaryModelList;

    private PrimaryService() {
        primaryModelList = FXCollections.observableArrayList();

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


    public ObservableList<PrimaryModel> getSomething(){
        return this.primaryModelList;
    }


}
