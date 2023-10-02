package org.example.model.data;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Github copilot
 */
public class AbstractDataModelTest {

    // Test class contructor which is given treemap of data points
    @Test
    public void testConstructorWithMap() {
        // Create a new data model with a 1 minute interval
        AbstractDataModel<Double> dataModel = new ExampleDataModel("Temperature", "Celsius", "2022-01-01 00:00:00",
                Duration.ofMinutes(1), new Double[] { 20.0, 23.0, 22.0 });

        // Get the data points
        Map<String, Double> dataPoints = dataModel.getDataPoints();

        // Check that the data points are correct
        assertEquals(3, dataPoints.size());
        assertEquals(20.0, dataPoints.get("2022-01-01 00:00:00"));
        assertEquals(23.0, dataPoints.get("2022-01-01 00:01:00"));
        assertEquals(22.0, dataPoints.get("2022-01-01 00:02:00"));
    }

    @Test
    public void testAddDataPointAndGetPoints() {
        // Create a new data model with a 1 minute interval
        AbstractDataModel<Double> dataModel = new ExampleDataModel("Temperature", "Celsius", Duration.ofMinutes(1));

        // Add some data points
        dataModel.addDataPoint("2022-01-01 00:00:00", 20.0);
        dataModel.addDataPoint("2022-01-01 00:01:00", 21.0);
        dataModel.addDataPoint("2022-01-01 00:02:00", 22.0);

        // Get the data points
        Map<String, Double> dataPoints = dataModel.getDataPoints();

        // Check that the data points are correct
        assertEquals(3, dataPoints.size());
        assertEquals(20.0, dataPoints.get("2022-01-01 00:00:00"));
        assertEquals(21.0, dataPoints.get("2022-01-01 00:01:00"));
        assertEquals(22.0, dataPoints.get("2022-01-01 00:02:00"));
    }

    @Test
    public void testAddDataPointAndGetPointsWithInterval() {
        // Create a new data model with a 1 minute interval
        AbstractDataModel<Double> dataModel = new ExampleDataModel("Temperature", "Celsius", Duration.ofMinutes(1));

        // Add some data points
        dataModel.addDataPoint("2022-01-01 00:00:00", 20.0);
        dataModel.addDataPoint("2022-01-01 00:01:00", 21.0);
        dataModel.addDataPoint("2022-01-01 00:02:00", 22.0);

        // Get the data points
        Map<String, Double> dataPoints = dataModel.getDataPoints();

        // Check that the data points are correct
        assertEquals(3, dataPoints.size());
        assertEquals(20.0, dataPoints.get("2022-01-01 00:00:00"));
        assertEquals(21.0, dataPoints.get("2022-01-01 00:01:00"));
        assertEquals(22.0, dataPoints.get("2022-01-01 00:02:00"));
    }

    @Test
    public void testAddDataPointAndGetPointsWithIntervalAndFirstTimestamp() {
        // Create a new data model with a 1 minute interval
        AbstractDataModel<Double> dataModel = new ExampleDataModel("Temperature", "Celsius", "2022-01-01 00:00:00",
                Duration.ofMinutes(1), new Double[] { 20.0, 23.0, 22.0 });

        // Get the data points
        Map<String, Double> dataPoints = dataModel.getDataPoints();

        // Check that the data points are correct
        assertEquals(3, dataPoints.size());
        assertEquals(20.0, dataPoints.get("2022-01-01 00:00:00"));
        assertEquals(23.0, dataPoints.get("2022-01-01 00:01:00"));
        assertEquals(22.0, dataPoints.get("2022-01-01 00:02:00"));
    }

    @Test
    public void testIsValidTimeStamp() {
        // Valid timestamps
        assertTrue(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:00"));
        assertTrue(AbstractDataModel.isValidTimeStamp("2022-01-01 12:00:00"));
        assertTrue(AbstractDataModel.isValidTimeStamp("2022-01-01 23:59:59"));

        // Invalid timestamps
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:0"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:60"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 24:00:00"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 23:60:00"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 23:59:60"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 23:59:59:59"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 23:59:59:59:59"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 23:59:59:59:59:59"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 23:59:59:59:59:59:59"));

        // Invalid timestamps with letters
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:00a"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:00a0"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:00a00"));

        // Invalid timestamps with special characters
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:00!"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:00!0"));

        // Invalid timestamps with spaces
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:00 "));
        assertFalse(AbstractDataModel.isValidTimeStamp(" 2022-01-01 00:00:00"));
        assertFalse(AbstractDataModel.isValidTimeStamp(" 2022-01-01 00:00:00 "));

