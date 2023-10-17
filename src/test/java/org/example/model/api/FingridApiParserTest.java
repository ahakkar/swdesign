package org.example.model.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.example.model.data.EnergyModel;
import org.example.utils.EnvironmentVariables;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import okhttp3.Response;

import org.example.model.data.ApiDataRequest;
import org.example.model.data.DataRequest;
import org.example.types.DataType;

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

            DataRequest dataRequest = 
                new DataRequest(
                    DataType.CONSUMPTION, 
                    LocalDateTime.of(2021, 10, 4, 0, 0),
                    LocalDateTime.of(2021, 10, 7, 0, 0),      
                    "Helsinki"
                    );
            ApiDataRequest apiDataRequest = new ApiDataRequest(EnergyModel.class, dataRequest);

            EnergyModel model = parser.parseToDataObject(apiDataRequest, response.body().string());
            assertEquals(model.getDataType(), DataType.CONSUMPTION);
            assertEquals(model.getUnit(), "MWh");

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
