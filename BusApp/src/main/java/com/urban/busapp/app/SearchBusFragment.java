package com.urban.busapp.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.urban.busapp.app.models.BusLine;
import com.urban.busapp.app.tasks.BusLinesAsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchBusFragment extends Fragment {

    protected ArrayList<String> buses = new ArrayList<String>();
    protected  ArrayList<BusLine> allLines;
    private ArrayAdapter<String> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bus, container, false);
        adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1);
        ListView busList = (ListView) rootView.findViewById(R.id.listViewBus);
        busList.setAdapter(adapter);
        BusLinesAsyncTask task = new BusLinesAsyncTask(this);
        task.execute("https://bus-estimates.herokuapp.com/busapp/buslines/");
        return rootView;
    }

    public void setBusLineNames(ArrayList<BusLine> lines){
        allLines = lines;
        for(BusLine line : allLines){
            buses.add(line.lineInfo());
            adapter.insert(line.lineInfo(),adapter.getCount());
        }
        adapter.notifyDataSetChanged();
    }

}
