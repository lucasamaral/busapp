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
            "    \"segments\": [1, 2],\n" +
            "    \"start_segment\": \"R. H22A, 1-305\",\n" +
            "    \"end_segment\": \"R. H22A, 1-305\",\n" +
            "    \"stop_points\": [{\n" +
            "        \"point\": \"Lat:-023.205920 Lon:-045.882316\",\n" +
            "        \"seg_id\": 1\n" +
            "    }, {\n" +
            "        \"point\": \"Lat:-023.208130 Lon:-045.878536\",\n" +
            "        \"seg_id\": 2\n" +
            "    }]\n" +
            "}]";

    public void testReadBusLines() throws JSONException {
        JSONArray jsonRet = new JSONArray(jsonString);
        ArrayList<BusLine> linesArray = BusLine.fromJsonArray(jsonRet);
        BusLine line = linesArray.get(0);
        assertEquals("Centro-CTA", line.getName());
        assertEquals("2324", line.getNumber());
        assertEquals("R. H22A, 1-305", line.getStart());
        assertEquals("R. H22A, 1-305", line.getEnd());
        assertEquals(-023.205920, line.getStopPoints().get(0).getPoint().latitude);
        assertEquals(-045.882316, line.getStopPoints().get(0).getPoint().longitude);
        assertEquals("1", line.getStopPoints().get(0).getSegId().toString());
        assertTrue(line.getStopPoints().size() == 2);
    }
}
