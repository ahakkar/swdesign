package fi.nordicwatt.model.api.fingrid;

import fi.nordicwatt.model.api.AbstractAPIRequestBuilder;
import fi.nordicwatt.types.APIType;
import fi.nordicwatt.utils.EnvironmentVariables;

import okhttp3.HttpUrl;

/**
 * Class for building Fingrid API requests.
 * 
 * The class uses the Builder design pattern.
 * 
 * Example on how to use this builder:
 * 
 * <pre>
 * FingridAPIRequestBuilder builder = new FingridAPIRequestBuilder()
 *         .withDataType(dataType) // For instance "241"
 *         .withStartTime(formattedStartTime) // For instance "2023-10-04T00:00:00Z"
 *         .withEndTime(formattedEndTime); // For instance "2023-10-06T00:00:00Z"
 * 
 * Response response = builder.execute();
 * </pre>
 * 
 * @author Heikki Hohtari, with help from ChatGTP
 */
public class FingridAPIRequestBuilder extends AbstractAPIRequestBuilder<FingridAPIRequestBuilder> {

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