        // Invalid timestamps with wrong format
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:00:00"));
        assertFalse(AbstractDataModel.isValidTimeStamp("2022-01-01 00:00:00:00:00"));
    }

    // Test increment timestamp
    @Test
    public void testIncrementTimestamp() {
        assertEquals("2022-01-01 00:01:00",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofMinutes(1)));
        assertEquals("2022-01-01 00:02:00",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofMinutes(2)));
        assertEquals("2022-01-01 00:00:01",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofSeconds(1)));
        assertEquals("2022-01-01 00:00:02",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofSeconds(2)));
        assertEquals("2022-01-01 01:00:00",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofHours(1)));
        assertEquals("2022-01-02 00:00:00",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofDays(1)));
        assertEquals("2022-01-03 00:00:00",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofDays(2)));
        assertEquals("2022-02-01 00:00:00",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofDays(31)));
        assertEquals("2023-01-01 00:00:00",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofDays(365)));
        assertEquals("2022-01-01 00:00:00",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofDays(0)));
        assertEquals("2021-12-31 00:00:00",
                AbstractDataModel.incrementTimestamp("2022-01-01 00:00:00", Duration.ofDays(-1)));
    }

    // test checkDataPoints
    @Test
    public void testCheckDataPoints() {
        // Create a new data model with a 1 minute interval
        AbstractDataModel<Double> dataModel = new ExampleDataModel("Temperature", "Celsius", Duration.ofMinutes(1));

        // Add some data points wtih missing point
        dataModel.addDataPoint("2022-01-01 00:00:00", 20.0);
        dataModel.addDataPoint("2022-01-01 00:02:00", 22.0);
        assertFalse(dataModel.checkDataPoints());

        // Add the missing point
        dataModel.addDataPoint("2022-01-01 00:01:00", 21.0);
        assertTrue(dataModel.checkDataPoints());
    }

    // test trying to add datapoint with invalid timestamp
    @Test
    public void testAddDataPointWithInvalidTimestamp() {
        // Create a new data model with a 1 minute interval
        AbstractDataModel<Double> dataModel = new ExampleDataModel("Temperature", "Celsius", Duration.ofMinutes(1));

        // Add some data points
        dataModel.addDataPoint("2022-01-01 00:00:00", 20.0);
        dataModel.addDataPoint("2022-01-01 00:01:00", 21.0);
        dataModel.addDataPoint("2022-01-01 00:02:00", 22.0);

        // Try to add a data point with invalid timestamp
        try {
            dataModel.addDataPoint("2022-01-01 00:02:0", 23.0);
        } catch (IllegalArgumentException e) {
            assertEquals("Timestamp is not valid", e.getMessage());
        }
    }

    // test getDataPointsWithRange
    @Test
    public void testGetDataPointsWithRange() {
        // Create a new data model with a 1 minute interval
        AbstractDataModel<Double> dataModel = new ExampleDataModel("Temperature", "Celsius", Duration.ofMinutes(1));

        // Add some data points
        dataModel.addDataPoint("2022-01-01 00:00:00", 20.0);
        dataModel.addDataPoint("2022-01-01 00:01:00", 21.0);
        dataModel.addDataPoint("2022-01-01 00:02:00", 22.0);
        dataModel.addDataPoint("2022-01-01 00:03:00", 23.0);
        dataModel.addDataPoint("2022-01-01 00:04:00", 24.0);
        dataModel.addDataPoint("2022-01-01 00:05:00", 25.0);
        dataModel.addDataPoint("2022-01-01 00:06:00", 26.0);
        dataModel.addDataPoint("2022-01-01 00:07:00", 27.0);
        dataModel.addDataPoint("2022-01-01 00:08:00", 28.0);
        dataModel.addDataPoint("2022-01-01 00:09:00", 29.0);
        dataModel.addDataPoint("2022-01-01 00:10:00", 30.0);

        // Get the data points
        Map<String, Double> dataPoints = dataModel.getDataPointsWithRange("2022-01-01 00:02:00", "2022-01-01 00:08:00");

        // Check that the data points are correct
        assertEquals(7, dataPoints.size());
        assertEquals(22.0, dataPoints.get("2022-01-01 00:02:00"));
        assertEquals(23.0, dataPoints.get("2022-01-01 00:03:00"));
        assertEquals(24.0, dataPoints.get("2022-01-01 00:04:00"));
        assertEquals(25.0, dataPoints.get("2022-01-01 00:05:00"));
        assertEquals(26.0, dataPoints.get("2022-01-01 00:06:00"));
        assertEquals(27.0, dataPoints.get("2022-01-01 00:07:00"));
        assertEquals(28.0, dataPoints.get("2022-01-01 00:08:00"));

        // Check for values outside the range
        assertFalse(dataPoints.containsKey("2022-01-01 00:01:00"));
        assertFalse(dataPoints.containsKey("2022-01-01 00:09:00"));
    }

    /**
     * Simple class to represent the abstracts class for the tests.
     */
    public static class ExampleDataModel extends AbstractDataModel<Double> {

        static {
            for (DataType type : DataType.values()) {
                supportedDataTypes.add(type.name());
            }
        }

        public enum DataType {
            TEMPERATURE,
            HUMIDITY,
            PRESSURE
        }

        public ExampleDataModel(String dataType, String unit, String firstEntryTimestamp, Duration interval,
                Double[] values) {
            super(AbstractDataModel.parseDataType(supportedDataTypes, dataType), unit, firstEntryTimestamp, interval,
                    values);
        }

        public ExampleDataModel(String dataType, String unit, Duration interval) {
            super(dataType, unit, interval);
        }
    }

}