package org.example.model.api;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.example.model.data.EnergyModel;
import org.example.model.data.WeatherModel;
import okhttp3.Response;

/**
 * FMIApiParser - Parses response from FMI API to WeatherModel.
 * 
 * @see APIParserInterface
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
    public WeatherModel parseToDataObject(Response response) {
        try {
            String responseBody = response.body().string();
            String responseDataAsString = getStringBetween(responseBody, "<gml:doubleOrNilReasonTupleList>",
                    "</gml:doubleOrNilReasonTupleList>");
            List<Double> responseData = extractDoubles(responseDataAsString);
            Double[] responseDataArray = new Double[responseData.size()];

            String location = getStringBetween(responseBody, "<target:region codeSpace="+"\""+"http://xml.fmi.fi/namespace/location/region"+"\""+">", "</target:region>");
            Duration interval = Duration.ofHours(1);
            CharSequence firstEntryTimestamp = getStringBetween(responseBody, "<gml:beginPosition>", "</gml:beginPosition>");
            DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(firstEntryTimestamp, originalFormat);
            String formattedTimestamp = dateTime.format(desiredFormat);
            String dataType = getStringBetween(response.toString(), "parameters=","&");
            String unit = "";

            switch (dataType) {
                case "temperature":
                    unit = "C";
                    break;
                case "windspeedms":
                    unit = "m/s";
                    break;
                case "pressure":
                    unit = "hPa";
                    break;
                case "humidity":
                    unit = "%";
                    break;
            }

            return new WeatherModel(dataType, unit, formattedTimestamp, interval, location, responseData.toArray(responseDataArray));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new WeatherModel(null, null, null, null, null, null);

    }

    /**
     * getStringBetween - Gets a string between two strings. Exclude the start and
     * end strings from the result.
     * 
     * @param originalStr
     * @param startStr
     * @param endStr
     * @return
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
     * @param input
     * @return
     */
    public static List<Double> extractDoubles(String input) {
        List<Double> resultList = new ArrayList<>();
        String[] lines = input.split("\n");

        for (String line : lines) {
            line = line.trim(); // Remove any leading/trailing whitespace
            try {
                Double value = Double.parseDouble(line);
                resultList.add(value);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing value: " + line);
            }
        }
        return resultList;
    }
}
