package org.example.model.services;

import org.example.model.data.AbstractDataModel;
import org.example.model.data.DataRequest;
import org.example.model.data.EnergyModel;
import org.example.model.data.WeatherModel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author ???
 */
public class DataStorage {

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
        // TODO IMPLEMENT THIS
    }

    protected AbstractDataModel<Double> getData(DataRequest query) {

        AbstractDataModel<Double> model = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (AbstractDataModel<Double> dataModel : dataModels) {
            if (dataModel.getDataType().equals(query.getDataType())) {
                model = dataModel;
                break;
            }
        }

        if (model != null){

            String start = query.getStarttime().format(formatter);
            String end = query.getEndtime().format(formatter);

            Map<String, Double> dataPoints = model.getDataPointsWithRange(start, end);
            Double[] values = new Double[dataPoints.size()];

            int i = 0;
            for (Double val : dataPoints.values()){
                values[i] = val;
                ++i;
            }

            if (dataPoints.containsKey(start) && dataPoints.containsKey(end)){
                if (model instanceof EnergyModel){
                    return new EnergyModel(model.getDataType(), model.getUnit(), start, model.getInterval(), values);
                }
                else if (model instanceof WeatherModel){
                    return new WeatherModel(model.getDataType(), model.getUnit(), start, model.getInterval(), ((WeatherModel) model).getLocation(), values);
                }
            }
        }

        return null;
    }



}
