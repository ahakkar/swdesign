package fi.nordicwatt.model.data;

import java.time.LocalDateTime;
import java.util.UUID;

import fi.nordicwatt.types.DataType;

/**
 * @author Janne Taskinen
 */
public class DataRequest {

    private final DataType dataType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String location;
    private final String id;

    public DataRequest(
        DataType dataType, 
        LocalDateTime startTime,
        LocalDateTime endTime,        
        String location
    ) {
        this.dataType = dataType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.id = UUID.randomUUID().toString();
    }

    public DataType getDataType() {
        return dataType;
    }

    public LocalDateTime getStarttime() {
        return startTime;
    }

    public LocalDateTime getEndtime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DataRequest [dataType=" + dataType + ", endTime=" + endTime + ", id=" + id + ", location=" + location
                + ", startTime=" + startTime + "]";
    }
}