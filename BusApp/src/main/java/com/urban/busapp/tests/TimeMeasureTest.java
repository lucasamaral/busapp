package com.urban.busapp.tests;


import android.test.InstrumentationTestCase;

import com.urban.busapp.app.models.TimeMeasure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

public class TimeMeasureTest extends InstrumentationTestCase {
    String jsonExpected = "{\"time_value\":100,\"line_segment\":2}";
    String jsonArrayExpected = "[{\"time_value\":145,\"line_segment\":2},{\"time_value\":233,\"line_segment\":3}]";

    public void testJsonObject() throws JSONException {
        TimeMeasure time = new TimeMeasure(2l, 100);
        JSONObject timeJson = TimeMeasure.toJson(time);
        JSONObject expected = new JSONObject(jsonExpected);
        assertEquals(expected.getInt("time_value"), timeJson.getInt("time_value"));
        assertEquals(expected.getInt("line_segment"), timeJson.getInt("line_segment"));
    }

    public void testJsonArray() throws JSONException {
        TimeMeasure time1 = new TimeMeasure(2l, 145);
        TimeMeasure time2 = new TimeMeasure(3l, 233);
        ArrayList<TimeMeasure> arrayList = new ArrayList<TimeMeasure>();
        arrayList.add(time1);
        arrayList.add(time2);
        JSONArray timeJson = TimeMeasure.toJsonArray(arrayList);
        JSONArray array = new JSONArray(jsonArrayExpected);
        assertEquals(array.getJSONObject(0).getInt("time_value"),
                timeJson.getJSONObject(0).getInt("time_value"));
        assertEquals(array.getJSONObject(0).getInt("line_segment"),
                timeJson.getJSONObject(0).getInt("line_segment"));
        assertEquals(array.length(), timeJson.length());
    }


}
