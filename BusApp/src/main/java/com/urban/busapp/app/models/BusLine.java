package com.urban.busapp.app.models;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusLine{
    private String number;
    private String name;
    private String start;
    private String end;
    private ArrayList<StopPoint> stopPoints;
    private ArrayList<SegmentTime> segmentTimes;

    public BusLine(String number, String name, String start, String end,
                   ArrayList<StopPoint> stopPoints, ArrayList<SegmentTime> segmentTimes) {
        this.number = number;
        this.name = name;
        this.start = start;
        this.end = end;
        this.stopPoints = stopPoints;
        this.segmentTimes = segmentTimes;
    }

    public static BusLine fromJson(JSONObject obj){
        try {
             return new BusLine(obj.getString("number"),obj.getString("name"),
                    obj.getString("start_segment"),obj.getString("end_segment"),
                     parsePoints(obj.getJSONArray("stop_points")),
                     parseSegments(obj.getJSONArray("segs_ordered")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<SegmentTime> parseSegments(JSONArray segments) {
        ArrayList<SegmentTime> parsedSegs = new ArrayList<SegmentTime>();
        for (int i = 0; i < segments.length(); i++) {
            try {
                JSONObject obj = segments.getJSONObject(i);
                parsedSegs.add(new SegmentTime(obj.getInt("time_estimated")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedSegs;
    }

    private static ArrayList<StopPoint> parsePoints(JSONArray stopPoints) {
        ArrayList<StopPoint> parsedPoints = new ArrayList<StopPoint>();
        for (int i = 0; i < stopPoints.length(); i++) {
            try {
                JSONObject obj = stopPoints.getJSONObject(i);
                String pointStr = obj.getString("point");
                String[] values = pointStr.split(" ");
                Double lat = Double.parseDouble(values[0].split(":")[1]);
                Double lng = Double.parseDouble(values[1].split(":")[1]);
                LatLng point = new LatLng(lat, lng);
                parsedPoints.add(new StopPoint(point, obj.getInt("seg_id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedPoints;
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

    public ArrayList<StopPoint> getStopPoints() {
        return stopPoints;
    }

    public ArrayList<SegmentTime> getSegmentTimes() {
        return segmentTimes;
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