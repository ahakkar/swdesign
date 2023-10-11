package org.example.model.services;

import org.example.model.data.DataResult;

import java.util.List;

public interface DataManagerListener {

    default void onDataReadyForChart(List<DataResult> data, Exception e) {}

    default void onDataReadyForSomethingElse() {}
}
