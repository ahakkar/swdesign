package org.example.Models;

import java.util.Date;

public class PrimaryModel {

    private Date date;
    private double temperature;
    private double lat;
    private double lon;

    public PrimaryModel(Date date, double temperature, double lat, double lon) {
        this.date = date;
        this.temperature = temperature;
        this.lat = lat;
        this.lon = lon;
    }

    public Date getDate() {
        return date;
    }

    protected void setDate(Date date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    protected void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getLat() {
        return lat;
    }

    protected void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    protected void setLon(double lon) {
        this.lon = lon;
    }

}
