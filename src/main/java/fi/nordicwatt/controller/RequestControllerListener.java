package fi.nordicwatt.controller;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classes listening to RequestController.
 * @author Markus Hissa
 */
public interface RequestControllerListener 
{
    public Scene LoadScene(String sceneName, Stage stageToUse, int width, int height) throws IOException;
}
