package fi.nordicwatt.types;

/**
 * List supported API key types here. Api keys (when needed) are given via a GUI dialog by the user.
 * 
 * @author Antti Hakkarainen
 */
public enum APIType {
    NOAPI("-", false), FINGRID("Fingrid", true), FMI("Finnish Meteorological Institute (FMI)",
            false);

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
}
