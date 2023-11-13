package fi.nordicwatt.model.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import fi.nordicwatt.model.datamodel.AbstractDataModel;
import fi.nordicwatt.model.data.DataRequest;
import fi.nordicwatt.model.datamodel.EnergyModel;
import fi.nordicwatt.model.datamodel.WeatherModel;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.utils.Logger;

/**
 * @author ???
 * Stores data models and provides methods for accessing them
 * Singleton class
 *
 */
public final class DataStorage {

    private static DataStorage instance;
    private final ArrayList<AbstractDataModel<Double>> dataModels;

    private DataStorage() {
        dataModels = new ArrayList<>();
    }

    protected static DataStorage getInstance() {
        synchronized (DataStorage.class){
            if (instance == null) {

                instance = new DataStorage();
            }
            return instance;
        }
    }

    protected void addData(AbstractDataModel<Double> dataModel) {

        AbstractDataModel<Double> existingModel = getModelByType(dataModel.getDataType());

        if (existingModel == null){
            dataModels.add(dataModel);
        }
        else {
            try {
                existingModel.merge(dataModel);
            }
            catch (IllegalArgumentException e) {
                System.out.println("DATASTORAGE: MERGE FAILED");
                e.printStackTrace();
            }

        }

    }

    private AbstractDataModel<Double> getModelByType(DataType dataType){
        for (AbstractDataModel<Double> model : dataModels) {
            if (model.getDataType().equals(dataType)) {
                return model;
            }
        }
        return null;
    }

    protected AbstractDataModel<Double> getData(DataRequest query) {

        AbstractDataModel<Double> model = getModelByType(query.getDataType());

        if (model != null){

            LocalDateTime start = query.getStarttime();
            LocalDateTime end = query.getEndtime().withMinute(0).withSecond(0);

            Map<LocalDateTime, Double> dataPoints = model.getDataPointsWithRange(start, end);
            Double[] values = new Double[dataPoints.size()];

            int i = 0;
            for (Double val : dataPoints.values()){
                values[i] = val;
                ++i;
            }

            if (dataPoints.containsKey(start) && dataPoints.containsKey(end)){
                if (model instanceof EnergyModel){
                    return new EnergyModel(model.getDataType(), model.getUnit(), start, values);
                }
                else if (model instanceof WeatherModel){
                    return new WeatherModel(model.getDataType(), model.getUnit(), start, ((WeatherModel) model).getLocation(), values);
                }
            }
            else {
                return null;
            }
        }

        return null;
    }



}
