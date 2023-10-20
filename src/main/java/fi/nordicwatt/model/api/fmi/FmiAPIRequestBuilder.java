package fi.nordicwatt.model.api.fmi;

import fi.nordicwatt.model.api.AbstractAPIRequestBuilder;

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

    public FmiAPIRequestBuilder() {
        // Setting the default base URL and static query parameters
        withBaseUrl(this.API_ENDPOINT);          
    }

    @Override
    protected void addPath() {

    }

    public FmiAPIRequestBuilder withLocation(String location) {
        urlBuilder.addQueryParameter("place", location);
        return this;
    }

    @Override
    public FmiAPIRequestBuilder withDataType(String dataType) {
        urlBuilder.addQueryParameter("parameters", dataType);
        return this;
    }

    @Override
    public FmiAPIRequestBuilder withStartTime(String startTime) {
        super.withStartTime(startTime);
        urlBuilder.addQueryParameter("starttime", startTime);
        return this;
    }

    @Override
    public FmiAPIRequestBuilder withEndTime(String endTime) {
        super.withEndTime(endTime);
        urlBuilder.addQueryParameter("endtime", endTime);
        return this;
    }

    public FmiAPIRequestBuilder withTimestep(String timestep) {        
        urlBuilder.addQueryParameter("timestep", timestep);
        return this;
    }

    @Override
    protected boolean isValid() {
        return startTime != null && endTime != null;
    }

    @Override
    protected void addQueryParameters() {
        urlBuilder.addQueryParameter("service", "WFS");
        urlBuilder.addQueryParameter("version", "2.0.0");
        urlBuilder.addQueryParameter("request", "getFeature");
        urlBuilder.addQueryParameter("storedquery_id", "fmi::observations::weather::multipointcoverage");
    }
}
