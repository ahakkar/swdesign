package fi.nordicwatt.model.datamodel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.MeasurementUnit;

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

    private DataType dataType;
    private MeasurementUnit unit;
    private Map<LocalDateTime, T> dataPoints;

    public AbstractDataModel(DataType dataType, MeasurementUnit unit) {
        this.dataType = dataType;
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
    public AbstractDataModel(
            DataType dataType,
            MeasurementUnit unit,
            LocalDateTime firstEntryTimestamp,
            T[] values) {
        this.dataType = dataType;
        this.unit = unit;
        LocalDateTime timestamp = firstEntryTimestamp;
        this.dataPoints = new TreeMap<>();
        for (T value : values) {
            addDataPoint(timestamp, value);
            timestamp = incrementTimestamp(timestamp, dataType.getInterval());
        }
    }

    /**
     * Returns timestamp that is incremented by one hour.
     * 
     * @param timestamp
     * @return
     */
    public static LocalDateTime incrementTimestamp(LocalDateTime timestamp, Duration interval) {
        LocalDateTime dateTime = timestamp;
        dateTime = dateTime.plus(interval);
        return dateTime;
    }

    /**
     * Returns data type.
     * 
     * @return String
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * Returns unit.
     * 
     * @return String
     */
    public MeasurementUnit getUnit() {
        return unit;
    }

    /**
     * Returns data points as a collection.
     * 
     * @return Collection<T>
     */
    public Map<LocalDateTime, T> getDataPoints() {
        return dataPoints;
    }

    /**
     * Returns data points as a collection between startTimestamp and endTimestamp.
     * 
     * @param startTimestamp
     * @param endTimestamp
     * @return
     */
    public Map<LocalDateTime, T> getDataPointsWithRange(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        Map<LocalDateTime, T> dataPoints = new TreeMap<>();
        for (LocalDateTime timestamp : this.dataPoints.keySet()) {
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
     * @throws IllegalArgumentException if timestamp is not valid
     */
    public boolean addDataPoint(LocalDateTime timestamp, T value) {

        if (value != null) {
            dataPoints.put(timestamp, value);
            return true;
        } else {
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
        LocalDateTime timestamp = dataPoints.keySet().iterator().next();
        for (LocalDateTime key : dataPoints.keySet()) {
            if (!timestamp.equals(key)) {
                return false;
            }
            timestamp = incrementTimestamp(timestamp, this.dataType.getInterval());
        }
        return true;
    }

    public void merge(AbstractDataModel<T> other) throws IllegalArgumentException {
        if (this.dataType != other.dataType) {
            throw new IllegalArgumentException("Data types do not match");
        }
        if (!Objects.equals(this.unit, other.unit)) {
            throw new IllegalArgumentException("Units do not match");
        }
        this.dataPoints.putAll(other.dataPoints);
    }

    public <E extends AbstractDataModel> void combineModels(E model) {
        dataPoints.putAll(model.getDataPoints());
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
