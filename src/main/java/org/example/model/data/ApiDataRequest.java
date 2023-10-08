package org.example.model.data;

import java.time.Duration;
import java.time.LocalDateTime;

public class ApiDataRequest 
{
    private final Class dataClass;
    private DataQuery dataQuery;
    public ApiDataRequest(Class dataClass, DataQuery dataQuery)
    {
        this.dataClass = dataClass;
        this.dataQuery = dataQuery;
    }


    public Class getDataClass() {
        return dataClass;
    }

    public DataQuery getDataQuery() {
        return dataQuery;
    }
}
