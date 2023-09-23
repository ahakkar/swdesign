package org.example.types;

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
    FINGRID, 
    FMI;

    public static boolean contains(String value) {
        for (APIType type : APIType.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
