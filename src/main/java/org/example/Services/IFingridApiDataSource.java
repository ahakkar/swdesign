package org.example.Services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.example.Models.DataPoint;

/**
 * Interface for Controller to use to get the data from Fingrid's api it needs
 * Apparently Fingrid's data has always the same format, only three variables are
 * variable_id, start and end times...
 * 
 * @author Antti Hakkarainen
 */
public interface IFingridApiDataSource {
    List<DataPoint> getFixedData(String datatype, LocalDate start, LocalDate end) throws IOException;
}