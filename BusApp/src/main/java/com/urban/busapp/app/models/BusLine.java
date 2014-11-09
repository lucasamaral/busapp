package com.urban.busapp.app.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusLine{
    private String number;
    private String name;
    private String start;
    private String end;


    public BusLine(String number, String name, String start, String end) {
        this.number = number;
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public static BusLine fromJson(JSONObject obj){
        try {
             return new BusLine(obj.getString("number"),obj.getString("name"),
                    obj.getString("start_segment"),obj.getString("end_segment"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<BusLine> fromJsonArray(JSONArray jsonRet) {
        ArrayList<BusLine> allLines = new ArrayList<BusLine>();
        for (int i = 0; i < jsonRet.length(); i++) {
            try {
                JSONObject obj = jsonRet.getJSONObject(i);
                BusLine line = fromJson(obj);
                allLines.add(line);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allLines;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String lineInfo(){
        return getNumber() + " - " + getName();
    }

    @Override
    public String toString() {
        return "BusLine{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}