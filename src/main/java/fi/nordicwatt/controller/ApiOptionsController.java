package fi.nordicwatt.controller;

import java.io.IOException;

import fi.nordicwatt.types.APIType;
import fi.nordicwatt.utils.ApiSettings;
import fi.nordicwatt.utils.CustomAlerts;
import fi.nordicwatt.utils.Logger;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controls the Api options view, where user can adjust Api options and enter
 * keys for apis which require a key.
 * 
 * @author Antti Hakkarainen
 */
public class ApiOptionsController {

    private static ApiSettings apiSettings = ApiSettings.getInstance();

    @FXML
    Button okButton;

    @FXML
    Button cancelButton;

    @FXML
    Label supportedApiLabel;

    @FXML
    VBox apiKeyVBox;

    @FXML
    public void initialize() {
        initApiOptions();
    }

    @FXML
    void okButtonAction() {
        saveApiOptions();
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancelButtonAction() {
        // Discard settings by just closing the window
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     *  
     */
    private void initApiOptions() {
        // List available API's & Generate a box for each API requiring a key
        String text = supportedApiLabel.getText();
        boolean first = true;

        for (APIType apiType : APIType.values())
        {
            if (apiType == APIType.NOAPI) {
                continue;
            }

            if (apiType.apiKeyRequired()) {
                addApiKeyTextField(apiType);
            } else {
                addApiKeyNotRequiredLabel(apiType);
            }

            if (first) {
                text += apiType.toString();
                first = false;
            } else {
                text += ", " + apiType.toString();
            }
        }

        supportedApiLabel.setText(text);
    }

    private void saveApiOptions() {
        for (Node child : apiKeyVBox.getChildren())
        {
            if (child instanceof TextField)
            {
                TextField textField = (TextField) child;
                String id = textField.getId();
                APIType apiType = APIType.valueOf(id); 
                String apiKey = textField.getText();

                apiSettings.setApiKey(apiType, apiKey);
            }
        }
        
        // save apisettings to file
        try {
            apiSettings.save();
        } catch (IOException e) {
            CustomAlerts.displayAlert(
                AlertType.ERROR,
                "Exception from ApiOptionsControler",
                "Something went wrong while trying to save .env file:",
                e.toString()
            );
            Logger.log("[ApiOptionsController] apiSettings.save() IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generate a box for each API requiring a key
     * Get possible already saved keys from apiSettings
     * @param apiType
     */
    private void addApiKeyTextField(APIType apiType) {
        // Add a label with the api's name & a textfield to apiKeyVBox
        Label apiLabel = new Label(apiType.toString() + " API-key:");
        TextField apiTextField = new TextField(apiSettings.getApiKey(apiType));
        apiTextField.setId(apiType.name());

        apiKeyVBox.getChildren().addAll(apiLabel, apiTextField);
    }


    /**
     * Informs the user about apis which don't require a keyu
     * 
     * @param apiType
     */
    private void addApiKeyNotRequiredLabel(APIType apiType) {
        Label apiLabel = new Label("API-key is not required for " + apiType.toString());
        apiLabel.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        apiKeyVBox.getChildren().addAll(apiLabel);
    }
    
}
