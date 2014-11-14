package com.urban.busapp.app.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimeMeasure {
    private Long lineSegment;
    private Integer timeValue;

    public TimeMeasure(Long lineSegment, Integer timeValue) {
        this.lineSegment = lineSegment;
        this.timeValue = timeValue;
    }

    public Long getLineSegment() {
        return lineSegment;
    }

    public Integer getTimeValue() {
        return timeValue;
    }

    public static JSONObject toJson(TimeMeasure time){
        JSONObject object = new JSONObject();
        try {
            object.put("line_segment", time.getLineSegment());
            object.put("time_value", time.getTimeValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONArray toJsonArray(ArrayList<TimeMeasure> times){
        JSONArray array = new JSONArray();
        for(TimeMeasure time : times){
            array.put(toJson(time));
        }
        return array;
    }
}
