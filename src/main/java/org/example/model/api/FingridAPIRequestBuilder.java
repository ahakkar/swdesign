package org.example.model.api;

import org.example.types.APIType;
import org.example.utils.EnvironmentVariables;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Class for building Fingrid API requests.
 * 
 * The class uses the Builder design pattern.
 * 
 * @author Heikki Hohtari, with help from ChatGTP
 */
public class FingridAPIRequestBuilder extends AbstractAPIRequestBuilder {

    private final EnvironmentVariables envVars = EnvironmentVariables.getInstance();
    private final String API_KEY = envVars.get(APIType.FINGRID);
    private final String API_ENDPOINT = "https://api.fingrid.fi/v1";

    public FingridAPIRequestBuilder() {
        // Setting the default base URL
        withBaseUrl(API_ENDPOINT);

        // Adding the header in the constructor
        this.requestBuilder.header("x-api-key", API_KEY);
    }

    @Override
    public FingridAPIRequestBuilder withBaseUrl(String baseUrl) {
        if (baseUrl == null) {
            this.urlBuilder = new HttpUrl.Builder().host(API_ENDPOINT).scheme("https");
        } else {
            super.withBaseUrl(baseUrl);
        }
        return this;
    }

    @Override
    protected boolean isValid() {
        return dataType != null && startTime != null && endTime != null;
    }

    @Override
    protected void addPath() {
        urlBuilder.addPathSegment("variable");
        urlBuilder.addPathSegment(dataType);
        urlBuilder.addPathSegment("events");
        urlBuilder.addPathSegment("json");
    }

    @Override
    protected void addQueryParameters() {
        urlBuilder.addQueryParameter("start_time", startTime);
        urlBuilder.addQueryParameter("end_time", endTime);
    }

    @Override
    public FingridAPIRequestBuilder withDataType(String dataType) {
        super.withDataType(dataType);
        return this;
    }

    @Override
    public FingridAPIRequestBuilder withStartTime(String startTime) {
        super.withStartTime(startTime);
        return this;
    }

    @Override
    public FingridAPIRequestBuilder withEndTime(String endTime) {
        super.withEndTime(endTime);
        return this;
    }
}
