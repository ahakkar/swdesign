package fi.nordicwatt.model.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import fi.nordicwatt.model.api.FMIApiParser;
import fi.nordicwatt.model.api.FmiAPIRequestBuilder;
import fi.nordicwatt.model.data.ApiDataRequest;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.data.WeatherModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.MeasurementUnit;

import okhttp3.Response;

/**
 * Test class for FmiAPIRequestBuilder.
 * 
 * Editors note: Result Data from FMI is so awful that it's hard to test the
 * response without parsing it first.
 * Therefore this test can also test (partialy) the FMIApiParser class.
 * 
 * @author Heikki Hohtari, with help from ChatGTP
 * 
 * TODO fix broken tests due to missing DataType enum compatibility
 */
public class FmiAPIRequestBuilderTest {

    @Test
    public void testFmiApiRequestBuilder() throws Exception {

        String place = "tampere";

        MeasurementUnit unit = MeasurementUnit.CELSIUS;

        // Set fixed date as 25.09.2023 and set the end date as 29.09.2023
        LocalDate startTimeDate = LocalDate.of(2023, 9, 25);
        LocalDate endTimeDate = LocalDate.of(2023, 9, 29);

        // Format dates for the API
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00Z'");
        String formattedStartTime = startTimeDate.format(formatter);
        String formattedEndTime = endTimeDate.format(formatter);

        // Build the request
        FmiAPIRequestBuilder builder = new FmiAPIRequestBuilder()
                .withPlace(place)
                .withStartTime(formattedStartTime)
                .withEndTime(formattedEndTime)
                .withDataType(DataType.TEMPERATURE.getVariableId());

        Response response = builder.execute();
        FMIApiParser parser = new FMIApiParser();

        DataRequest dataRequest = 
            new DataRequest(
                DataType.TEMPERATURE, 
                LocalDateTime.of(2021, 10, 4, 0, 0),
                LocalDateTime.of(2021, 10, 7, 0, 0),      
                "tampere"
                );
        ApiDataRequest apiDataRequest = new ApiDataRequest(dataRequest);

        WeatherModel responseBody = parser.parseToDataObject(apiDataRequest, response.body().string());
        assertNotNull(responseBody);

        String realLocation = responseBody.getLocation().toLowerCase();
        assertEquals(place,realLocation);

/*         String realDataType = responseBody.getDataType();
        assertEquals(parameters, DataType.CONSUMPTION); */
        
        MeasurementUnit realUnit = responseBody.getUnit();
        assertEquals(unit, realUnit);

        // Assert that response is not empty
        assertNotNull(responseBody);
    }
}
