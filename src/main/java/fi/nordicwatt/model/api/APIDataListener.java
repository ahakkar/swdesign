package fi.nordicwatt.model.api;

import fi.nordicwatt.model.datamodel.RequestBundle;
import fi.nordicwatt.model.datamodel.ResponseBundle;

/**
 * 
 * @author ??? 
 */
public interface APIDataListener {
    void APIDataRequestSuccess(ResponseBundle responses);

    void APIDataRequestFailure(RequestBundle requests, Exception e);
}
