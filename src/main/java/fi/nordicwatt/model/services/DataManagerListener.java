package fi.nordicwatt.model.services;

import java.util.List;

import fi.nordicwatt.model.data.DataResult;

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
