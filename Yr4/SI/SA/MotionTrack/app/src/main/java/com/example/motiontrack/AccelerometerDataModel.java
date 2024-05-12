package com.example.motiontrack;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccelerometerDataModel {
    Float valueX;
    Float valueY;
    Float valueZ;
    Integer accuracy;
    long timestamp;
    String date;


    public AccelerometerDataModel(Float valueX, Float valueY, Float valueZ, Integer accuracy, long timestamp) {
        this.valueX = valueX;
        this.valueY = valueY;
        this.valueZ = valueZ;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        this.date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(timestamp));
    }

    public Float getValueX() {
        return valueX;
    }

    public Float getValueY() {
        return valueY;
    }

    public Float getValueZ() {
        return valueZ;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        return date;
    }
}
