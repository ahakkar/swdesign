package fi.nordicwatt.model.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fi.nordicwatt.model.data.SettingsData;
import fi.nordicwatt.types.DataType;

public class PresetParsingTest 
{
    @Test
    public void testPresetParsing() 
        throws IOException
    {
        DataType xAxis = DataType.TIME;
        DataType yAxis = DataType.WIND;
        LocalDate starttime = LocalDate.of(2023, 9, 25);
        LocalDate endtime = LocalDate.of(2023, 9, 29);
        String location = "tampere";

        PresetParsing.saveSettings(xAxis, yAxis, starttime, endtime, location);
        File file = new File("./settings.conf");
        try ( BufferedReader reader = new BufferedReader(new FileReader(file)) )
        {
            assertEquals(xAxis.toString(),reader.readLine());
            assertEquals(yAxis.toString(), reader.readLine());
            assertEquals(starttime.toString(), reader.readLine());
            assertEquals(endtime.toString(), reader.readLine());
            assertEquals(location, reader.readLine());

            SettingsData settings = PresetParsing.loadSettings();
            assertEquals(xAxis, settings.getXAxis());
            assertEquals(yAxis, settings.getYAxis());
            assertEquals(starttime.toString(), settings.getStarttime().toString());
            assertEquals(endtime.toString(), settings.getEndtime().toString());
            assertEquals(location, settings.getLocation());
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }


        
    }
}
