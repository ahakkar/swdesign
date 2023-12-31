package fi.nordicwatt.model.api;

import fi.nordicwatt.model.api.fingrid.FingridAPIRequestBuilder;
import fi.nordicwatt.model.api.fingrid.FingridApiParser;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.datamodel.EnergyModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.MeasurementUnit;
import fi.nordicwatt.utils.ApiSettings;
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
        ApiSettings.load();
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
            Map<LocalDateTime, Double> dataPoints = model.getDataPoints();
            assertEquals(73, dataPoints.size());

            // Check first four data points and first -1
            assertEquals(null, dataPoints.get(LocalDateTime.of(2021, 10, 3, 23, 0)));
            assertEquals(7983, dataPoints.get(LocalDateTime.of(2021, 10, 4, 0, 0)), 0.01);
            assertEquals(7470, dataPoints.get(LocalDateTime.of(2021, 10, 4, 3, 0)), 0.01);
            assertEquals(7595, dataPoints.get(LocalDateTime.of(2021, 10, 4, 4, 0)), 0.01);
            assertEquals(7957, dataPoints.get(LocalDateTime.of(2021, 10, 4, 5, 0)), 0.01);

            // Check datapoint from middle
            assertEquals(9333, dataPoints.get(LocalDateTime.of(2021, 10, 5, 15, 0)), 0.01);

            // Check the last and last +1
            assertEquals(8071, dataPoints.get(LocalDateTime.of(2021, 10, 7, 0, 0)), 0.01);
            assertEquals(null, dataPoints.get(LocalDateTime.of(2021, 10, 7, 1, 0)));

        } catch (Exception e) {
            e.printStackTrace();
            fail("API request or parsing failed");
        }

    }
}
