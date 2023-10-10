package org.example.model.api;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.example.model.data.WeatherModel;
import org.example.utils.EnvironmentVariables;
import org.junit.jupiter.api.Test;

import okhttp3.Response;

/**
 * Test class for FmiAPIRequestBuilder.
 * 
 * Editors note: Result Data from FMI is so awful that it's hard to test the
 * response without parsing it first.
 * Therefore this test can also test (partialy) the FMIApiParser class.
 * 
 * @author Heikki Hohtari, with help from ChatGTP
 */
public class FmiAPIRequestBuilderTest {

    @Test
    public void testFmiApiRequestBuilder() throws Exception {
        EnvironmentVariables.load(".env");

        String place = "tampere";

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
                .withEndTime(formattedEndTime);

        Response response = builder.execute();
        FMIApiParser parser = new FMIApiParser();
        WeatherModel responseBody = parser.parseToDataObject(response);
        // TODO @markus: Probably do couple tests on the data once you get it as a
        // WeatherModel object

        // Assert that response is not empty
        assertNotNull(responseBody);

        // TODO: This test is awful, but it's a start
        // It should actually test the response body for the correct data
        // But extracting the data from the XML response is a bit tricky
    }
}
