package org.example.controller;

import java.io.IOException;

import org.example.App;
import org.example.types.Scenes;
import org.example.utils.EnvironmentVariables;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Loads views and controllers. Starts from mainworkspace
 * 
 * @author Antti Hakkarainen
 */
public class PrimaryController {
    private static PrimaryController instance;
    private static Scene scene;
    private Stage stage;

    public PrimaryController() {}

    public static PrimaryController getInstance() {
        if (instance == null) {
            instance = new PrimaryController();
        }
        return instance;
    }

    /**
     * Loads up enviroment variables (api keys) and the initial view
     * 
     * @param stage created in App.class
     * @throws IOException
     */
    public void init(Stage stage) throws IOException {
        if (this.stage == null) {
            this.stage = stage;
        }

        EnvironmentVariables.getInstance();
        EnvironmentVariables.load(".env");

        LoadScene(Scenes.MainWorkspace.toString());
    }

       
    private void LoadScene(String sceneName) throws IOException {
        scene = new Scene(loadFXML(sceneName), 1400, 1000);
        stage.setScene(scene);
        stage.show();
    }


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
