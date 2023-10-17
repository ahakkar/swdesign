package org.example.model.data;

import org.example.types.DataType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * @author github copliot
 */
public class WeatherModelTest {
    // Test that the constructor works
    @Test
    public void testConstructor() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "C", "Helsinki");
        assertEquals(DataType.TEMPERATURE, model.getDataType());
        assertEquals("C", model.getUnit());
    }
    @Test
    public void testGetLocation() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "C", "Helsinki");
        assertEquals("Helsinki", model.getLocation());
    }

    // Test getdatapoints
    @Test
    public void testGetDataPoints() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "C", "Helsinki");
        assertEquals(0, model.getDataPoints().size());
    }

    // Test getdatapooints with some actual values
    @Test
    public void testGetDataPointsWithData() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "C", "Helsinki");
        model.addDataPoint("2021-01-01 01:00:00", 1.0);
        model.addDataPoint("2021-01-01 02:00:00", 2.0);
        model.addDataPoint("2021-01-01 03:00:00", 3.0);
        assertEquals(3, model.getDataPoints().size());
    }

    // Test getdatapoints with range
    @Test
    public void testGetDataPointsWithRange() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "C", "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(1, model.getDataPointsWithRange("2021-01-01 00:02:00", "2021-01-01 00:08:00").size());
    }

    // Test getdatapoints with range when bottom and top equal to some datapoint
    @Test
    public void testGetDataPointsWithRangeWhenBottomAndTopEqual() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "Celsius", "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(2, model.getDataPointsWithRange("2021-01-01 00:05:00", "2021-01-01 00:10:00").size());
    }

}
