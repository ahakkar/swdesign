package fi.nordicwatt.model.api.fmi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import fi.nordicwatt.model.api.APIParserInterface;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.datamodel.WeatherModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.DateTimeConverter;
import fi.nordicwatt.utils.Logger;

/**
 * FMIApiParser - Parses response from FMI API to WeatherModel.
 * 
 * @see APIParserInterface
 * @author Markus Hissa ? 
 */
public class FmiApiParser implements APIParserInterface<WeatherModel> {

    /**
     * parseToDataObject - Parses response to data object.
     * 
     * @param response - Response from API
     * @return Data object
     *
     */
    @Override
    public WeatherModel parseToDataObject(DataRequest request, String response) throws ParseException {

        String responseDataAsString = getStringBetween(response, "<gml:doubleOrNilReasonTupleList>",
                "</gml:doubleOrNilReasonTupleList>");
        Logger.log("Response data: " + responseDataAsString, "FmiApiParser.log");
        if (responseDataAsString.isEmpty()) {
            Logger.log("Data requested from FMI API was empty and contained no values. This means that the data is not available for the requested time period. Returning empty datamodel.", "FmiApiParser.log");
            return new WeatherModel(request.getDataType(), request.getDataType().getUnit(), request.getStarttime(), request.getLocation(), new Double[0]);
        }
        List<Double> responseData = extractDoubles(responseDataAsString);
        Double[] responseDataArray = new Double[responseData.size()];

        String location = getStringBetween(response, "<target:region codeSpace="+"\""+"http://xml.fmi.fi/namespace/location/region"+"\""+">", "</target:region>");
        CharSequence firstEntryTimestamp = getStringBetween(response, "<gml:beginPosition>", "</gml:beginPosition>");
        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = DateTimeConverter.gmtTimeToFinnishTime(LocalDateTime.parse(firstEntryTimestamp, originalFormat));
        //String formattedTimestamp = dateTime.format(desiredFormat);
        DataType dataType = request.getDataType();

        Double[] responseArray = responseData.toArray(responseDataArray);

        WeatherModel model = 
            new WeatherModel(
                dataType,
                dataType.getUnit(),
                dateTime,
                location,
                responseArray
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
                    parsedNumbers.add(null);
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
