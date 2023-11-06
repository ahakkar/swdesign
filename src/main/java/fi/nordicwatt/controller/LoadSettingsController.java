package fi.nordicwatt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

/**
 * Controller for loadsettingswindow.fxml
 * @author Markus Hissa
 */
public class LoadSettingsController 
{
    @FXML
    private Button loadButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox choosePresetBox;

    public void initialize()
    {
        initializeChoosePresetBox();
    }

    private void initializeChoosePresetBox()
    {

    }

    private void loadButtonAction()
    {

    }

    private void cancelLoadButtonAction()
    {
        
    }
}
