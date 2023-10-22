package fi.nordicwatt.types;

/**
 * Holds units of measurement. Add more units as needed!
 * 
 * @author Antti Hakkarainen
 */
public enum MeasurementUnit {
    HPA("hPa"),
    CELSIUS("°C"),
    METERS_PER_SECOND("m/s"),
    EURO("€"),
    KILO_WATT_HOUR("kWh"),
    MEGA_WATT_HOUR("MWh"),
    MEGA_WATT("MW"),
    HUMIDITY("%"),
    MILLIMETERS("mm"),
    HOUR("h");

    private final String symbol;

    MeasurementUnit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}