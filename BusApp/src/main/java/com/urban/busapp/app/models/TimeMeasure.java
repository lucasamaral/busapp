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
        if(time != null){
            try {
                object.put("time_value", time.getTimeValue());
                object.put("line_segment", time.getLineSegment());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static JSONArray toJsonArray(ArrayList<TimeMeasure> times){
        JSONArray array = new JSONArray();
        if(times != null && times.size()>0){
            for(TimeMeasure time : times){
                array.put(toJson(time));
            }
        }
        return array;
    }
}
