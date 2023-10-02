package org.example.controller;

/**
 * Singleton. Probably validates data from UI, and returns true/false depending on
 * if data was good. Data good? -> send request to datamanager
 * 
 * @author Antti Hakkarainen
 */
public class RequestDispatcher {
    
    private static RequestDispatcher instance;

    private RequestDispatcher() { }


    /**
     * Creates a new instance if one doesn't exist. Returns it.
     * @return RequestDispatcher instance
     */
    public static RequestDispatcher getInstance() {
        synchronized (RequestDispatcher.class) {
            if (instance == null) {
                instance = new RequestDispatcher();
            }
            return instance;
        }
    }


    /**
     * Validates data from UI and passes it on to datamanager
     * @return
     */
    public Boolean handleDataRequest() {
        // TODO do something with the data
        return false;
    }
}
