package com.kuruvatech.pipelinerecorder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dayas on 21-08-2019.
 */

public class PipelineObject {
    private String name;
    private String village;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//

    //    private String capital;
//    private long population;
    //private List<String> regions;

    public List<HashMap<String, String>> getLine() {
        return line;
    }

    public void setLine(List<HashMap<String, String>> line) {
        this.line = line;
    }

    List<HashMap<String, String>> line ;//= new ArrayList<HashMap<String,String>>();

    public PipelineObject() {
        line = new ArrayList<HashMap<String, String>>();
//        HashMap<String,String> l1=  new HashMap<>();
//        HashMap<String,String> obj2=  new HashMap<>();
//        HashMap<String,String> obj3=  new HashMap<>();
//        HashMap<String,String> l4=  new HashMap<>();
//        l1.put("Lat", "Alan");
//        l1.put("logt", "Mathison");
//        obj2.put("Lat", Double.toString(14.9808098));
//        obj2.put("logt", Double.toString(74.980980980));
//        obj3.put("Lat", Double.toString(14.98080));
//        obj3.put("logt", Double.toString(74.9809809));
//        l4.put("Lat", Double.toString(14.98098));
//        l4.put("logt", Double.toString(74.9809880));
//        line.add(l1);
//        line.add(obj2);
//        line.add(obj3);
//        line.add(l4);
    }

    public PipelineObject(String name, String state, String country, boolean capital,
                          long population, List<String> regions) {
        line = new ArrayList<HashMap<String, String>>();
//        HashMap<String,String> l1=  new HashMap<>();
//        HashMap<String,String> obj2=  new HashMap<>();
//        HashMap<String,String> obj3=  new HashMap<>();
//        HashMap<String,String> l4=  new HashMap<>();
//        l1.put("Lat", "Alan");
//        l1.put("logt", "Mathison");
//        obj2.put("Lat", Double.toString(14.9808098));
//        obj2.put("logt", Double.toString(74.980980980));
//        obj3.put("Lat", Double.toString(14.98080));
//        obj3.put("logt", Double.toString(74.9809809));
//        l4.put("Lat", Double.toString(14.98098));
//        l4.put("logt", Double.toString(74.9809880));
////        line.add(l1);
////        line.add(obj2);
////        line.add(obj3);
////        line.add(l4);
//      //  line.add(l1);
//        // ...
    }
}
