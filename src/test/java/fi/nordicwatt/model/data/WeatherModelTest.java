package fi.nordicwatt.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import fi.nordicwatt.model.datamodel.WeatherModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.MeasurementUnit;

/**
 * @author github copliot
 */
public class WeatherModelTest {
    // Test that the constructor works
    @Test
    public void testConstructor() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "Helsinki");
        assertEquals(DataType.TEMPERATURE, model.getDataType());
        assertEquals(MeasurementUnit.CELSIUS, model.getUnit());
    }
    @Test
    public void testGetLocation() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "Helsinki");
        assertEquals("Helsinki", model.getLocation());
    }

    // Test getdatapoints
    @Test
    public void testGetDataPoints() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "Helsinki");
        assertEquals(0, model.getDataPoints().size());
    }

    // Test getdatapooints with some actual values
    @Test
    public void testGetDataPointsWithData() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "Helsinki");
        model.addDataPoint("2021-01-01 01:00:00", 1.0);
        model.addDataPoint("2021-01-01 02:00:00", 2.0);
        model.addDataPoint("2021-01-01 03:00:00", 3.0);
        assertEquals(3, model.getDataPoints().size());
    }

    // Test getdatapoints with range
    @Test
    public void testGetDataPointsWithRange() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(1, model.getDataPointsWithRange("2021-01-01 00:02:00", "2021-01-01 00:08:00").size());
    }

    // Test getdatapoints with range when bottom and top equal to some datapoint
    @Test
    public void testGetDataPointsWithRangeWhenBottomAndTopEqual() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "Helsinki");
        model.addDataPoint("2021-01-01 00:00:00", 1.0);
        model.addDataPoint("2021-01-01 00:05:00", 2.0);
        model.addDataPoint("2021-01-01 00:10:00", 3.0);
        assertEquals(2, model.getDataPointsWithRange("2021-01-01 00:05:00", "2021-01-01 00:10:00").size());
    }

}
