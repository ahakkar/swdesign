package org.example.model.services;

import java.io.IOException;
import java.time.LocalDate;

import org.example.types.APIType;
import org.example.utils.EnvironmentVariables;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * fetches json data from fingrid api
 * 
 * @author Antti Hakkarainen  
 */
public class FingridApiService {    
    private final EnvironmentVariables envVars = EnvironmentVariables.getInstance();
    private final String API_ENDPOINT = "api.fingrid.fi";
    private final String API_KEY = envVars.get(APIType.FINGRID);
    
    OkHttpClient client = new OkHttpClient();

    public String fetchData(
        String dataType,
        LocalDate start, 
        LocalDate end
    ) throws IOException {
        // TODO add alert or exception or something if params are bad
        if (dataType == "") { return ""; }
        
        HttpUrl url = new HttpUrl.Builder()
            .scheme("https")
            .host(API_ENDPOINT)
            .addPathSegment("v1")
            .addPathSegment("variable")
            .addPathSegment(dataType)
            .addPathSegment("events")
            .addPathSegment("json")
            .addQueryParameter("start_time", start.toString() + "T13:58:00+0000")
            .addQueryParameter("end_time", end.toString() + "T13:58:00+0000")
            .build();

        Request request = new Request.Builder()
            .url(url)            
            .header("x-api-key", API_KEY)
            .build();

        System.out.println("Doing Fingrid API request with URL: " + request.url());


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }  
    } 
}
