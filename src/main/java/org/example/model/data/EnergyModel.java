package org.example.model.data;

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
        TOTAL_CONSUMPTION,
        TOTAL_PRODUCTION,
        HYDRO_PRODUCTION,
        NUCLEAR_PRODUCTION,
        WIND_PRODUCTION;

        public static DataType parseDataType(String name) {
            try {
                return DataType.valueOf(name.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid energy data type: " + name);
            }
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
     * @param values              - Array of data points
     * @inheritDoc - AbstractDataModel
     */
    public EnergyModel(String dataType, String unit, String firstEntryTimestamp, Double[] values) {
        super(AbstractDataModel.parseDataType(supportedDataTypes, dataType), unit, firstEntryTimestamp, values);
    }

    /**
     * Constructor for EnergyModel. Can be used to initialize data model with data
     * points without making a map of data points beforehand. User should give the
     * first timestamp and the interval between data points. The timestamp is then
     * incremented by the interval for each data point.
     * 
     * @param dataType - Data type for example "Temperature"
     * @param unit     - Unit for example "Celsius"
     * @inheritDoc - AbstractDataModel
     */
    public EnergyModel(String dataType, String unit) {
        super(DataType.parseDataType(dataType).name().toLowerCase(Locale.ROOT), unit);
    }
}
