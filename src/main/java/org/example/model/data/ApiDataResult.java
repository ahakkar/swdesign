package org.example.model.data;

public class ApiDataResult {

    //TODO Tänne tarvittavat attribuutit, jotta APIOperator voi palauttaa oikeanlaisen datan
    private AbstractDataModel<Double> result;
    private ApiDataRequest request;

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
