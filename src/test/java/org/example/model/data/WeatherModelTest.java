package org.example.model.data;

import java.time.Duration;

import org.example.types.DataType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author github copliot
 */
public class WeatherModelTest {
    // Test that the constructor works
    @Test
    public void testConstructor() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "Celsius", Duration.ofMinutes(5), "Helsinki");
        assertEquals(DataType.TEMPERATURE, model.getDataType());
        assertEquals("Celsius", model.getUnit());
        assertEquals(Duration.ofMinutes(5), model.getInterval());
    }


    // Test location getter
    @Test
    public void testGetLocation() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "Celsius", Duration.ofMinutes(5), "Helsinki");
        assertEquals("Helsinki", model.getLocation());
    }

    // Test getdatapoints
    @Test
    public void testGetDataPoints() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "Celsius", Duration.ofMinutes(5), "Helsinki");
        assertEquals(0, model.getDataPoints().size());
    }

    // Test getdatapooints with some actual values
    @Test
    public void testGetDataPointsWithData() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "Celsius", Duration.ofMinutes(5), "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(3, model.getDataPoints().size());
    }

    // Test getdatapoints with range
    @Test
    public void testGetDataPointsWithRange() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "Celsius", Duration.ofMinutes(5), "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(1, model.getDataPointsWithRange("2021-01-01 00:02:00", "2021-01-01 00:08:00").size());
    }

    // Test getdatapoints with range when bottom and top equal to some datapoint
    @Test
    public void testGetDataPointsWithRangeWhenBottomAndTopEqual() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, "Celsius", Duration.ofMinutes(5), "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(2, model.getDataPointsWithRange("2021-01-01 00:05:00", "2021-01-01 00:10:00").size());
    }

}
