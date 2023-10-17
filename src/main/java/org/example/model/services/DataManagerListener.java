package org.example.model.services;

import org.example.model.data.DataResult;

import java.util.List;

/**
 * Listener for DataManager
 * 
 * @author Antti Hakkarainen
 */
public interface DataManagerListener {

    /**
     * Called when data is ready for a Chart generation
     * 
     * @param data       List of DataResults
     * @param e          Exception if anything went wrong while fetching data
     * @throws Exception if anything goes wrong while fetching data
     */
    default void onDataReadyForChart(List<DataResult> data, Exception e)  {}
}
