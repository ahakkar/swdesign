package org.example.model.data;

import java.time.LocalDateTime;

/**
 * Weather
 * Class for handling and storing weather data.
 * @author Markus Hissa
 */
public class Weather 
{
    private final String town;
    private final LocalDateTime time;
    private Double temperature;
    private Double wind;
    private Double rain;

    public Weather(String town, LocalDateTime time)
    {
        this.town = town;
        this.time = time;
    }

    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }

    public void setWind(Double wind)
    {
        this.wind = wind;
    }

    public void setRain(Double rain)
    {
        this.rain = rain;
    }

    public String getTown()
    {
        return town;
    }

    public LocalDateTime getTime()
    {
        return time;
    }

    public Double getTemperature()
    {
        return temperature;
    }

    public Double getWind()
    {
        return wind;
    }

    public Double getRain()
    {
        return rain;
    }

}
