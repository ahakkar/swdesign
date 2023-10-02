package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.Views.PrimaryView;
import org.example.utils.EnvironmentVariables;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    // Load env variables (== api keys at this stage from file)
    @Override
    public void start(Stage stage) throws IOException {
        EnvironmentVariables.getInstance();
        EnvironmentVariables.load(".env");
        new PrimaryView(stage);
    }

    public static void main(String[] args) {
        launch();
    }

}