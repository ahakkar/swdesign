package org.example.model.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.model.data.EnergyModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.Response;

/**
 * FingridApiParser - Parses response from Fingrid API to EnergyModel.
 * 
 * @see APIParserInterface
 */
public class FingridApiParser implements APIParserInterface<EnergyModel> {

    // key value pairs where key is variable if and value is datatype from
    // EnergyModel
    // TODO: Optimal solutions would be to use the datatypes from configfile that is
    // not yet fully implemented
    private static final Map<String, String> dataTypeMap = new HashMap<>();
    static {
        dataTypeMap.put("124", "TOTAL_CONSUMPTION");
        dataTypeMap.put("74", "TOTAL_PRODUCTION");

        // Todo: Implement hydro production
        // dataTypeMap.put("0", "HYDRO_PRODUCTION"); // Note: there is data source of
        // this with only 3 minute interval and thererefore we need to make calculations
        // for it to get the hourly data

        // Todo: Implement nuclear production
        // dataTypeMap.put("0", "NUCLEAR_PRODUCTION"); // Note: there is data source of
        // this with only 3 minute interval and thererefore we need to make calculations
        // for it to get the hourly data

        // Todo: Add variable id for wind production
        // dataTypeMap.put("0", "WIND_PRODUCTION"); // Note: this does have hourly data
        // from the source
    }

    /**
     * parseToDataObject - Parses response to data object.
     * 
     * @param response - Response from API
     * @return Data object
     */
    @Override
    public EnergyModel parseToDataObject(Response response) {
        String dataType = getDataType(response);

        try {
            String jsonData = response.body().string();
            ObjectMapper mapper = new ObjectMapper();

            // Deserialize JSON into List of Maps
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Map.class);
            List<Map<String, Object>> entries = mapper.readValue(jsonData, listType);

            if (entries.isEmpty()) {
                throw new RuntimeException("No data entries found in the API response");
            }

            // Extract values from the entries
            Double[] values = entries.stream()
                    .map(entry -> {
                        Object value = entry.get("value");
                        if (value instanceof Integer) {
                            return ((Integer) value).doubleValue();
                        }
                        return (Double) value;
                    })
                    .toArray(Double[]::new);

            // Extract first entry timestamp and format it
            String firstEntryTimestamp = (String) entries.get(0).get("start_time");
            DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
            DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(firstEntryTimestamp, originalFormat);
            String formattedTimestamp = dateTime.format(desiredFormat);

            // Interval is duration of 1 hour
            Duration interval = Duration.ofHours(1);
            return new EnergyModel(dataType, "MWh", formattedTimestamp, interval,
                    values);

        } catch (Exception e) {
            throw new RuntimeException("Error parsing response to EnergyModel", e);
        }
    }

    /**
     * getDataType - Gets the datatype from the response.
     * 
     * @param response - Response from API
     * @return Data type as string
     */
    private static String getDataType(Response response) {
        // Parse url from response
        String url = response.request().url().toString();
        // Here is example url:
        // https://api.fingrid.fi/v1/variable/241/events/json?start_time=2023-09-01T00:00:00Z&end_time=2023-09-02T00:00:00Z
        // variable id is the element right after variable
        String[] urlParts = url.split("/");
        for (int i = 0; i < urlParts.length; i++) {
            if ("variable".equals(urlParts[i]) && i + 1 < urlParts.length) {
                String variableId = urlParts[i + 1];
                String dataType = dataTypeMap.get(variableId);

                if (dataType != null) {
                    return dataType;
                } else {
                    throw new RuntimeException("Unknown variableId: " + variableId);
                }
            }
        }

        // If the loop completes and we haven't returned, there was no "variable"
        // segment
        throw new RuntimeException("No 'variable' segment found in URL: " + url);
    }

}
