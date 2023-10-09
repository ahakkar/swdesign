package org.example.model.api;

import org.example.model.data.EnergyModel;
import okhttp3.Response;

/**
 * FingridApiParser - Parses response from Fingrid API to EnergyModel.
 * 
 * @see APIParserInterface
 */
public class FingridApiParser implements APIParserInterface<EnergyModel> {

    /**
     * parseToDataObject - Parses response to data object.
     * 
     * @param response - Response from API
     * @return Data object
     */
    @Override
    public EnergyModel parseToDataObject(Response response) {
        // Todo: Implement parsing @heikki
        return new EnergyModel(null, null, null);

    }

}
