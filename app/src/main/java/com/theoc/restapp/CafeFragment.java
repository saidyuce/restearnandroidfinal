package com.theoc.restapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.theoc.restapp.adapters.AnketCafeAdapter;
import com.theoc.restapp.dataorganization.screendata.GetDataSurwey;

public class CafeFragment extends Fragment {

    GetDataSurwey getDataSurwey;
    public CafeFragment(GetDataSurwey a) {
        super();
        getDataSurwey=a;
    }

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View row = inflater.inflate(R.layout.fragment_anket_cafe, container, false);
        listView = (ListView) row.findViewById(R.id.listView3);
        AnketCafeAdapter adapter = new AnketCafeAdapter(getContext(),getDataSurwey); // data.get_context();
        listView.setAdapter(adapter);
        return row;


    }
}
