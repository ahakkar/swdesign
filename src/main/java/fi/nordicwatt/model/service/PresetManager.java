package fi.nordicwatt.model.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fi.nordicwatt.model.datamodel.SettingsData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * A Singleton class.
 * Saves the chart settings to a file.
 * Also fetches already stored settings if requested.
 * @author Markus Hissa, with the help of ChatGPT
 */
public final class PresetManager
{   
    private static PresetManager instance;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static PresetManager getInstance() 
    {
        synchronized (PresetManager.class) 
        {
            if (instance == null) 
            {
                instance = new PresetManager();
            }
            return instance;
        }
    }

    // Method to serialize a Map of SettingsData objects to JSON and save it to a file
    public void saveSettingsData(String id, SettingsData settingsData) throws IOException {
        Map<String, SettingsData> settingsDataMap = new TreeMap<>();
        settingsDataMap.put(id, settingsData);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        FileWriter fw = new FileWriter("settings.conf", true);

        // Serialize the map of settingsData to JSON and save it to a file
        objectMapper.writeValue(fw, settingsDataMap);
        System.out.println("Preset saved!");
    }

    // Method to read JSON data from a file, deserialize it into a map of SettingsData objects
    // and return a specific SettingsData object by ID
    public SettingsData loadSettingsData(String id) throws IOException {
        objectMapper.registerModule(new JavaTimeModule());

        // Deserialize JSON from the file into a map of SettingsData objects
        Map<String, SettingsData> settingsDataMap = objectMapper.readValue(new File("settings.conf"), new TypeReference<Map<String, SettingsData>>() {});

        // Retrieve and return the specific SettingsData object by ID
        return settingsDataMap.get(id);
    }
}
