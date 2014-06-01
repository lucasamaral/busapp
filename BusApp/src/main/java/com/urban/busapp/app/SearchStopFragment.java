package com.urban.busapp.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SearchStopFragment extends Fragment {

    protected String[] stops = {"Av. Cristiano Machado 2000", "Praça da Estação", "Av. Afonso Pena 1409"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stop, container, false);
        ListAdapter adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1, stops);
        ListView stopList = (ListView) rootView.findViewById(R.id.listViewStop);
        stopList.setAdapter(adapter);
        return rootView;
    }
}
