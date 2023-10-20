package fi.nordicwatt.model.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fi.nordicwatt.model.datamodel.SettingsData;
import fi.nordicwatt.types.ChartType;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.RelativeTimePeriod;

public class PresetParsingTest 
{
    @Test
    public void testPresetParsing() 
        throws IOException
    {
        ChartType chartType = ChartType.LINE_CHART;
        DataType xAxis = DataType.TIME;
        DataType yAxis = DataType.WIND;
        Boolean relativeTime = true;
        RelativeTimePeriod relativeTimePeriod = RelativeTimePeriod.LAST_3_DAYS;
        LocalDate starttime = LocalDate.of(2023, 9, 25);
        LocalDate endtime = LocalDate.of(2023, 9, 29);
        String location = "tampere";

        DataManager dataManager = DataManager.getInstance();

        dataManager.savePreset(chartType, xAxis, yAxis, relativeTime, relativeTimePeriod, starttime, endtime, location);
        File file = new File("./settings.conf");
        try ( BufferedReader reader = new BufferedReader(new FileReader(file)) )
        {
            assertEquals(chartType.toString(), reader.readLine());
            assertEquals(xAxis.toString(),reader.readLine());
            assertEquals(yAxis.toString(), reader.readLine());
            assertEquals(relativeTime.toString(), reader.readLine());
            assertEquals(relativeTimePeriod.toString(), reader.readLine());
            assertEquals(starttime.toString(), reader.readLine());
            assertEquals(endtime.toString(), reader.readLine());
            assertEquals(location, reader.readLine());

            SettingsData settings = dataManager.loadPreset();
            assertEquals(chartType, settings.getChartType());
            assertEquals(xAxis, settings.getXAxis());
            assertEquals(yAxis, settings.getYAxis());
            assertEquals(relativeTime, settings.isRelativeTime());
            assertEquals(relativeTimePeriod, settings.getRelativeTimePeriod());
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
