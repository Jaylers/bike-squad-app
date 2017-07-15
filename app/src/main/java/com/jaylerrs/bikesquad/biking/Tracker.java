package com.jaylerrs.bikesquad.biking;

/**
 * Created by jaylerr on 15-Jul-17.
 */

public class Tracker {
    String time;
    String latitude;
    String longitude;

    public Tracker(String time, String latitude, String longitude) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
