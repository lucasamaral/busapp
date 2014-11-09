package com.urban.busapp.tests;

import android.test.InstrumentationTestCase;

import com.urban.busapp.app.models.Stop;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class StopReadTest extends InstrumentationTestCase {

    String jsonString = "[{\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"\",\n" +
            "    \"address\": \"R. H22A, 1-305 - Campus do CTA\",\n" +
            "    \"point\": 1\n" +
            "}]";

    public void testReadStops() throws JSONException {
        JSONArray jsonRet = new JSONArray(jsonString);
        ArrayList<Stop> linesArray = Stop.fromJsonArray(jsonRet);
        Stop line = linesArray.get(0);
        assertEquals("", line.getName());
        assertEquals("R. H22A, 1-305 - Campus do CTA", line.getAddress());
    }
}
