package org.example.controller.factory;


import org.example.model.data.AbstractDataModel;
import org.example.model.data.ChartRequest;

/**
 * Data Transfer Object, DTO to pass params to chart creator classes
 * 
 * @author Antti Hakkarainen
 */
public class ChartParams {

    private final AbstractDataModel<Double> data;
    private final ChartRequest request;

    public ChartParams(
        AbstractDataModel<Double> data,
        ChartRequest request
    ) {        
        this.data = data;
        this.request = request;
    };

    public AbstractDataModel<Double> getData() {
        return data;
    }

    public ChartRequest getRequest() {
        return request;
    }
    
}
