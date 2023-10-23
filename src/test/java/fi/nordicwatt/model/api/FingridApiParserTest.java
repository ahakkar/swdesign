package fi.nordicwatt.model.api;

import fi.nordicwatt.model.api.fingrid.FingridAPIRequestBuilder;
import fi.nordicwatt.model.api.fingrid.FingridApiParser;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.datamodel.EnergyModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.MeasurementUnit;
import fi.nordicwatt.utils.EnvironmentVariables;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
                .withStartTime("2021-10-03T21:00:00Z")
                .withEndTime("2021-10-06T21:00:00Z");

        try {
            Response response = builder.execute();
            FingridApiParser parser = new FingridApiParser();

            LocalDateTime start = LocalDateTime.of(2021, 10, 4, 0, 0);
            LocalDateTime end = LocalDateTime.of(2021, 10, 7, 0, 0);

            DataRequest dataRequest = 
                new DataRequest(
                    DataType.CONSUMPTION, 
                    start,
                    end,
                    "Helsinki"
                    );  
            String responseBody = response.body().string();
            EnergyModel model = parser.parseToDataObject(dataRequest, responseBody);
            assertEquals(model.getDataType(), DataType.CONSUMPTION);
            assertEquals(model.getUnit(), MeasurementUnit.MEGA_WATT_HOUR);

            // Get all datapoints in a map
            Map<String, Double> dataPoints = model.getDataPoints();
            assertEquals(73, dataPoints.size());

            // Check first four data points and first -1
            assertEquals(null, dataPoints.get("2021-10-03 23:00:00"));
            assertEquals(7983, dataPoints.get("2021-10-04 00:00:00"), 0.01);
            assertEquals(7470, dataPoints.get("2021-10-04 03:00:00"), 0.01);
            assertEquals(7595, dataPoints.get("2021-10-04 04:00:00"), 0.01);
            assertEquals(7957, dataPoints.get("2021-10-04 05:00:00"), 0.01);

            // Check datapoint from middle
            assertEquals(9333, dataPoints.get("2021-10-05 15:00:00"), 0.01);

            // Check the last and last +1
            assertEquals(8071, dataPoints.get("2021-10-07 00:00:00"), 0.01);
            assertEquals(null, dataPoints.get("2021-10-07 01:00:00"));

        } catch (Exception e) {
            e.printStackTrace();
            fail("API request or parsing failed");
        }

    }
}
