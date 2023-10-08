package org.example.model.api;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Abstract class for building API requests.
 * 
 * The class is abstract because it is not meant to be used directly, but rather
 * extended by classes that implement the specific API requests.
 * 
 * The class uses the Builder design pattern.
 * 
 * @author Heikki Hohtari, with help from ChatGTP
 */
public abstract class AbstractAPIRequestBuilder {
    private final OkHttpClient httpClient = new OkHttpClient();
    protected HttpUrl.Builder urlBuilder;
    protected Request.Builder requestBuilder = new Request.Builder(); // Initialized here
    protected String apiKey;
    protected String dataType;
    protected String startTime;
    protected String endTime;

    public AbstractAPIRequestBuilder() {
        // Empty constructor
    }

    public AbstractAPIRequestBuilder withBaseUrl(String baseUrl) {
        this.urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        return this;
    }

    public AbstractAPIRequestBuilder withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public AbstractAPIRequestBuilder withDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public AbstractAPIRequestBuilder withStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public AbstractAPIRequestBuilder withEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getBuiltUrl() {
        return urlBuilder.build().toString();
    }

    protected abstract boolean isValid();

    protected abstract void addPath();

    protected abstract void addQueryParameters();

    public Response execute() throws IOException {
        if (!isValid()) {
            throw new IllegalStateException("Request is not valid");
        }

        addPath();
        addQueryParameters();
        Request finalRequest = requestBuilder.url(urlBuilder.build()).build();

        return httpClient.newCall(finalRequest).execute();
    }
}
