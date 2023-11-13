package fi.nordicwatt.model.data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import fi.nordicwatt.model.datamodel.AbstractDataModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.MeasurementUnit;

/**
 * @author Github copilot
 */
public class AbstractDataModelTest {

    // Test class contructor which is given treemap of data points
    @Test
    public void testConstructorWithMap() {
        // Create a new data model
        AbstractDataModel<Double> dataModel = new ExampleDataModel(
                DataType.TEMPERATURE,
                MeasurementUnit.CELSIUS,
                LocalDateTime.of(2022, 1, 1, 0, 0),
                new Double[] { 20.0, 23.0, 22.0 });

        // Get the data points
        Map<LocalDateTime, Double> dataPoints = dataModel.getDataPoints();

        // Check that the data points are correct
        assertEquals(3, dataPoints.size());
        assertEquals(20.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 0, 0)));
        assertEquals(23.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 1, 0)));
        assertEquals(22.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 2, 0)));
    }

    @Test
    public void testAddDataPointAndGetPoints() {
        // Create a new data model with a 1 minute interval

        AbstractDataModel<Double> dataModel = new ExampleDataModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS);

        // Add some data points
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 0, 0), 20.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 1, 0), 21.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 2, 0), 22.0);

        // Get the data points
        Map<LocalDateTime, Double> dataPoints = dataModel.getDataPoints();

        // Check that the data points are correct
        assertEquals(3, dataPoints.size());
        assertEquals(20.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 0, 0)));
        assertEquals(21.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 1, 0)));
        assertEquals(22.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 2, 0)));
    }

    @Test
    public void testAddDataPointAndGetPointsWithInterval() {
        // Create a new data model with a 1 minute interval

        AbstractDataModel<Double> dataModel = new ExampleDataModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS);

        // Add some data points
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 0, 0), 20.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 1, 0), 21.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 2, 0), 22.0);

        // Get the data points
        Map<LocalDateTime, Double> dataPoints = dataModel.getDataPoints();

        // Check that the data points are correct
        assertEquals(3, dataPoints.size());
        assertEquals(20.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 0, 0)));
        assertEquals(21.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 1, 0)));
        assertEquals(22.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 2, 0)));
    }

    @Test
    public void testAddDataPointAndGetPointsWithIntervalAndFirstTimestamp() {
        // Create a new data model
        AbstractDataModel<Double> dataModel = new ExampleDataModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS,
                LocalDateTime.of(2022, 1, 1, 0, 0),
                new Double[] { 20.0, 23.0, 22.0 });

        // Get the data points
        Map<LocalDateTime, Double> dataPoints = dataModel.getDataPoints();

        // Check that the data points are correct
        assertEquals(3, dataPoints.size());
        assertEquals(20.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 0, 0)));
        assertEquals(23.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 1, 0)));
        assertEquals(22.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 2, 0)));
    }

    // Test increment timestamp
    @Test
    public void testIncrementTimestamp() {
        assertEquals(LocalDateTime.of(2022, 1, 1, 1, 0),
                AbstractDataModel.incrementTimestamp(LocalDateTime.of(2022, 1, 1, 0, 0), Duration.ofHours(1)));
        assertEquals(LocalDateTime.of(2022, 1, 2, 0, 0),
                AbstractDataModel.incrementTimestamp(LocalDateTime.of(2022, 1, 1, 23, 0), Duration.ofHours(1)));
        assertEquals(LocalDateTime.of(2022, 2, 1, 0, 0),
                AbstractDataModel.incrementTimestamp(LocalDateTime.of(2022, 1, 31, 23, 0), Duration.ofHours(1)));
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0),
                AbstractDataModel.incrementTimestamp(LocalDateTime.of(2022, 12, 31, 23, 0), Duration.ofHours(1)));
    }

    // test checkDataPoints
    @Test
    public void testCheckDataPoints() {
        // Create a new data model
        AbstractDataModel<Double> dataModel = new ExampleDataModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS);

        // Add some data points wtih missing point
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 0, 0), 20.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 2, 0), 22.0);
        assertFalse(dataModel.checkDataPoints());

        // Add the missing point
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 1, 0), 21.0);
        assertTrue(dataModel.checkDataPoints());
    }

    // test trying to add datapoint with invalid timestamp
    @Test
    public void testAddDataPointWithInvalidTimestamp() {
        // Create a new data model
        AbstractDataModel<Double> dataModel = new ExampleDataModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS);

        // Add some data points
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 0, 0), 20.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 1, 0), 21.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 2, 0), 22.0);

        // Try to add a data point with invalid timestamp
        try {
            dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 2, 0), 23.0);
        } catch (IllegalArgumentException e) {
            assertEquals("Timestamp is not valid", e.getMessage());
        }
    }

    // test getDataPointsWithRange
    @Test
    public void testGetDataPointsWithRange() {
        // Create a new data model
        AbstractDataModel<Double> dataModel = new ExampleDataModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS);

        // Add some data points
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 0, 0), 20.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 1, 0), 21.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 2, 0), 22.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 3, 0), 23.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 4, 0), 24.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 5, 0), 25.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 6, 0), 26.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 7, 0), 27.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 8, 0), 28.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 9, 0), 29.0);
        dataModel.addDataPoint(LocalDateTime.of(2022, 1, 1, 10, 0), 30.0);

        // Get the data points
        Map<LocalDateTime, Double> dataPoints = dataModel.getDataPointsWithRange(
                LocalDateTime.of(2022, 1, 1, 2, 0),
                LocalDateTime.of(2022, 1, 1, 8, 0));

        // Check that the data points are correct
        assertEquals(7, dataPoints.size());
        assertEquals(22.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 2, 0)));
        assertEquals(23.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 3, 0)));
        assertEquals(24.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 4, 0)));
        assertEquals(25.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 5, 0)));
        assertEquals(26.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 6, 0)));
        assertEquals(27.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 7, 0)));
        assertEquals(28.0, dataPoints.get(LocalDateTime.of(2022, 1, 1, 8, 0)));

        // Check for values outside the range
        assertFalse(dataPoints.containsKey(LocalDateTime.of(2022, 1, 1, 1, 0)));
        assertFalse(dataPoints.containsKey(LocalDateTime.of(2022, 1, 1, 9, 0)));
    }

    /**
     * Simple class to represent the abstracts class for the tests.
     */
    public static class ExampleDataModel extends AbstractDataModel<Double> {

        public ExampleDataModel(
                DataType dataType,
                MeasurementUnit celsius,
                LocalDateTime firstEntryTimestamp,
                Double[] values) {
            super(dataType, celsius, firstEntryTimestamp, values);
        }

        public ExampleDataModel(DataType dataType, MeasurementUnit unit) {
            super(dataType, unit);
        }
    }

}