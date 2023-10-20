package fi.nordicwatt.model.datamodel;

import fi.nordicwatt.model.data.DataResponse;

public class ResponseBundle extends Bundle<DataResponse> {

    private final String requestId;

    public ResponseBundle(String requestId) {
        super(); // Calls the constructor of the parent class (Bundle)
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }
}
