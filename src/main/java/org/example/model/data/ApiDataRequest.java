package org.example.model.data;

public class ApiDataRequest {
    private final Class<? extends AbstractDataModel<Double>>  dataClass;
    private final DataRequest dataRequest;

    public ApiDataRequest(
        Class<? extends AbstractDataModel<Double>> dataClass,
        DataRequest dataRequest
    ) {
        this.dataClass = dataClass;
        this.dataRequest = dataRequest;
    }

    public Class<? extends AbstractDataModel<Double>> getDataClass() {
        return dataClass;
    }

    public DataRequest getDataRequest() {
        return dataRequest;
    }
}
