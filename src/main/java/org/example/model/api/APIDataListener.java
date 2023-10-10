package org.example.model.api;

import org.example.model.data.ApiDataResult;

import java.util.ArrayList;

public interface APIDataListener {

    void newApiDataAvailable(ArrayList<ApiDataResult> data, Exception exception);
}
