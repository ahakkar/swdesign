package org.example.model.data;

import java.time.LocalDateTime;
import java.util.OptionalInt;

public class DataQuery {

    private final String dataType;
    private final LocalDateTime starttime;
    private final LocalDateTime endtime;
    private final String location;

    public DataQuery(String dataType, LocalDateTime starttime, LocalDateTime endtime, String location) {
        this.dataType = dataType;
        this.starttime = starttime;
        this.endtime = endtime;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

}
