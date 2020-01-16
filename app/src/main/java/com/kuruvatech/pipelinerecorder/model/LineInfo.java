package com.kuruvatech.pipelinerecorder.model;

public class LineInfo {

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
    public location getLoc() {
        return loc;
    }

    public void setLoc(location loc) {
        this.loc = loc;
    }

    location loc;
//    String name;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    String phone;
    public LineInfo() {
        loc = new location();
//        name ="";
//        phone ="";
    }
}
