package org.example.model.api;

import org.example.model.data.WeatherModel;
import okhttp3.Response;

/**
 * FMIApiParser - Parses response from FMI API to WeatherModel.
 * 
 * @see APIParserInterface
 */
public class FMIApiParser implements APIParserInterface<WeatherModel> {

    /**
     * parseToDataObject - Parses response to data object.
     * 
     * @param response - Response from API
     * @return Data object
     *
     */
    @Override
    public WeatherModel parseToDataObject(Response response) {
        // Todo: Implement parsing @markus
        return new WeatherModel(null, null, null, null, null, null);

    }
}
