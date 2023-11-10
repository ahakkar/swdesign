package fi.nordicwatt.controller;

import java.io.IOException;
import java.util.ArrayList;

import fi.nordicwatt.model.datamodel.SettingsData;
import fi.nordicwatt.model.service.DataManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * Controller for loadsettingswindow.fxml
 * @author Markus Hissa
 */
public class LoadSettingsController 
{
    private final DataManager dataManager = DataManager.getInstance();
    private static LoadSettingsController instance;
    private static final ArrayList<LoadSettingsControllerListener> listeners = new ArrayList<>();

    @FXML
    private Button loadButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox choosePresetBox;

    public static LoadSettingsController getInstance() 
    {
        if (instance == null) 
        {
            instance = new LoadSettingsController();
        }
        return instance;
    }

    public void initialize() throws IOException
    {
        initializeChoosePresetBox();
    }

    private void initializeChoosePresetBox() throws IOException
    {
        choosePresetBox.setItems((ObservableList) dataManager.getPresetIds());
    }

    public void loadButtonAction() throws IOException
    {
        String id = String.valueOf(choosePresetBox.getValue());
        SettingsData settingsData = dataManager.loadPreset(id);
        for ( LoadSettingsControllerListener listener : listeners )
        {
            listener.loadSettings(settingsData);
        }
        System.out.println("Preset "+id+" loaded successfully.");
        Stage stage = (Stage) loadButton.getScene().getWindow();
        stage.close();
    }

    public void cancelLoadButtonAction()
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void addListener(LoadSettingsControllerListener listener)
    {
        listeners.add(listener);
    }
}