package org.example.model.api;

import java.io.IOException;

import org.example.model.data.ApiDataRequest;
import org.example.model.data.ApiDataResult;
import org.example.model.data.EnergyModel;
import org.example.model.data.WeatherModel;

public class APIOperator {

    protected static ApiDataResult getData(ApiDataRequest request) throws InterruptedException, IOException {
        if (request.getDataClass() == WeatherModel.class) {

        } else if (request.getDataClass() == EnergyModel.class) {
            // Tästä voidaan päätellä, että kutsutaan Fingridin APIa @HEIKKI
        }

        Thread.sleep(3000); // Tämän voi poistaa, kun on olemassa oikea API-kutsu

        return new ApiDataResult();
    }
}
