package org.example.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds one datapoint, compatible with Jackson so the json can be more
 * easily processed into this type.
 * 
 * @author Antti Hakkarainen
 */
public class DataPoint {

    @JsonProperty("value")
    private Double value;

    @JsonProperty("start_time")
    private String start_time;

    @JsonProperty("end_time")
    private String end_time;

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public String getStartTime() {
        return start_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
    }

    public String getEndTime() {
        return end_time;
    }
}
