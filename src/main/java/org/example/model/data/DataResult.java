package org.example.model.data;

public class DataResult {

    private DataRequest request;
    private AbstractDataModel<Double> data;

    public DataResult(DataRequest request, AbstractDataModel<Double> data) {
        this.request = request;
        this.data = data;
    }

    public DataRequest getRequest() {
        return request;
    }

    public AbstractDataModel<Double> getData() {
        return data;
    }
}
