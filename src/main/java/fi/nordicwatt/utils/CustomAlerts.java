package fi.nordicwatt.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * In case we want to use some alerts..
 * 
 * @author Antti Hakkarainen 
 */
public class CustomAlerts {
    /**
     * Displays an alert with the given type, title and content.
     * 
     * @param type      AlertType type
     * @param title     String title
     * @param content   String content
     */
    public static void displayAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Displays an alert with the given type, title, header and content.
     * 
     * @param type      AlertType type
     * @param title     String title
     * @param header    String header
     * @param content   String content
     */
    public static void displayAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
