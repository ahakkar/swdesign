package fi.nordicwatt.controller;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for savesettingswindow.fxml
 * @author Markus Hissa
 */
public class SaveSettingsController 
{
    private static SaveSettingsController instance;
    private static final ArrayList<SaveSettingsControllerListener> listeners = new ArrayList<>();

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField presetIdField;

    public static SaveSettingsController getInstance() 
    {
        if (instance == null) 
        {
            instance = new SaveSettingsController();
        }
        return instance;
    }

    public void addListener(SaveSettingsControllerListener listener)
    {
        listeners.add(listener);
    }

    @FXML
    public void saveButtonAction()
    {
        for ( SaveSettingsControllerListener listener : listeners )
        {
            listener.saveSettings(presetIdField.getCharacters().toString()); 
        }
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void cancelSaveButtonAction()
    {
        System.out.print("Placeholder: cancel");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
