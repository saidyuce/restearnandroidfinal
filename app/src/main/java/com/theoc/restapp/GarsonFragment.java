package com.theoc.restapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.theoc.restapp.adapters.AnketCafeAdapter;
import com.theoc.restapp.adapters.AnketGarsonAdapter;
import com.theoc.restapp.dataorganization.screendata.GetDataSurwey;
import com.theoc.restapp.helper.AnketGarsonDialog;

public class GarsonFragment extends Fragment {

    GetDataSurwey getDataSurwey;
    public GarsonFragment(GetDataSurwey getDataSurwey) {
        super();
        this.getDataSurwey=getDataSurwey;
    }
    GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View row = inflater.inflate(R.layout.fragment_anket_garson, container, false);
        gridView = (GridView) row.findViewById(R.id.gridView);
        AnketGarsonAdapter adapter = new AnketGarsonAdapter(getContext(),getDataSurwey); // data.get_context();
        gridView.setAdapter(adapter);
        return row;
    }
}