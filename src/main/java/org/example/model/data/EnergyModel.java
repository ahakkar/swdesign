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
 * @author Heikki Hohtari with help of github copilot
 */
public class EnergyModel extends AbstractDataModel<Double> {

    /**
     * DataType - Enum for energy data types.
     */
    public enum DataType {
        // You can add more types here in the future
        TOTAL_CONSUMPTION,
        TOTAL_PRODUCTION,
        HYDRO_PRODUCTION,
        NUCLEAR_PRODUCTION,
        WIND_PRODUCTION;

        /**
         * Parse data type from string.
         * 
         * @throws IllegalArgumentException if data type is invalid
         * @param dataType - Data type as string
         * @return - Data type as enum
         */
        public static DataType parseDataType(String dataType) {
            for (DataType type : DataType.values()) {
                if (type.name().equalsIgnoreCase(dataType)) {
                    return type;
                }
            }
            throw new IllegalArgumentException(
                    "Invalid dataType. Allowed values are: " + String.join(", ", getNames()));
        }

        /**
         * Get names of data types as string array.
         * 
         * @return - Data types as string array
         */
        public static String[] getNames() {
            DataType[] types = values();
            String[] names = new String[types.length];

            for (int i = 0; i < types.length; i++) {
                names[i] = types[i].name().toLowerCase(Locale.ROOT);
            }
            return names;
        }

    }

    /**
     * Constructor for EnergyModel. Can be used to initialize data model with data
     * points without making a map of data points beforehand. User should give the
     * first timestamp and the interval between data points. The timestamp is then
     * incremented by the interval for each data point.
     * 
     * @param dataType            - Data type for example "Temperature"
     * @param unit                - Unit for example "Celsius"
     * @param firstEntryTimestamp - Timestamp for the first data point in format
     * @param interval            - Interval between data points for example if data
     * @param values              - Array of data points
     * @inheritDoc - AbstractDataModel
     */
    public EnergyModel(String dataType, String unit, String firstEntryTimestamp, Duration interval, Double[] values) {
        super(DataType.parseDataType(dataType).name().toLowerCase(Locale.ROOT), unit, firstEntryTimestamp, interval,
                values);
    }

    /**
     * Constructor for EnergyModel. Can be used to initialize data model with data
     * points without making a map of data points beforehand. User should give the
     * first timestamp and the interval between data points. The timestamp is then
     * incremented by the interval for each data point.
     * 
     * @param dataType - Data type for example "Temperature"
     * @param unit     - Unit for example "Celsius"
     * @param interval - Interval between data points for example if data
     * @inheritDoc - AbstractDataModel
     */
    public EnergyModel(String dataType, String unit, Duration interval) {
        super(DataType.parseDataType(dataType).name().toLowerCase(Locale.ROOT), unit, interval);
    }
}
