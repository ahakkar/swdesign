package fi.nordicwatt.model.data;

import java.util.UUID;

import fi.nordicwatt.model.datamodel.AbstractDataModel;

public class DataResponse {
    private final DataRequest request;
    private final AbstractDataModel<Double> data;
    private final String id;
    
    public DataResponse(DataRequest request, AbstractDataModel<Double> data) {
        this.request = request;
        this.data = data;
        this.id = UUID.randomUUID().toString();
    }

    public DataRequest getRequest() {
        return request;
    }

    public AbstractDataModel<Double> getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DataResponse [data=" + data + ", id=" + id + ", request=" + request + "]";
    }
}
