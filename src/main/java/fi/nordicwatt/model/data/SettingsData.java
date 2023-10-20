package fi.nordicwatt.model.data;

import java.time.LocalDate;

import fi.nordicwatt.types.ChartType;
import fi.nordicwatt.types.DataType;

/**
 * A Class to handle the settings' data.
 * @author Markus Hissa
 */
public class SettingsData 
{
    private final ChartType chartType;
    private final DataType xAxis;
    private final DataType yAxis;
    private final LocalDate starttime;
    private final LocalDate endtime;
    private final String location;

    public SettingsData(ChartType chartType, DataType xAxis, DataType yAxis, LocalDate starttime, LocalDate endtime, String location)
    {
        this.chartType = chartType;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.starttime = starttime;
        this.endtime = endtime;
        this.location = location;
    }

    /**
     * @return ChartType return the chartType
     */
    public ChartType getChartType()
    {
        return chartType;
    }

    /**
     * @return DataType return the xAxis
     */
    public DataType getXAxis() {
        return xAxis;
    }

    /**
     * @return DataType return the yAxis
     */
    public DataType getYAxis() {
        return yAxis;
    }

    /**
     * @return LocalDate return the starttime
     */
    public LocalDate getStarttime() {
        return starttime;
    }

    /**
     * @return LocalDate return the endtime
     */
    public LocalDate getEndtime() {
        return endtime;
    }

    /**
     * @return String return the location
     */
    public String getLocation() {
        return location;
    }

}
