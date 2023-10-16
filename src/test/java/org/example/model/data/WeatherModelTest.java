package org.example.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * @author github copliot
 */
public class WeatherModelTest {
    // Test that the constructor works
    @Test
    public void testConstructor() {
        WeatherModel model = new WeatherModel("Temperature", "Celsius", "Helsinki");
        assertEquals("Temperature", model.getDataType());
        assertEquals("Celsius", model.getUnit());
    }

    // Test exception is thrown if data type is not supported
    @Test
    public void testConstructorExceptionDataType() {
        try {
            new WeatherModel("Temperature_that_is_non_valid_datatype", "Celsius", "Helsinki");
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid data type: Temperature_that_is_non_valid_datatype", e.getMessage());
        }
    }

    // Test location getter
    @Test
    public void testGetLocation() {
        WeatherModel model = new WeatherModel("Temperature", "Celsius",  "Helsinki");
        assertEquals("Helsinki", model.getLocation());
    }

    // Test all different data types are supported
    @Test
    public void testParseDataType() {
        for (WeatherModel.DataType type : WeatherModel.DataType.values()) {
            assertEquals(type, WeatherModel.DataType.parseDataType(type.name()));
        }
    }

    // Test getdatapoints
    @Test
    public void testGetDataPoints() {
        WeatherModel model = new WeatherModel("Temperature", "Celsius", "Helsinki");
        assertEquals(0, model.getDataPoints().size());
    }

    // Test getdatapooints with some actual values
    @Test
    public void testGetDataPointsWithData() {
        WeatherModel model = new WeatherModel("Temperature", "Celsius", "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(3, model.getDataPoints().size());
    }

    // Test getdatapoints with range
    @Test
    public void testGetDataPointsWithRange() {
        WeatherModel model = new WeatherModel("Temperature", "Celsius", "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(1, model.getDataPointsWithRange("2021-01-01 00:02:00", "2021-01-01 00:08:00").size());
    }

    // Test getdatapoints with range when bottom and top equal to some datapoint
    @Test
    public void testGetDataPointsWithRangeWhenBottomAndTopEqual() {
        WeatherModel model = new WeatherModel("Temperature", "Celsius", "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(2, model.getDataPointsWithRange("2021-01-01 00:05:00", "2021-01-01 00:10:00").size());
    }

}
