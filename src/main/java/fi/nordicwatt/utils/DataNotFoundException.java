package fi.nordicwatt.utils;

/**
 * Thrown when no data was found (from API, Memory, Storage, etc.)
 */
public class DataNotFoundException extends Exception {
    public DataNotFoundException(String message) {
        super(message);
    }
}