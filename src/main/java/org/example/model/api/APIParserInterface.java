package org.example.model.api;

import org.example.model.data.AbstractDataModel;
import org.example.model.data.ApiDataRequest;

/**
 * APIParserInterface - Interface for API parsers.
 * 
 */
public interface APIParserInterface<T extends AbstractDataModel<?>> {

    /**
     * parseToDataObject - Parses response to data object.
     * 
     * @param request - Request for API
     * @param response - Response from API
     * @return Data object
     */
    T parseToDataObject(ApiDataRequest request, String response) throws ParseException;

    class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
            System.err.println(message);
        }
    
        public ParseException(String message, Throwable cause) {
            super(message, cause);
            System.err.println(message);
            System.err.println("Cause: " + cause.getMessage());
            cause.printStackTrace();
        }
    }
    
    class NoDataException extends ParseException {
        public NoDataException(String message) {
            super(message);
        }
    }
}