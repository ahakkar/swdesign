package org.example.model.data;

/**
 * 
 * @author ???
 */
public class ApiDataResult {

    private final AbstractDataModel<Double> result;
    private final ApiDataRequest request;

    /**
     * ApiDataResult - Constructor for ApiDataResult.
     * @param data AbstractDataModel<Double> data contained in DataModel
     * @param request ApiDataRequest request
     */
    public ApiDataResult(AbstractDataModel<Double> data, ApiDataRequest request) {
        this.result = data;
        this.request = request;
    }

    public AbstractDataModel<Double> getResult() {
        return result;
    }

    public ApiDataRequest getRequest() {
        return request;
    }
}
