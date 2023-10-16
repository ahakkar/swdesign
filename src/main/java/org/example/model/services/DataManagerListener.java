package org.example.model.services;

import org.example.model.data.DataResult;

import java.util.List;

/**
 * TODO comment for javadoc
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


    /**
     * TODO remove if no other uses are found
     */
    default void onDataReadyForSomethingElse() {}
}
