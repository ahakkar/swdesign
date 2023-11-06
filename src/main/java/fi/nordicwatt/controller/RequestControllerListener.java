package fi.nordicwatt.controller;

import java.io.IOException;

/**
 * Classes listening to RequestController.
 * @author Markus Hissa
 */
public interface RequestControllerListener 
{
    public void LoadScene(String sceneName) throws IOException;
}
