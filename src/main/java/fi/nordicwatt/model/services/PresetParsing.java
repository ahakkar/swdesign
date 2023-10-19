package fi.nordicwatt.model.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import fi.nordicwatt.model.data.SettingsData;
import fi.nordicwatt.types.DataType;

/**
 * Saves the chart settings to a file.
 * Also fetches already stored settings if requested.
 * @author Markus Hissa
 */
public final class PresetParsing
{   
    private static final File FILE = new File("./settings.conf");

    protected static void saveSettings(DataType xAxis, DataType yAxis, LocalDate starttime, LocalDate endtime, String location) 
        throws IOException
    {
        try ( FileWriter fileWriter = new FileWriter(FILE,false) )
        {

            fileWriter.write(xAxis.toString()+"\n");
            fileWriter.write(yAxis.toString()+"\n");
            fileWriter.write(starttime.toString()+"\n");
            fileWriter.write(endtime.toString()+"\n");
            fileWriter.write(location);

            System.out.println("Preset saved successfully!");
        }
        catch ( FileNotFoundException e ) 
        {
            e.printStackTrace();
        }

    }

    protected static SettingsData loadSettings() 
        throws IOException
    {
        try ( BufferedReader reader = new BufferedReader(new FileReader(FILE)) )
        {
            DataType xAxis = DataType.valueOf(reader.readLine().toUpperCase());
            DataType yAxis = DataType.valueOf(reader.readLine().toUpperCase());
            LocalDate startime = LocalDate.parse(reader.readLine());
            LocalDate endtime = LocalDate.parse(reader.readLine());
            String location = reader.readLine();

            return new SettingsData(xAxis, yAxis, startime, endtime, location);
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        return new SettingsData(null, null, null, null, null);
    }
}
