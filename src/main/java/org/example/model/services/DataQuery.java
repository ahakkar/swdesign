package org.example.model.services;

import java.time.LocalDateTime;
import java.util.OptionalInt;

public class DataQuery {

    private final String dataType;
    private final LocalDateTime starttime;
    private final LocalDateTime endtime;

    public DataQuery(String dataType, LocalDateTime starttime, LocalDateTime endtime) {
        this.dataType = dataType;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public String getDataType() {
        return dataType;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public LocalDateTime getEndtime() {
        return endtime;
    }

}
