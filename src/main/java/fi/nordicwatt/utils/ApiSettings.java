package fi.nordicwatt.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fi.nordicwatt.Constants;
import fi.nordicwatt.types.APIType;
import javafx.scene.control.Alert.AlertType;

/**
 * Reads stored enviroment variables from .env file, like api keys
 * 
 * @author Antti Hakkarainen
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiSettings {

    private static ApiSettings instance;

    @JsonProperty("apiKeys")
    private static Map<APIType, String> apiKeys;

    public static ApiSettings getInstance() {
        if (instance == null) {
            synchronized (ApiSettings.class) {
                // check again as multiple threads can reach above step
                if (instance == null) {
                    instance = new ApiSettings();
                }                
            }
        }
        return instance;
    }

    public ApiSettings() {
        apiKeys = new HashMap<>();

        for (APIType apiType : APIType.values()) {
            if (apiType.apiKeyRequired()) {
                apiKeys.put(apiType, "");
            }
        }
    }

    public void setApiKey(APIType apiType, String apiKey) {
        if (apiType.apiKeyRequired()) {
            apiKeys.put(apiType, apiKey);
        }
    }

    public String getApiKey(APIType apiType) {
        return apiKeys.getOrDefault(apiType, null);
    }

    // These two setters/getters are for jackson
    public Map<APIType, String> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(Map<APIType, String> apiKeys) {
        ApiSettings.apiKeys = apiKeys;
    }

    private static ApiSettings createNewSettingsFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(Constants.APISETTIGS_FILEPATH), instance);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return instance;
    }

    /**
     * Load API settings from a JSON file
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public static ApiSettings load() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(Constants.APISETTIGS_FILEPATH), ApiSettings.class);
        } 
        catch (FileNotFoundException e) {
            return createNewSettingsFile();
        } 
        catch (JsonMappingException e) {
            return createNewSettingsFile();
        } 
        catch (IOException e) {
            CustomAlerts.displayAlert(
                AlertType.ERROR,
                "Exception from ApiSettings",
                "Something went wrong while trying to load .env file:",
                e.toString()
            );
            Logger.log("[ApiSettings] load() IOException: " + e.getMessage());
        }
        return null;
    }
       
    /**
     * Save API settings to a JSON file
     * 
     * @param filePath
     * @throws IOException
     */
    public void save() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(Constants.APISETTIGS_FILEPATH), this);
    }
}
