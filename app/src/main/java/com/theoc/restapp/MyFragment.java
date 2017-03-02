package com.theoc.restapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.theoc.restapp.adapters.MenuAdapter;
import com.theoc.restapp.dataorganization.screendata.GetDataMenu;
import com.theoc.restapp.helper.MenuInterface;

public class MyFragment extends Fragment {

    GetDataMenu getDataMenu;
    ListView listView;
    int posi;

    public MyFragment  (int po, GetDataMenu getDataMenu){

        super();
        posi=po;
        this.getDataMenu=getDataMenu;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View row = inflater.inflate(R.layout.fragment_menu, container, false);
        // Buraya ListView ve adapter koyup her sayfa için menü bilgisi listelenecek.
        listView = (ListView) row.findViewById(R.id.listViewMenu);
        MenuAdapter menuAdapter=new MenuAdapter(getDataMenu.get_context(),getDataMenu,posi, getDataMenu);
        listView.setAdapter(menuAdapter);
        return row;


    }
}