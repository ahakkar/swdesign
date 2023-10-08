package org.example.model.data;

import java.time.Duration;
import java.time.LocalDateTime;

public class ApiDataRequest 
{

    // TODO Tarvittavien attribuuttien lisäys, jotta Data manager voi kertoa APIOperatorille (APIQueuen kautta), mitä dataa tarvitaan
    private final Class dataClass;
    private String location;
    private final LocalDateTime starttime;
    private final LocalDateTime endtime;
    private final String dataType;

    public ApiDataRequest(Class dataClass, String location, LocalDateTime starttime, LocalDateTime endtime, String dataType) 
    {
        this.dataClass = dataClass;
        this.location = location;
        this.starttime = starttime;
        this.endtime = endtime;
        this.dataType = dataType;
    }

    public ApiDataRequest(Class dataClass, LocalDateTime starttime, LocalDateTime endtime, Duration interval, String dataType) 
    {
        this.dataClass = dataClass;
        this.starttime = starttime;
        this.endtime = endtime;
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

    public String getDataType()
    {
        return dataType;
    }

}
