package fi.nordicwatt.model.service;

import fi.nordicwatt.model.datamodel.AbstractDataModel;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.datamodel.WeatherModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.MeasurementUnit;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataStorageTest {

    @Test
    public void testAddData() {
        DataStorage storage = DataStorage.getInstance();
        Double[] values =  new Double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 9.0, 11.0, 12.4, -4.4, 1.0, -4.0}; // 16 values
        WeatherModel model = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "2023-01-01 01:00:00","tampere", values);
        storage.addData(model);


        WeatherModel data1 = (WeatherModel) getData("2023-01-01 01:00:00", "2023-01-01 16:00:00", DataType.TEMPERATURE);
        WeatherModel data2 = (WeatherModel) getData("2023-01-01 01:00:00", "2023-01-01 17:00:00", DataType.TEMPERATURE);
        WeatherModel data3 = (WeatherModel) getData("2023-01-01 00:00:00", "2023-01-01 10:00:00", DataType.TEMPERATURE);
        WeatherModel data4 = (WeatherModel) getData("2023-01-01 05:00:00", "2023-01-01 08:00:00", DataType.TEMPERATURE);

        assertTrue(data1.getDataPoints().size() == 16);
        assertTrue(data1.getDataPoints().get("2023-01-01 01:00:00") == 1.0);
        assertTrue(data2 == null);
        assertTrue(data3 == null);
        assertTrue(data4.getDataPoints().size() == 4);

        Double[] values2 =  new Double[]{-4.0, 2.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 9.0, 11.0, 12.4, -4.4, 1.0, -4.0, 5.5}; // 18 values
        WeatherModel model2 = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "2023-01-01 00:00:00","tampere", values2);
        storage.addData(model2);

        WeatherModel data5 = (WeatherModel) getData("2023-01-01 00:00:00", "2023-01-01 17:00:00", DataType.TEMPERATURE);
        assertTrue(data5.getDataPoints().size() == 18);
        assertTrue(data5.getDataPoints().get("2023-01-01 01:00:00") == 2.0);

        Double[] values3 =  new Double[]{11.0, 24.0, -2.0, -3.0, -4.0, 5.05, 6.50}; // 7 values
        WeatherModel model3 = new WeatherModel(DataType.TEMPERATURE, MeasurementUnit.CELSIUS, "2023-01-05 00:00:00","tampere", values3);
        storage.addData(model3);

        WeatherModel data6 = (WeatherModel) getData("2023-01-05 00:00:00", "2023-01-05 06:00:00", DataType.TEMPERATURE);
        assertTrue(data6.getDataPoints().size() == 7);
        assertTrue(data6.getDataPoints().get("2023-01-05 00:00:00") == 11.0);

        // Test for DataType.WIND
        Double[] valuesWind = new Double[]{2.0, 3.0, 4.0, 5.0, 6.0}; // 5 values
        WeatherModel modelWind = new WeatherModel(DataType.WIND, MeasurementUnit.METERS_PER_SECOND, "2023-01-01 01:00:00", "tampere", valuesWind);
        storage.addData(modelWind);

        WeatherModel dataWind = (WeatherModel) getData("2023-01-01 01:00:00", "2023-01-01 05:00:00", DataType.WIND);
        assertTrue(dataWind.getDataPoints().size() == 5);
        assertTrue(dataWind.getDataPoints().get("2023-01-01 01:00:00") == 2.0);

        // Test for DataType.RAIN rain is depricated for now. 
        /*Double[] valuesRain = new Double[]{1.0, 2.0, 0.5, 1.5}; // 4 values
        WeatherModel modelRain = new WeatherModel(DataType.RAIN, MeasurementUnit.MILLIMETERS, "2023-01-01 01:00:00", "tampere", valuesRain);
        storage.addData(modelRain);

        WeatherModel dataRain = (WeatherModel) getData("2023-01-01 01:00:00", "2023-01-01 04:00:00", DataType.RAIN);
        assertTrue(dataRain.getDataPoints().size() == 4);
        assertTrue(dataRain.getDataPoints().get("2023-01-01 01:00:00") == 1.0);
*/
        // Test for DataType.HUMIDITY
        Double[] valuesHumidity = new Double[]{60.0, 55.0, 70.0, 65.0}; // 4 values
        WeatherModel modelHumidity = new WeatherModel(DataType.HUMIDITY, MeasurementUnit.HUMIDITY, "2023-01-01 01:00:00", "tampere", valuesHumidity);
        storage.addData(modelHumidity);

        WeatherModel dataHumidity = (WeatherModel) getData("2023-01-01 01:00:00", "2023-01-01 04:00:00", DataType.HUMIDITY);
        assertTrue(dataHumidity.getDataPoints().size() == 4);
        assertTrue(dataHumidity.getDataPoints().get("2023-01-01 01:00:00") == 60.0);


    }

    private AbstractDataModel<Double> getData(String start, String end, DataType datatype) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return DataStorage.getInstance().getData(new DataRequest(datatype, LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter), "tampere"));
    }


}
