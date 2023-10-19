package fi.nordicwatt.model.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import fi.nordicwatt.types.DataType;

/**
 * Saves the chart settings to a file.
 * Also fetches already stored settings if wanted.
 * @author Markus Hissa
 */
public final class PresetStorage 
{   
    private static final File FILE = new File("./src/main/java/fi/nordicwatt/model/data/settings.conf");

    protected static void saveSettings(DataType xAxis, DataType yAxis, LocalDate starttime, LocalDate endtime, String location) 
        throws IOException
    {
        try(FileWriter fileWriter = new FileWriter(FILE,false))
        {

            fileWriter.write(xAxis.toString()+"\n");
            fileWriter.write(yAxis.getVariableId()+"\n");
            fileWriter.write(starttime.toString()+"\n");
            fileWriter.write(endtime.toString()+"\n");
            fileWriter.write(location);

            System.out.println("Preset saved successfully!");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    protected static void loadSettings()
    {

    }
}
