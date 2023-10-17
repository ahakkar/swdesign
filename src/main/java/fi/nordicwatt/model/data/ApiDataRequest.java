package fi.nordicwatt.model.data;

public class ApiDataRequest {
    private final DataRequest dataRequest;

    public ApiDataRequest(
        DataRequest dataRequest
    ) {
        this.dataRequest = dataRequest;
    }

    public DataRequest getDataRequest() {
        return dataRequest;
    }
}
