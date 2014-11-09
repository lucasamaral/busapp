package com.urban.busapp.app.tasks;


import android.os.AsyncTask;

import com.urban.busapp.app.SearchStopFragment;
import com.urban.busapp.app.models.Stop;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StopsAsyncTask  extends AsyncTask<String, Void, ArrayList<Stop>> {

    private SearchStopFragment stopFragment;

    public StopsAsyncTask(SearchStopFragment stopFragment) {
        this.stopFragment = stopFragment;
    }

    @Override
    protected ArrayList<Stop> doInBackground(String... params) {
        return fetchAllStops(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Stop> stops) {
        stopFragment.setStopNames(stops);
    }

    private ArrayList<Stop> fetchAllStops(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        ArrayList<Stop> stops = new ArrayList<Stop>();
        String resultString;
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if(entity != null){
                InputStream inStream = entity.getContent();
                resultString = TaskUtils.convertStreamToString(inStream);
                JSONArray jsonRet = new JSONArray(resultString);
                stops = Stop.fromJsonArray(jsonRet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stops;
    }
}
