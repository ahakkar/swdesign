package fi.nordicwatt.types;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * TODO least effort solution for prototype
 * 
 * List supported API key types here
 * Add keys to .env in root folder (same folder with pom and .gitignore)
 * .env file format is, for example:
 * 
 * FINGRID=my_secret_api_key
 * FMI=my_secret_api_key2
 * 
 * @author Antti Hakkarainen
 */
public enum APIType {
    NOAPI("-", false),
    FINGRID("Fingrid", true), 
    FMI("Finnish Meteorological Institute (FMI)", false);

    private final String label;
    private final boolean apiKeyRequired;

    APIType(String label, boolean apiKeyRequired) {
        this.label = label;
        this.apiKeyRequired = apiKeyRequired;
    }

    @Override
    public String toString() {
        return label;
    }

    public boolean apiKeyRequired() {
        return apiKeyRequired;
    }
/* 
    @JsonValue
    public String getValue() {
        return label;
    } */
}
