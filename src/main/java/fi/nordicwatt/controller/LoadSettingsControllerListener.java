package fi.nordicwatt.controller;

import fi.nordicwatt.model.datamodel.SettingsData;

/**
 * Classes listening to LoadSettingsController.
 * @author Markus Hissa
 */
public interface LoadSettingsControllerListener
{
    public void loadSettings(SettingsData settings);
}