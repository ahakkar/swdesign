package fi.nordicwatt;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import fi.nordicwatt.controller.PrimaryController;

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
