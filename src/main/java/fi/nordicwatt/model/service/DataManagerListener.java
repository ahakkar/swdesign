package fi.nordicwatt.model.service;

import fi.nordicwatt.model.datamodel.RequestBundle;
import fi.nordicwatt.model.datamodel.ResponseBundle;

/**
 * Listener for DataManager
 * 
 * @author Antti Hakkarainen
 */
public interface DataManagerListener {

    default void dataRequestSuccess(ResponseBundle responses) { }

    default void dataRequestFailure(RequestBundle requests, Exception e) {}
    
}
