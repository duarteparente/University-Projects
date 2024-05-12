package com.example.falldetective;

public class FallRecordModel {

    String date;
    boolean positive;
    double latitude;
    double longitude;

    private FallRecordModel() {}

    public FallRecordModel(String date, boolean positive, double latitude, double longitude) {
        this.date = date;
        this.positive = positive;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public boolean isPositive() {
        return positive;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
