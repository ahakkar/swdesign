package fi.nordicwatt.model.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fi.nordicwatt.model.api.APIParserInterface.ParseException;
import fi.nordicwatt.model.api.fingrid.FingridApiParser;
import fi.nordicwatt.model.api.fingrid.FingridAPIRequestBuilder;
import fi.nordicwatt.model.api.fmi.FmiApiParser;
import fi.nordicwatt.model.api.fmi.FmiAPIRequestBuilder;
import fi.nordicwatt.model.data.*;
import fi.nordicwatt.model.datamodel.WeatherModel;
import fi.nordicwatt.model.datamodel.EnergyModel;

import fi.nordicwatt.utils.DateTimeConverter;
import okhttp3.Response;

/**
 * APIOperator - Class for handling API requests.
 * 
 * @author Janne Taskinen       initial stub
 * @author Heikki Hohtari       getEnergyModelData
 * @author Markus Hissa         getWeatherModelData
 * @author Antti Hakkarainen    moderate refactoring
 */
public class APIOperator {

    protected DataResponse getData(DataRequest request)
        throws  InterruptedException, 
                IOException, 
                IllegalArgumentException, 
                ParseException
    {
        switch (request.getDataType().getAPIType()) {
            case FMI:
                return getWeatherModelData(request);
            case FINGRID:
                return getEnergyModelData(request);
            default:
                throw new IllegalArgumentException("Proper API not found for the request");
        } 
    }


    /**
     * Fetches and parses data from FMI API
     * 
     * @return a single dataresponse for a single api call
     */
    private DataResponse getWeatherModelData(DataRequest request)
        throws ParseException
    {
        LocalDateTime startTime = DateTimeConverter.finnishTimeToGMTTime(request.getStarttime());
        LocalDateTime endTime = DateTimeConverter.finnishTimeToGMTTime(request.getEndtime());
        //LocalDateTime startTime = request.getStarttime();
        //LocalDateTime endTime = request.getEndtime();
        FmiAPIRequestBuilder builder = new FmiAPIRequestBuilder()
            .withLocation(request.getLocation())
            .withDataType(request.getDataType().getVariableId())
            .withStartTime(startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
            .withEndTime(endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
            .withTimestep("60");        

        try (Response httpResponse = builder.execute()) {
            if (!httpResponse.isSuccessful()) {   
                // TODO log exception
                throw new IOException("FMI Httprequest not successful. Code: " + httpResponse.code() + ", Message: " + httpResponse.message());
            }
            String jsonData = httpResponse.body().string();          
            FmiApiParser parser = new FmiApiParser();            
            WeatherModel data = parser.parseToDataObject(request, jsonData);
            DataResponse response = new DataResponse(request, data);

            return response;            

        } 
        catch (IllegalStateException e) {
            System.out.println("[APIOperator]: IllegalStateException");
            // TODO log exception
        }
        catch (IOException e) {
            System.out.println("[APIOperator]: IO Exception");
            // TODO log exception
        } catch (ParseException e) {
            System.out.println("[APIOperator]: ParseException while trying to parse API response to DataModel");
            // TODO log exception
        }
        catch (Exception e) {
            System.out.println("[APIOperator]: Exception");
            // TODO log exception
        }

        return null;
    }

    /**
     * Fetches and parses data from Fingrid API
     * 
     * @return a single dataresponse for a single api call
     */
    private DataResponse getEnergyModelData(DataRequest request) {        
        FingridAPIRequestBuilder builder = new FingridAPIRequestBuilder()
            .withDataType(request.getDataType().getVariableId())
            .withStartTime(request.getStarttime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
            .withEndTime(request.getEndtime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));

        try (Response httpResponse = builder.execute()) {
            if (!httpResponse.isSuccessful()) {   
                // TODO log exception
                throw new IOException("Fingrid Httprequest not successful. Code: " + httpResponse.code() + ", Message: " + httpResponse.message());
            }

            String jsonData = httpResponse.body().string();
            FingridApiParser parser = new FingridApiParser();
            EnergyModel data = parser.parseToDataObject(request, jsonData);
            DataResponse response = new DataResponse(request, data);

            return response;            

        } catch (IOException e) {
            System.err.println("[APIOperator]: IO Exception");
            // TODO log exception
        } catch (ParseException e) {
            System.err.println("[APIOperator]: ParseException while trying to parse API response to DataModel");
            // TODO log exception
        }
        
        return null;
    }
}
