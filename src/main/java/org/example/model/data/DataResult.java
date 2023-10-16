package org.example.model.data;

/**
 * 
 * @author ???
 */
public class DataResult {

    private DataRequest request;
    private AbstractDataModel<Double> data;
    private Exception exception;

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

    public Exception getException() {
        return exception;
    }
}
