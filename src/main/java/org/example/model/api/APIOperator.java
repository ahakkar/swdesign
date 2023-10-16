package org.example.model.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeFormatter;

import okhttp3.Response;

import org.example.model.api.APIParserInterface.ParseException;
import org.example.model.data.*;

/**
 * APIOperator - Class for handling API requests.
 * 
 * @author Janne Taskinen       initial stub
 * @author Heikki Hohtar        igetEnergyModelData
 * @author Markus Hissa         getWeatherModelData
 * @author Antti Hakkarainen    slight refactoring
 */
public class APIOperator {

    private DataRequest dataRequest;
    private ApiDataRequest apiDataRequest;

    protected ApiDataResult getData(ApiDataRequest request)
            throws InterruptedException, IOException, IllegalArgumentException {

        this.dataRequest = request.getDataRequest();
        this.apiDataRequest = request;

        switch (request.getDataRequest().getDataType().getAPIType()) {
            case FMI:
                return getWeatherModelData();
            case FINGRID:
                return getEnergyModelData();
            default:
                throw new IllegalArgumentException("Proper API not found for the request");
        } 
    }


    /**
     * getWeatherModelData - Gets weather data from FMI API.
     */
    private ApiDataResult getWeatherModelData() {
        URL url;
        try {
            url = new URL(
                "http://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::observations::weather::multipointcoverage&place="
                    + dataRequest.getLocation() + "&parameters=" + dataRequest.getDataType() + "&starttime="
                    + dataRequest.getStarttime() + "&endtime=" + dataRequest.getEndtime() + "&timestep=60");

            System.out.println(url);
        
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();

            // Listen to what the server responds, parse to string, parse to data object, return...
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                
                reader.close();
                inputStream.close();

                connection.disconnect();
                
                String responseString = stringBuilder.toString();

                FMIApiParser parser = new FMIApiParser();
                WeatherModel weatherData = parser.parseToDataObject(apiDataRequest, responseString);
                return new ApiDataResult(weatherData, apiDataRequest);
            } 
            else {
                System.out.println("Failed to get a valid response. HTTP code: " + responseCode);
            }

            



            
        } catch (MalformedURLException e) {   
            System.err.println("[APIOperator]: Malformed URL");         
            e.printStackTrace();            
        } catch (IOException e) {
            System.err.println("[APIOperator]: IO Exception");
            e.printStackTrace();  
        } 
        
        return null;
    }

    private ApiDataResult getEnergyModelData() {
        FingridAPIRequestBuilder builder = new FingridAPIRequestBuilder()
            .withDataType(dataRequest.getDataType().getVariableId())
            .withStartTime(dataRequest.getStarttime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
            .withEndTime(dataRequest.getEndtime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));

        try (Response response = builder.execute()) {
            FingridApiParser parser = new FingridApiParser();
            
            EnergyModel energyData = parser.parseToDataObject(apiDataRequest, response.body().string());
            ApiDataResult result = new ApiDataResult(energyData, apiDataRequest);

            return result;            

        } catch (IOException e) {
            System.err.println("[APIOperator]: IO Exception");
            System.err.println("Cause:" + e.getCause());
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("[APIOperator]: ParseException while trying to parse API response to DataModel");
            System.err.println("Cause:" + e.getCause());
            e.printStackTrace();
        }
        
        return null;
    }
}
