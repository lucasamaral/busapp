package com.urban.busapp.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.urban.busapp.app.models.BusLine;
import com.urban.busapp.app.tasks.BusLinesAsyncTask;

import java.util.ArrayList;

public class SearchBusFragment extends Fragment {

    protected  ArrayList<BusLine> allLines;
    private ArrayAdapter<String> adapter;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bus, container, false);
        adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1);
        ListView busList = (ListView) rootView.findViewById(R.id.listViewBus);
        busList.setAdapter(adapter);
        busList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), TrackUserActivity.class);
                intent.putParcelableArrayListExtra("points", allLines.get(position).getStopPoints());
                startActivity(intent);

            }
        });
        BusLinesAsyncTask task = new BusLinesAsyncTask(this);
        task.execute("https://bus-estimates.herokuapp.com/busapp/buslines/");
        return rootView;
    }

    public void setBusLineNames(ArrayList<BusLine> lines){
        allLines = lines;
        for(BusLine line : allLines){
            adapter.insert(line.lineInfo(),adapter.getCount());
        }
        adapter.notifyDataSetChanged();
    }

}
