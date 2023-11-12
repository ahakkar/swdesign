package fi.nordicwatt.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

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
        model.addDataPoint(LocalDateTime.of(2021, 1, 1, 1, 0), 1.0);
        model.addDataPoint(LocalDateTime.of(2021, 1, 1, 2, 0), 2.0);
        model.addDataPoint(LocalDateTime.of(2021, 1, 1, 3, 0), 3.0);
        assertEquals(3, model.getDataPoints().size());
    }

    // Test getdatapoints with range
    @Test
    public void testGetDataPointsWithRange() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "Helsinki");
        model.addDataPoint(LocalDateTime.of(2021, 1, 1, 0, 0), 1.0);
        model.addDataPoint(LocalDateTime.of(2021, 1, 1, 0, 5), 2.0);
        model.addDataPoint(LocalDateTime.of(2021, 1, 1, 0, 10), 3.0);
        assertEquals(1, model.getDataPointsWithRange(
                LocalDateTime.of(2021, 1, 1, 0, 2), LocalDateTime.of(2021, 1, 1, 0, 8)).size());
    }

    // Test getdatapoints with range when bottom and top equal to some datapoint
    @Test
    public void testGetDataPointsWithRangeWhenBottomAndTopEqual() {
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "Helsinki");
        model.addDataPoint(LocalDateTime.of(2021, 1, 1, 0, 0), 1.0);
        model.addDataPoint(LocalDateTime.of(2021, 1, 1, 0, 5), 2.0);
        model.addDataPoint(LocalDateTime.of(2021, 1, 1, 0, 10), 3.0);
        assertEquals(2,
                model.getDataPointsWithRange(LocalDateTime.of(2021, 1, 1, 0, 5), LocalDateTime.of(2021, 1, 1, 0, 10))
                        .size());
    }

}
