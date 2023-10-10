package org.example.model.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import org.example.utils.EnvironmentVariables;
import org.junit.jupiter.api.Test;

import okhttp3.Response;

import org.example.model.data.EnergyModel;

/**
 * Test class for FingridAPIRequestBuilder.
 * 
 * This class is used for testing the FingridAPIRequestBuilder class.
 * It tests that the API request is built correctly and that the
 * response is parsed correctly.
 * 
 * @see FingridAPIRequestBuilder
 * @see FingridApiParser
 */
public class FingridApiParserTest {

    @Test
    public void testFingridParser124() throws IOException {
        EnvironmentVariables.load(".env");
        // Make Fingrid API request
        FingridAPIRequestBuilder builder = new FingridAPIRequestBuilder()
                .withDataType("124")
                .withStartTime("2021-10-04T00:00:00Z")
                .withEndTime("2021-10-07T00:00:00Z");

        try {
            Response response = builder.execute();
            FingridApiParser parser = new FingridApiParser();
            EnergyModel model = parser.parseToDataObject(response);
            assertEquals(model.getDataType(), "TOTAL_CONSUMPTION");
            assertEquals(model.getUnit(), "MWh");
            assertEquals(model.getInterval(), Duration.ofHours(1));

            // Get all datapoints in a map
            Map<String, Double> dataPoints = model.getDataPoints();
            assertEquals(73, dataPoints.size());

            // Check first three datavalues
            assertEquals(7470, dataPoints.get("2021-10-04 00:00:00"), 0.01);
            assertEquals(7595, dataPoints.get("2021-10-04 01:00:00"), 0.01);
            assertEquals(7957, dataPoints.get("2021-10-04 02:00:00"), 0.01);

            // Check datapoint from middle
            assertEquals(9333, dataPoints.get("2021-10-05 12:00:00"), 0.01);

            // Check the las
            assertEquals(7552, dataPoints.get("2021-10-07 00:00:00"), 0.01);

        } catch (Exception e) {
            e.printStackTrace();
            fail("API request or parsing failed");
        }

    }
}
