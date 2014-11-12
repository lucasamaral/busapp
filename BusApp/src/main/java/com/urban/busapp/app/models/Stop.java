package com.urban.busapp.app.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Stop {

    private String address;
    private String name;

    public Stop(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public static Stop fromJson(JSONObject obj){
        try {
            return new Stop(obj.getString("address"),obj.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Stop> fromJsonArray(JSONArray jsonRet) {
        ArrayList<Stop> allLines = new ArrayList<Stop>();
        for (int i = 0; i < jsonRet.length(); i++) {
            try {
                JSONObject obj = jsonRet.getJSONObject(i);
                Stop line = fromJson(obj);
                allLines.add(line);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allLines;
    }

    public String stopInfo(){
        String address;
        String name;
        if(getAddress().length() > 10)
            address = getAddress().substring(0, 15) + "...";
        else
            address = getAddress();
        if(getName().length() > 10)
            name = getName().substring(0, 15) + "...";
        else
            name = getName();
        return name + " - " + address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
