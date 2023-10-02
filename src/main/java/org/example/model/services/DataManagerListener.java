package org.example.model.services;

import org.example.model.data.AbstractDataModel;

import java.util.List;

public interface DataManagerListener {

    void newDataAvailable(List<AbstractDataModel<Double>> data, List<DataQuery> query);
}
