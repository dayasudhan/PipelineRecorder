package com.kuruvatech.pipelinerecorder.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class location {
    public location() {
        type = "";
        coordinates = new ArrayList<LatLng>();
        elevation = new ArrayList<GeoPoint>();
        name = "";
        phone = "";
        paid = 0;
        vendorusername="";
        size="";
        pipe_type="";
        purpose="";
        remarks="";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<LatLng> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<LatLng> coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }
    String type;
    ArrayList<LatLng> coordinates ;

    public ArrayList<GeoPoint> getElevation() {
        return elevation;
    }

    public void setElevation(ArrayList<GeoPoint> elevation) {
        this.elevation = elevation;
    }

    ArrayList<GeoPoint> elevation ;
    String name;
    String phone;
    String size;

    public String getPipe_type() {
        return pipe_type;
    }

    public void setPipe_type(String pipe_type) {
        this.pipe_type = pipe_type;
    }

    String pipe_type;
    String purpose;
    String remarks;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;
    public String getSizeofpipeline() {
        return size;
    }

    public void setSizeofpipeline(String sizeofpipeline) {
        this.size = sizeofpipeline;
    }



    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getVendorusername() {
        return vendorusername;
    }

    public void setVendorusername(String vendorusername) {
        this.vendorusername = vendorusername;
    }

    String vendorusername;
    int paid;

}
