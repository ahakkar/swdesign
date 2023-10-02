package org.example.model.api;

import org.example.model.data.*;

public class APIOperator {

    protected static ApiDataResult getData(ApiDataRequest request) throws InterruptedException {
        if (request.getDataClass() == WeatherModel.class) {
            // Tästä voidaan päätellä, että kutsutaan Ilmatieteenlaitoksen APIa @MARKUS
        } else if (request.getDataClass() == EnergyModel.class) {
            // Tästä voidaan päätellä, että kutsutaan Fingridin APIa @HEIKKI
        }

        Thread.sleep(3000); // Tämän voi poistaa, kun on olemassa oikea API-kutsu

        return new ApiDataResult();
    }
}
