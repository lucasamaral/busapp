package com.urban.busapp.tests;

import android.test.InstrumentationTestCase;

import com.urban.busapp.app.models.BusLine;
import com.urban.busapp.app.tasks.BusLinesAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class BusLineReadTest extends InstrumentationTestCase {

    String jsonString = "[{\n" +
            "    \"name\": \"Centro-CTA\",\n" +
            "    \"number\": \"2324\",\n" +
            "    \"segments\": [1],\n" +
            "    \"start_segment\": \"R. H22A, 1-305\",\n" +
            "    \"end_segment\": \"R. H22A, 1-305\"\n" +
            "}]";

    public void testReadBusLines() throws JSONException {
        JSONArray jsonRet = new JSONArray(jsonString);
        ArrayList<BusLine> linesArray = BusLine.fromJsonArray(jsonRet);
        BusLine line = linesArray.get(0);
        assertEquals("Centro-CTA", line.getName());
        assertEquals("2324", line.getNumber());
        assertEquals("R. H22A, 1-305", line.getStart());
        assertEquals("R. H22A, 1-305", line.getEnd());
    }
}
