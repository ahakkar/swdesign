package org.example.model.data;

import java.time.Duration;
import java.util.Locale;

/**
 * EnergyModel - Class for storing energy data.
 * 
 * This class is used for storing energy data.
 * It extends AbstractDataModel and adds the data types
 * and units used in the energy data.
 * 
 * @author Markus Hissa
 */
public class WeatherModel extends AbstractDataModel<Double> {

    private String location;

    static {
        for (DataType type : DataType.values()) {
            supportedDataTypes.add(type.name());
        }
    }

    /**
     * DataType - Enum for energy data types.
     */
    public enum DataType {
        // You can add more types here in the future
        WIND,
        TEMPERATURE,
        RAIN,
        HUMIDITY,
        AIR_PRESSURE;

        public static DataType parseDataType(String name) {
            try {
                return DataType.valueOf(name.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid energy data type: " + name);
            }
        }
    }

    /**
     * Constructor for EnergyModel
     * 
     * @param dataType - Data type for example "Temperature"
     * @param unit     - Unit for example "Celsius"
     * @param interval - Interval between data points for example if data is
     *                 collected every 5 minutes, interval is 5 minutes
     * @param -        location for example "Helsinki"
     * @inheritDoc - AbstractDataModel
     */
    public WeatherModel(String dataType, String unit, Duration interval, String location) {
        super(dataType, unit, interval);
        this.location = location;
    }

    /**
     * Constructor for EnergyModel
     * Can be used to initialize data model with data points without making
     * a map of data points beforehand. User should give the first timestamp and
     * the interval between data points. The timestamp is then incremented by
     * the interval for each data point.
     * 
     * @param dataType            - Data type for example "Temperature"
     * @param unit                - Unit for example "Celsius"
     * @param firstEntryTimestamp - Timestamp for the first data point in format
     *                            "yyyy-MM-dd HH:mm:ss"
     * @param interval            - Interval between data points for example if data
     *                            is collected every 5 minutes, interval is 5
     *                            minutes
     * @param values              - Array of data points
     * @inheritDoc - AbstractDataModel
     */
    public WeatherModel(String dataType, String unit, String firstEntryTimestamp, Duration interval, String location,
            Double[] values) {
        super(dataType, unit, firstEntryTimestamp, interval, values);
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }
}
