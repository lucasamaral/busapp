package com.urban.busapp.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TrackUserFragment extends Fragment {

    protected String[] buses = {"8102", "82", "8150"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bus, container, false);
        ListAdapter adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1, buses);
        ListView busList = (ListView) rootView.findViewById(R.id.listViewBus);
        busList.setAdapter(adapter);
        return rootView;
    }
}
