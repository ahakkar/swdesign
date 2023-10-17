package org.example.model.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.example.model.data.ApiDataRequest;
import org.example.model.data.WeatherModel;
import org.example.types.DataType;

/**
 * FMIApiParser - Parses response from FMI API to WeatherModel.
 * 
 * @see APIParserInterface
 * @author Markus Hissa ? 
 */
public class FMIApiParser implements APIParserInterface<WeatherModel> {

    /**
     * parseToDataObject - Parses response to data object.
     * 
     * @param response - Response from API
     * @return Data object
     *
     */
    @Override
    public WeatherModel parseToDataObject(ApiDataRequest request, String response) throws ParseException {

        String responseDataAsString = getStringBetween(response, "<gml:doubleOrNilReasonTupleList>",
                "</gml:doubleOrNilReasonTupleList>");
        List<Double> responseData = extractDoubles(responseDataAsString);
        Double[] responseDataArray = new Double[responseData.size()];

        String location = getStringBetween(response, "<target:region codeSpace="+"\""+"http://xml.fmi.fi/namespace/location/region"+"\""+">", "</target:region>");
        CharSequence firstEntryTimestamp = getStringBetween(response, "<gml:beginPosition>", "</gml:beginPosition>");
        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(firstEntryTimestamp, originalFormat);
        String formattedTimestamp = dateTime.format(desiredFormat);
        DataType dataType = request.getDataRequest().getDataType();
        String unit = "";
        responseData.toArray(responseDataArray);

        switch (dataType) {
            case TEMPERATURE:
                unit = "C";
                break;
            case WIND:
                unit = "m/s";
                break;
            case AIR_PRESSURE:
                unit = "hPa";
                break;
            case HUMIDITY:
                unit = "%";
                break;
        }
        WeatherModel model = 
            new WeatherModel(
                dataType,
                unit,
                formattedTimestamp,
                location, 
                responseData.toArray(responseDataArray)
                );

            return model;    
    }

    /**
     * getStringBetween - Gets a string between two strings. Exclude the start and
     * end strings from the result.
     * 
     * @param originalStr Original string
     * @param startStr    Start string
     * @param endStr      End string
     * @return            String between start and end strings
     */
    public static String getStringBetween(String originalStr, String startStr, String endStr) {
        // Check if both startStr and endStr exist in originalStr
        if (originalStr.contains(startStr) && originalStr.contains(endStr)) {
            int startIndex = originalStr.indexOf(startStr) + startStr.length();
            int endIndex = originalStr.indexOf(endStr, startIndex); // Searching for endStr after the startIndex

            // Check if startStr is before endStr
            if (startIndex < endIndex) {
                return originalStr.substring(startIndex, endIndex).trim();
            }
        }
        return ""; // Return an empty string if conditions are not met
    }

    /**
     * extractDoubles - Extracts doubles from a string.
     * 
     * @param input String containing doubles
     * @return      List of all found doubles
     */
    public static List<Double> extractDoubles(String input) {
        String[] tokens = input.split("\\s+"); // Split by one or more spaces

        List<Double> parsedNumbers = new ArrayList<>();

        for (String token : tokens) {
            try {
                Double number = Double.parseDouble(token);
                
                if (number.isNaN()) {
                    // TODO Maybe skip, or add a sentinel value, or log an error
                } else {
                    parsedNumbers.add(number);
                }
            } catch (NumberFormatException e) {
                System.err.println("[FMIApiParser]: Failed to parse double from token: " + token);
            }
        }
        
        return parsedNumbers;
    }
}
