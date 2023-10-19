package fi.nordicwatt.model.services;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import fi.nordicwatt.types.DataType;

public class PresetStorageTest 
{
    @Test
    public void testSaveSettings() 
        throws IOException
    {
        DataType xAxis = DataType.TIME;
        DataType yAxis = DataType.WIND;
        LocalDate starttime = LocalDate.of(2023, 9, 25);
        LocalDate endtime = LocalDate.of(2023, 9, 29);
        String location = "tampere";

        PresetStorage.saveSettings(xAxis, yAxis, starttime, endtime, location);
    }
}
