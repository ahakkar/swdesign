package org.example.model.data;

public class ApiDataRequest {

    // TODO Tarvittavien attribuuttien lisäys, jotta Data manager voi kertoa APIOperatorille (APIQueuen kautta), mitä dataa tarvitaan
    private Class dataClass;

    public ApiDataRequest(Class dataClass) {
        this.dataClass = dataClass;
    }

    public Class getDataClass() {
        return dataClass;
    }


}
