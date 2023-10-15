package org.example.model.data;

public class ApiDataResult {

    private final AbstractDataModel<Double> result;
    private final ApiDataRequest request;

    public ApiDataResult(AbstractDataModel<Double> result, ApiDataRequest request) {
        this.result = result;
        this.request = request;
    }

    public AbstractDataModel<Double> getResult() {
        return result;
    }

    public ApiDataRequest getRequest() {
        return request;
    }
}
