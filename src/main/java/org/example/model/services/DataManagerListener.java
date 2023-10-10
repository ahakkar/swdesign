package org.example.model.services;

import org.example.model.data.AbstractDataModel;
import org.example.model.data.DataRequest;
import org.example.model.data.DataResult;

import java.util.List;

public interface DataManagerListener {

    void onDataReady(List<DataResult> data, Exception exception);
}
