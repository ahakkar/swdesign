package fi.nordicwatt.model.api;
/**
 * Class for building FMI API requests.
 * 
 * The class uses the Builder design pattern.
 * 
 * Example on how to use this builder:
 * 
 * <pre>
 * FmiAPIRequestBuilder builder = new FmiAPIRequestBuilder()
 *         .withPlace(place) // For instance "tampere"
 *         .withStartTime(formattedStartTime) // For instance "2023-09-25T00:00:00Z"
 *         .withEndTime(formattedEndTime) // For instance "2023-09-29T00:00:00Z"
 * 
 * Response response = builder.execute();
 * </pre>
 * 
 * @author Markus Hissa
 */
public class FmiAPIRequestBuilder extends AbstractAPIRequestBuilder<FmiAPIRequestBuilder> {

    private final String API_ENDPOINT = "http://opendata.fmi.fi/wfs";

    private String place; // New field for the place parameter

    public FmiAPIRequestBuilder() {
        // Setting the default base URL and static query parameters
        withBaseUrl(API_ENDPOINT);
        urlBuilder.addQueryParameter("service", "WFS")
                .addQueryParameter("version", "2.0.0")
                .addQueryParameter("request", "getFeature")
                .addQueryParameter("storedquery_id", "fmi::observations::weather::multipointcoverage")
                .addQueryParameter("timestep", "60");
    }

    public FmiAPIRequestBuilder withPlace(String place) {
        this.place = place;
        return this;
    }

    @Override
    protected boolean isValid() {
        return place != null && startTime != null && endTime != null;
    }

    @Override
    protected void addPath() {
        // There's no path segment to add for this API
    }

    @Override
    protected void addQueryParameters() {
        urlBuilder.addQueryParameter("place", place);
        urlBuilder.addQueryParameter("parameters", dataType);
        urlBuilder.addQueryParameter("starttime", startTime);
        urlBuilder.addQueryParameter("endtime", endTime);
    }
}
