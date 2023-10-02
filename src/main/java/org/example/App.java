package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.PrimaryController;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        PrimaryController pc = PrimaryController.getInstance();
        pc.init(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
