package org.example.model.data;

import java.time.Duration;

import org.example.types.DataType;

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
     * @param interval            - Interval between data points for example if data
     * @param values              - Array of data points
     * @inheritDoc - AbstractDataModel
     */
    public EnergyModel(DataType dataType, String unit, String firstEntryTimestamp, Duration interval, Double[] values) {
        super(dataType, unit, firstEntryTimestamp, interval,
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
    public EnergyModel(DataType dataType, String unit, Duration interval) {
        super(dataType, unit, interval);
    }
}
