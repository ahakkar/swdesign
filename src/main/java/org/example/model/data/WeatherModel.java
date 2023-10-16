package org.example.model.data;

import java.util.Locale;

/**
 * WeatherModel - Class for storing weather data.
 * 
 * This class is used for storing weather data.
 * It extends AbstractDataModel and adds the data types
 * and units used in the weather data.
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
     * DataType - Enum for weather data types.
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
     * Constructor for WeatherModel
     * 
     * @param dataType - Data type for example "Temperature"
     * @param unit     - Unit for example "Celsius"
     * @param -        location for example "Helsinki"
     * @inheritDoc - AbstractDataModel
     */
    public WeatherModel(String dataType, String unit, String location) {
        super(dataType, unit);
        this.location = location;
    }

    /**
     * Constructor for WeatherModel
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
     * @inheritDoc - AbstractDataModel
     */
    public WeatherModel(String dataType, String unit, String firstEntryTimestamp, String location,
            Double[] values) {
        super(dataType, unit, firstEntryTimestamp, values);
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }
}
