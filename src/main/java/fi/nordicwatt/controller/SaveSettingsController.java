package fi.nordicwatt.controller;

import java.io.IOException;
import java.util.ArrayList;

import fi.nordicwatt.model.service.DataManager;
import fi.nordicwatt.utils.CustomAlerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for savesettingswindow.fxml
 * 
 * @author Markus Hissa
 */
public class SaveSettingsController {
    private final DataManager dataManager = DataManager.getInstance();
    private static SaveSettingsController instance;
    private static final ArrayList<SaveSettingsControllerListener> listeners = new ArrayList<>();

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField presetIdField;

    public static SaveSettingsController getInstance() {
        if (instance == null) {
            instance = new SaveSettingsController();
        }
        return instance;
    }

    public void addListener(SaveSettingsControllerListener listener) {
        listeners.add(listener);
    }

    @FXML
    public void saveButtonAction() throws IOException {
        String currentPresetId = presetIdField.toString().trim();

        // Check if preset name is empty
        if (currentPresetId.equals("")) {
            CustomAlerts.displayAlert(AlertType.ERROR, "Save error",
                    "Please enter a name for your preset.");
            return;
        }

        // Check if ID already exists
        ArrayList<String> existingIds = dataManager.getPresetIds();
        if (existingIds.contains(currentPresetId)) {
            CustomAlerts.displayAlert(AlertType.ERROR, "Error while saving a preset", "Preset"
                    + currentPresetId + " already exists.\n Please use another name for a preset.");
            return;
        }

        for (SaveSettingsControllerListener listener : listeners) {
            listener.saveSettings(presetIdField.getCharacters().toString().trim());
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelSaveButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
