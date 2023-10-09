package org.example.model.data;

import java.time.Duration;
import java.time.LocalDateTime;

public class ApiDataRequest {
    private final Class dataClass;
    private final DataRequest dataRequest;

    public ApiDataRequest(Class dataClass, DataRequest dataRequest) {
        this.dataClass = dataClass;
        this.dataRequest = dataRequest;
    }

    public Class getDataClass() {
        return dataClass;
    }

    public DataRequest getDataRequest() {
        return dataRequest;
    }
}
