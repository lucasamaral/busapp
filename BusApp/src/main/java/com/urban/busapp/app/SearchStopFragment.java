package com.urban.busapp.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.urban.busapp.app.models.Stop;
import com.urban.busapp.app.tasks.StopsAsyncTask;

import java.util.ArrayList;

public class SearchStopFragment extends Fragment {

//    protected ArrayList<String> stops = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ArrayList<Stop> allStops;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stop, container, false);
        adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1);
        ListView stopList = (ListView) rootView.findViewById(R.id.listViewStop);
        stopList.setAdapter(adapter);
        StopsAsyncTask task = new StopsAsyncTask(this);
        task.execute("https://bus-estimates.herokuapp.com/busapp/stops/");
        return rootView;
    }


    public void setStopNames(ArrayList<Stop> stops){
        allStops = stops;
        for(Stop stop : allStops){
            adapter.insert(stop.stopInfo(),adapter.getCount());
        }
        adapter.notifyDataSetChanged();
    }
}
