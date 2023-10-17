package fi.nordicwatt.model.session;

/**
 * ID and name of a tab, possibly something else too later?
 * 
 * @author Antti Hakkarainen
 */
public class TabInfo {
    private final String uuid;
    private String title;

    public TabInfo(String uuid, String title) {
        this.uuid = uuid;
        this.title = title;
    }
    // getters and setters


    public String getId() {
        return uuid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
