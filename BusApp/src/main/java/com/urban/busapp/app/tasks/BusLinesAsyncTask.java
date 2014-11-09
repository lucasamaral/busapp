package com.urban.busapp.app.tasks;


import android.os.AsyncTask;

import com.urban.busapp.app.SearchBusFragment;
import com.urban.busapp.app.models.BusLine;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BusLinesAsyncTask  extends AsyncTask<String, Void, ArrayList<BusLine>> {

    SearchBusFragment busFragment;

    public BusLinesAsyncTask(SearchBusFragment busFragment) {
        this.busFragment = busFragment;
    }

    @Override
    protected ArrayList<BusLine> doInBackground(String... params) {
        return fetchAllBusLines(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<BusLine> busLines) {
        busFragment.setBusLineNames(busLines);
    }

    private ArrayList<BusLine> fetchAllBusLines(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        ArrayList<BusLine> busLines = new ArrayList<BusLine>();
        String resultString;
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if(entity != null){
                InputStream inStream = entity.getContent();
                resultString = TaskUtils.convertStreamToString(inStream);
                JSONArray jsonRet = new JSONArray(resultString);
                busLines = readBusLines(jsonRet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return busLines;
    }

    public ArrayList<BusLine> readBusLines(JSONArray jsonRet) {
        ArrayList<BusLine> allLines = new ArrayList<BusLine>();
        for (int i = 0; i < jsonRet.length(); i++) {
            try {
                JSONObject obj = jsonRet.getJSONObject(i);
                BusLine line = new BusLine(obj.getString("number"),obj.getString("name"),
                        obj.getString("start_segment"),obj.getString("end_segment"));
                allLines.add(line);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allLines;
    }
}
