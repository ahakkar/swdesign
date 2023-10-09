package org.example.model.api;

import okhttp3.Response;
import org.example.model.data.AbstractDataModel;

/**
 * APIParserInterface - Interface for API parsers.
 * 
 */
public interface APIParserInterface<M extends AbstractDataModel<?>> {

    /**
     * parseToDataObject - Parses response to data object.
     * 
     * @param response - Response from API
     * @return Data object
     */
    M parseToDataObject(Response response);
}