package fi.nordicwatt.model.datamodel;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.nordicwatt.types.ChartType;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.RelativeTimePeriod;

/**
 * A Class to handle the settings' data.
 * @author Markus Hissa
 */
public class SettingsData 
{
    private ChartType chartType;
    @JsonProperty("xaxis")
    private DataType xAxis;
    @JsonProperty("yaxis")
    private DataType yAxis;
    private Boolean relativeTime;
    private RelativeTimePeriod relativeTimePeriod;
    private LocalDate starttime;
    private LocalDate endtime;
    private String location;

    public SettingsData()
    {

    }

    public SettingsData(ChartType chartType, DataType xAxis, DataType yAxis, Boolean relativeTime, RelativeTimePeriod relativeTimePeriod, LocalDate starttime, LocalDate endtime, String location)
    {
        this.chartType = chartType;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.relativeTime = relativeTime;
        this.relativeTimePeriod = relativeTimePeriod;
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
     * @return Boolean return the relativeTime
     */
    public Boolean isRelativeTime()
    {
        return relativeTime;
    }

    /**
     * @return RelativeTimePeriod return the relativeTimePeriod
     */
    public RelativeTimePeriod getRelativeTimePeriod()
    {
        return relativeTimePeriod;
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
