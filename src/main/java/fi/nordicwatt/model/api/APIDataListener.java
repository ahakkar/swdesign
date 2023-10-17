package fi.nordicwatt.model.api;

import java.util.ArrayList;

import fi.nordicwatt.model.data.ApiDataResult;

/**
 * 
 * @author ??? 
 */
public interface APIDataListener {

    void newApiDataAvailable(ArrayList<ApiDataResult> data, Exception exception);
}
