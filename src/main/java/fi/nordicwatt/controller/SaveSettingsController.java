package fi.nordicwatt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller for savesettingswindow.fxml
 * @author Markus Hissa
 */
public class SaveSettingsController 
{
    SaveSettingsControllerListener listener;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField presetIdField;

    public void saveButtonAction()
    {

    }

    public void cancelSaveButtonAction()
    {
        System.out.print("Placeholder: cancel");
    }

}
