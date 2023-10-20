package fi.nordicwatt.model.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import fi.nordicwatt.model.datamodel.SettingsData;
import fi.nordicwatt.types.ChartType;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.RelativeTimePeriod;

/**
 * A Singleton class.
 * Saves the chart settings to a file.
 * Also fetches already stored settings if requested.
 * @author Markus Hissa
 */
public final class PresetParsing
{   
    private static PresetParsing instance;
    private static final File FILE = new File("./settings.conf");

    public static PresetParsing getInstance() 
    {
        synchronized (PresetParsing.class) 
        {
            if (instance == null) 
            {
                instance = new PresetParsing();
            }
            return instance;
        }
    }

    protected void saveSettings(ChartType chartType, DataType xAxis, DataType yAxis, Boolean relativeTime, RelativeTimePeriod relativeTimePeriod, LocalDate starttime, LocalDate endtime, String location) 
        throws IOException
    {
        try ( FileWriter fileWriter = new FileWriter(FILE,false) )
        {
            fileWriter.write(chartType.toString()+"\n");
            fileWriter.write(xAxis.toString()+"\n");
            fileWriter.write(yAxis.toString()+"\n");
            fileWriter.write(relativeTime.toString()+"\n");
            fileWriter.write(relativeTimePeriod.toString()+"\n");
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

    protected SettingsData loadSettings() 
        throws IOException
    {
        try ( BufferedReader reader = new BufferedReader(new FileReader(FILE)) )
        {
            ChartType chartType = ChartType.valueOf(reader.readLine().replace(' ', '_').toUpperCase());
            DataType xAxis = DataType.valueOf(reader.readLine().toUpperCase());
            DataType yAxis = DataType.valueOf(reader.readLine().toUpperCase());
            Boolean relativeTime = Boolean.valueOf(reader.readLine());
            RelativeTimePeriod relativeTimePeriod = RelativeTimePeriod.valueOf(reader.readLine().replace(' ', '_').toUpperCase());
            LocalDate startime = LocalDate.parse(reader.readLine());
            LocalDate endtime = LocalDate.parse(reader.readLine());
            String location = reader.readLine();

            return new SettingsData(chartType, xAxis, yAxis, relativeTime, relativeTimePeriod, startime, endtime, location);
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        return new SettingsData(null, null, null, null, null, null, null, null);
    }
}
