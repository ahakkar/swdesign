package fi.nordicwatt;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import fi.nordicwatt.controller.PrimaryController;

import fi.nordicwatt.utils.Logger;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        PrimaryController pc = PrimaryController.getInstance();
        stage.setTitle("NordicWatt");
        stage.getIcons().add(new Image("file:doc/logo_small.png"));
        pc.init(stage);
    }

    public static void main(String[] args) {
        Logger.log("\n==================================================\n" +
            "Starting application\n" +
            "==================================================");
        launch(args);
    }

}
