package fi.nordicwatt.types;

/**
 * Contains a list of supported scenes (.fxml files)
 * 
 * @author Antti Hakkarainen
 */
public enum Scenes {
    MainWorkspace("mainworkspace"),
    SaveSettingsWindow("savesettingswindow");

    private final String label;

    private Scenes(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

}
