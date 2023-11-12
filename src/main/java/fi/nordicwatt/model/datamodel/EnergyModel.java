package fi.nordicwatt.model.datamodel;

import java.time.LocalDateTime;

import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.MeasurementUnit;

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
    public EnergyModel(DataType dataType, MeasurementUnit unit, LocalDateTime firstEntryTimestamp, Double[] values) {
        super(dataType, unit, firstEntryTimestamp, values);
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
    public EnergyModel(DataType dataType, MeasurementUnit unit) {
        super(dataType, unit);
    }
}
