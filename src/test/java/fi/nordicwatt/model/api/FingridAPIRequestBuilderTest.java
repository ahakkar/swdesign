package fi.nordicwatt.model.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import fi.nordicwatt.utils.EnvironmentVariables;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Response;

/**
 * Test class for FingridAPIRequestBuilder.
 * 
 * @author Heikki Hohtari, with help from ChatGTP
 */
public class FingridAPIRequestBuilderTest {
    @Test
    public void testFingridApiRequestBuilder() throws Exception {
        EnvironmentVariables.load(".env");
        String dataType = "241";

        // Set fixed date as 7.10.2023 and get the previous 3 full calendar days
        LocalDate fixedDate = LocalDate.of(2023, 10, 7);
        LocalDate endTime = fixedDate.minusDays(1); // 6.10.2023
        LocalDate startTime = endTime.minusDays(2); // 4.10.2023

        // Format dates for the API
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00Z'");
        String formattedStartTime = startTime.format(formatter);
        String formattedEndTime = endTime.format(formatter);

        // Build the request
        FingridAPIRequestBuilder builder = new FingridAPIRequestBuilder()
                .withDataType(dataType)
                .withStartTime(formattedStartTime)
                .withEndTime(formattedEndTime);

        // Make the API call
        Response response = builder.execute();
        String responseBody = response.body().string();

        // Parse the responseBody using Jackson's ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataArray = objectMapper.readTree(responseBody);

        // Run the checks that the response had correct data

        // 1. Check specific data points: first three, one from the middle, and the last
        // one
        assertEquals(6790.69, dataArray.get(0).get("value").asDouble(), 0.01);
        assertEquals(6610.58, dataArray.get(1).get("value").asDouble(), 0.01);
        assertEquals(6904.57, dataArray.get(2).get("value").asDouble(), 0.01);
        assertEquals(8433.08, dataArray.get(dataArray.size() / 2).get("value").asDouble(), 0.01); // Middle value
        assertEquals(8975.04, dataArray.get(dataArray.size() - 1).get("value").asDouble(), 0.01); // Last value

        // 2. Check that the total count of entries matches
        assertEquals(49, dataArray.size());

        // 3. Count all the values together and compare them to their mathematical sum
        double calculatedSum = 0.0;
        for (JsonNode dataEntry : dataArray) {
            calculatedSum += dataEntry.get("value").asDouble();
        }
        double expectedSum = 409641.43;
        assertEquals(expectedSum, calculatedSum, 0.01);
    }

}
