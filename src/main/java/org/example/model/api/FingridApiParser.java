package org.example.model.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.example.model.data.ApiDataRequest;
import org.example.model.data.EnergyModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
/**
 * FingridApiParser - Parses response from Fingrid API to EnergyModel.
 * 
 * @see APIParserInterface
 * @author ???
 */
public class FingridApiParser implements APIParserInterface<EnergyModel> {
    /**
     * parseToDataObject - Parses response to data object.
     * 
     * @param request  - ApiDataRequest originally sent to api
     * @param response - Response as a String from API
     * @return Data object
     */
    @Override    
    public EnergyModel parseToDataObject(ApiDataRequest request, String response) throws ParseException {
        try {
            ObjectMapper mapper = new ObjectMapper();
    
            // Deserialize JSON into List of Maps
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Map.class);
            List<Map<String, Object>> entries = mapper.readValue(response, listType);
    
            if (entries.isEmpty()) {
                throw new NoDataException("[FingridAPIParser]: No data entries found in the API response");
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
    
            return new EnergyModel(request.getDataRequest().getDataType(), "MWh", formattedTimestamp, values);

        } catch (IOException e) {  // ObjectMapper's readVlue can throw IOException
            throw new ParseException("[FingridAPIParser]: Error reading and processing the JSON response", e);
        } catch (DateTimeParseException e) { // LocalDateTime's parse can throw DateTimeParseException
            throw new ParseException("[FingridAPIParser]: Error parsing date time from the API response", e);
        }
    }  
}
