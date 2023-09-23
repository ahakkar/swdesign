package org.example.Services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.example.Models.DataPoint;

/**
 * Implements Fingrid's api interface
 * 
 * @author Antti Hakkarainen  
 */
public class FingridApiDataSource implements IFingridApiDataSource {

    FingridApiService fs;
    private String jsonData;

    public FingridApiDataSource() {
        fs = new FingridApiService();
    }

    @Override
    public List<DataPoint> getFixedData(String dataType, LocalDate start, LocalDate end) throws IOException { 
        jsonData = fs.fetchData(dataType, start, end);
        return FingridDataParser.parseJsonData(jsonData);
    }

}