package org.example.model.services;

import java.util.ArrayList;

import org.example.model.data.DataPoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Parses the JSON data fetched from Fingrid's api
 * 
 * @author Antti Hakkarainen
 */
public class FingridDataParser {

    public static ArrayList<DataPoint> parseJsonData(String jsonData) {

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            dataPoints = mapper.readValue(jsonData, new TypeReference<ArrayList<DataPoint>>(){});            
        }
        catch (JsonProcessingException e) {
             e.printStackTrace();
        }      

        return dataPoints;
    }
}
