package org.example.model.data;

<<<<<<< HEAD
import java.time.Duration;
import java.time.LocalDateTime;

public class ApiDataRequest 
{
=======
import org.example.types.APIType;

public class ApiDataRequest {
>>>>>>> parent of f95acf8 (separate primary and req controllers)

    // TODO Tarvittavien attribuuttien lisäys, jotta Data manager voi kertoa APIOperatorille (APIQueuen kautta), mitä dataa tarvitaan
    private final Class dataClass;
    private String location;
    private final LocalDateTime starttime;
    private final LocalDateTime endtime;
    private final Duration interval;
    private final String dataType;

    public ApiDataRequest(Class dataClass, String location, LocalDateTime starttime, LocalDateTime endtime, Duration interval, String dataType) 
    {
        this.dataClass = dataClass;
        this.location = location;
        this.starttime = starttime;
        this.endtime = endtime;
        this.interval = interval;
        this.dataType = dataType;
    }

    public ApiDataRequest(Class dataClass, LocalDateTime starttime, LocalDateTime endtime, Duration interval, String dataType) 
    {
        this.dataClass = dataClass;
        this.starttime = starttime;
        this.endtime = endtime;
        this.interval = interval;
        this.dataType = dataType;
    }

    public Class getDataClass() {
        return dataClass;
    }

    public String getLocation()
    {
        return location;
    }

    public LocalDateTime getStarttime()
    {
        return starttime;
    }

    public LocalDateTime getEndtime()
    {
        return endtime;   
    }

    public Duration getInterval()
    {
        return interval;
    }

    public String getDataType()
    {
        return dataType;
    }

}
