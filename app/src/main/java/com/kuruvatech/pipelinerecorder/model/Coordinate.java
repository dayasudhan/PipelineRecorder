package com.kuruvatech.pipelinerecorder.model;

public class Coordinate {
    public String getSouthwestlatitude() {
        return southwestlatitude;
    }

    public void setSouthwestlatitude(String southwestlatitude) {
        this.southwestlatitude = southwestlatitude;
    }

    public String getSouthwestlongitude() {
        return southwestlongitude;
    }

    public void setSouthwestlongitude(String southwestlongitude) {
        this.southwestlongitude = southwestlongitude;
    }

    public String getNortheastlatitude() {
        return northeastlatitude;
    }

    public void setNortheastlatitude(String northeastlatitude) {
        this.northeastlatitude = northeastlatitude;
    }

    public String getNortheastlongitude() {
        return northeastlongitude;
    }

    public void setNortheastlongitude(String northeastlongitude) {
        this.northeastlongitude = northeastlongitude;
    }
    public  Coordinate()
    {

    }
    private String southwestlatitude ;
    private String southwestlongitude ;
    private String northeastlatitude ;
    private String northeastlongitude;

}
