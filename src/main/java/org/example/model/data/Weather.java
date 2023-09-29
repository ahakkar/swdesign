package org.example.model.data;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeMap;

import org.example.utils.WeatherDataException;

/**
 * Weather
 * Class for handling and storing weather data.
 * @author Markus Hissa
 */
public class Weather 
{
    private final String town;
    // ArrayList contains weather information in the following manner:
    // [0] = Temperature, [1] = Wind, [2] = Rain
    private TreeMap<LocalDateTime,ArrayList<Double>> weatherData;

    public Weather(String town)
    {
        this.town = town;
        this.weatherData = new TreeMap<>();
    }

    private void setValue(int valueType, Double value, LocalDateTime time)
    {
        try
        {
            ArrayList<Double> dataValues;
            if ( weatherData.containsKey(time) == true )
            {
                dataValues = weatherData.get(time);
                dataValues.remove(valueType);
                dataValues.add(valueType, value);
            }
            else
            {
                dataValues = new ArrayList<>();
                dataValues.add(valueType, value);
                weatherData.put(time,dataValues);
            }
        }
        catch( InvalidParameterException e )
        {
            System.out.print("Faulty data/no data at:" + time.toString());
        }

    }

    public void setTemperature(LocalDateTime time, Double temperature)
    {
        this.setValue(0, temperature, time);
    }

    public void setWind(LocalDateTime time, Double wind)
    {
        this.setValue(0, wind, time);
    }

    public void setRain(LocalDateTime time, Double rain)
    {
        this.setValue(0, rain, time);
    }

    public String getTown()
    {
        return town;
    }

    public Double getTemperature(LocalDateTime time) 
        throws WeatherDataException
    {
        if ( weatherData.containsKey(time) == true )
        {
            return weatherData.get(time).get(0);
        }
        throw new WeatherDataException("No temperature data found at" + time.toString());
    }

    public Double getWind(LocalDateTime time) 
        throws WeatherDataException
    {
        if ( weatherData.containsKey(time) == true )
        {
            return weatherData.get(time).get(1);
        }
        throw new WeatherDataException("No wind data found at" + time.toString());
    }

    public Double getRain(LocalDateTime time) 
        throws WeatherDataException
    {
        if ( weatherData.containsKey(time) == true )
        {
            return weatherData.get(time).get(2);
        }
        throw new WeatherDataException("No rain data found at" + time.toString());
    }

}
