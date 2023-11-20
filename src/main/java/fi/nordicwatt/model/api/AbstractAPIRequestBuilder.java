package fi.nordicwatt.model.api;

import java.io.IOException;

import fi.nordicwatt.utils.Logger;
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
 *  * 
 * @param <T> Self-bounding type for builder pattern.
 * 
 * @author Heikki Hohtari, with help from ChatGTP
 */
public abstract class AbstractAPIRequestBuilder<T extends AbstractAPIRequestBuilder<T>> {
    private final OkHttpClient httpClient = new OkHttpClient();
    protected HttpUrl.Builder urlBuilder;
    protected Request.Builder requestBuilder = new Request.Builder(); // Initialized here
    protected String apiKey;
    protected String dataType;
    protected String startTime;
    protected String endTime;

    // These suppressions mean "I know what I'm doing, don't warn me about it"
    @SuppressWarnings("unchecked") 
    public T withBaseUrl(String baseUrl) {
        this.urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withDataType(String dataType) {
        this.dataType = dataType;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withStartTime(String startTime) {
        this.startTime = startTime;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withEndTime(String endTime) {
        this.endTime = endTime;
        return (T) this;
    }   


    public String getBuiltUrl() {
        return urlBuilder.build().toString();
    }

    protected abstract boolean isValid();

    protected abstract void addPath();

    protected abstract void addQueryParameters();

    public Response execute() throws IOException {
        addPath();
        addQueryParameters();

        if (!isValid()) {
            Logger.log("Validation failed. Start Time: " + startTime + ", End Time: " + endTime);
            throw new IllegalStateException("Request is not valid");
        }


        Request finalRequest = requestBuilder.url(urlBuilder.build()).build();

        return httpClient.newCall(finalRequest).execute();
    }
}
