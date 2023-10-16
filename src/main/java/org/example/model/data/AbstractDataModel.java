package org.example.model.data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * AbstractDataModel - Abstract class for handling and storing data.
 * 
 * @param <T> - Type of data to be stored.
 * 
 *            This class is used as a base for all data models.
 *            It contains the data type and unit of the data, as well as a map
 *            of data points.
 *            The data points are stored in a LinkedHashMap, which preserves the
 *            order of insertion.
 *            The data points are stored as key-value pairs, where the key is a
 *            timestamp and the value is the data point.
 *            Time is stored as a string in format "yyyy-MM-dd HH:mm:ss".
 * @author Heikki Hohtari with the help of github copilot
 */
public abstract class AbstractDataModel<T extends Number> {

    // Interface for data types
    protected static Set<String> supportedDataTypes = new HashSet<>();

    // Data type and unit for example "Temperature", "EnergyConsumption"
    private String dataType;

    // Unit for example "Celsius", "kWh"
    private String unit;

    // Map of data points
    // Key is timestamp in format "yyyy-MM-dd HH:mm:ss"
    // Value data can whatever numerical value
    private Map<String, T> dataPoints;

    /**
     * Parses and validates the data type.
     * 
     * @param dataType - Data type to be parsed.
     * @return dataType if it's valid.
     * @throws IllegalArgumentException if the data type is invalid.
     */
    protected static String parseDataType(Set<String> supportedDataTypes, String dataType) {
        if (supportedDataTypes.contains(dataType.toUpperCase())) {
            return dataType;
        }
        throw new IllegalArgumentException("Invalid data type: " + dataType);
    }

    /**
     * Constructor for AbstractDataModel
     * 
     * @param dataType - Data type for example "Temperature"
     * @param unit     - Unit for example "Celsius"
     */
    public AbstractDataModel(String dataType, String unit) {
        this.dataType = parseDataType(supportedDataTypes, dataType);
        this.unit = unit;
        this.dataPoints = new TreeMap<>();
    }

    /**
     * Constructor for AbstractDataModel
     * Can be used to initialize data model with data points without making
     * a map of data points beforehand. User should give the first timestamp and
     * the interval between data points. The timestamp is then incremented by
     * the interval for each data point.
     * 
     * @param dataType            - Data type for example "Temperature"
     * @param unit                - Unit for example "Celsius"
     * @param firstEntryTimestamp - Timestamp for the first data point in format
     *                            "yyyy-MM-dd HH:mm:ss"
     * @param values              - Array of data points
     * @return AbstractDataModel
     */
    public AbstractDataModel(String dataType, String unit, String firstEntryTimestamp, T[] values) {
        this.dataType = parseDataType(supportedDataTypes, dataType);
        this.unit = unit;
        String timestamp = firstEntryTimestamp;
        this.dataPoints = new TreeMap<>();
        for (T value : values) {
            addDataPoint(timestamp, value);
            timestamp = incrementTimestamp(timestamp);
        }
    }

    /**
     * Returns timestamp that is incremented by interval.
     * 
     * @param timestamp
     * @return
     */
    public static String incrementTimestamp(String timestamp) {
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Duration interval = Duration.ofHours(1);
        dateTime = dateTime.plus(interval);
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Returns data type.
     * 
     * @return String
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Returns unit.
     * 
     * @return String
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Returns data points as a collection.
     * 
     * @return Collection<T>
     */
    public Map<String, T> getDataPoints() {
        return dataPoints;
    }

    /**
     * Returns data points as a collection between startTimestamp and endTimestamp.
     * 
     * @param startTimestamp
     * @param endTimestamp
     * @return
     */
    public Map<String, T> getDataPointsWithRange(String startTimestamp, String endTimestamp) {
        Map<String, T> dataPoints = new TreeMap<>();
        for (String timestamp : this.dataPoints.keySet()) {
            if (timestamp.compareTo(startTimestamp) >= 0 && timestamp.compareTo(endTimestamp) <= 0) {
                dataPoints.put(timestamp, this.dataPoints.get(timestamp));
            }
        }
        return dataPoints;
    }

    /**
     * Adds a data point to the data model.
     * 
     * @param timestamp - Timestamp for the data point in format
     *                  "yyyy-MM-dd HH:mm:ss"
     * @param value     - Data point
     */
    public void addDataPoint(String timestamp, T value) {
        if (!isValidTimeStamp(timestamp)) {
            throw new IllegalArgumentException("Timestamp is not valid");
        }
        dataPoints.put(timestamp, value);
    }

    /**
     * Checks if timestamp is valid. Timestamp is valid if it is in format
     * "yyyy-MM-dd HH:mm:ss".
     * 
     * @param timestamp - Timestamp to be checked
     * @return boolean - True if timestamp is valid, false if not
     */
    public static boolean isValidTimeStamp(String timestamp) {
        String regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$";
        if (!timestamp.matches(regex)) {
            return false;
        }
        try {
            LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks that there are no missing datapoints in the data model.
     * Interval is used to check this. Returns true if there are no missing
     * and false if there are.
     * 
     * @return boolean - True if there are no missing data points, false if there
     */
    public boolean checkDataPoints() {
        String timestamp = dataPoints.keySet().iterator().next();
        for (String key : dataPoints.keySet()) {
            if (!timestamp.equals(key)) {
                return false;
            }
            timestamp = incrementTimestamp(timestamp);
        }
        return true;
    }

    @Override
    public String toString() {
        return "AbstractDataModel{" +
                "dataType='" + dataType + '\'' +
                ", unit='" + unit + '\'' +
                ", interval=" + Duration.ofHours(1) +
                ", dataPoints=" + dataPoints +
                '}';
    }
}
