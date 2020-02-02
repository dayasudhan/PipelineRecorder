package com.kuruvatech.pipelinerecorder.model;
import com.google.android.gms.maps.model.LatLng;

public class GeoPoint {
    LatLng latlng;

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    double elevation;
    double resolution;

    public GeoPoint(LatLng ll, double ele, double res) {
        latlng = ll;
        elevation = ele;
        resolution = res;
    }

}
