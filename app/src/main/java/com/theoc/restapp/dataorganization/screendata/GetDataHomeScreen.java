package com.theoc.restapp.dataorganization.screendata;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.theoc.restapp.CafeActivity;
import com.theoc.restapp.MapActivity;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.adapters.HomeGridAdapter;
import com.theoc.restapp.adapters.ImageAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetDataHomeScreen extends GetDataFromLocal {

    JSONObject main_data, ana_data, filter_data;
    String[] listItems = {"Hepsini Göster",
            "Restarn",
            "Happy Hour",
            "Ürün"};


    public GetDataHomeScreen(Activity activity) {
        super(activity.getBaseContext(), activity);
    }

    public void start_paralel() {


        super.sync_finish(0);


    }

    public void start_normal() {


        get_data_from_local(0);
        data_received(0);


    }

    public void stop_paralel_task() {

        super.stop_get_data_from_local();
    }

    String q_loc="";
    @Override
    protected void get_data_from_local(int i) {
        if (GeneralSync.location_city == null)
            q_loc="";
        else{
            q_loc="and cafe.city= '"+GeneralSync.location_city.toLowerCase()+"'";
        }

        super.result_json = new JSONObject();

        if (i==0) {

            super.select_and_response_json("campaing", "SELECT  " +
                    "campaing.id AS cid," +
                    "cafe.id AS cfid," +
                    "((" + GeneralSync.x_loc + " - cafe.x_)*(" + GeneralSync.x_loc + "-cafe.x_)+(" + GeneralSync.y_loc + " - cafe.y_)*(" + GeneralSync.y_loc + " - cafe.y_)) as distance," +
                    "campaing.picture_url," +
                    "cafe.preminium_type," +
                    "campaing.detail," +
                    "cafe.name," +
                    "cafe.x_,"+
                    "cafe.y_,"+
                    "campaing.category as campaing_category,"+
                    "cafe.category as cafe_category " +
                    "FROM " + "campaing JOIN cafe on campaing.cafe_id=cafe.id where cafe.preminium_type=0 "+q_loc + " ORDER BY distance ASC");
        }else if(i==1){
            //for preminium
            super.select_and_response_json("campaing", "SELECT  " +
                    "campaing.id AS cid," +
                    "cafe.id AS cfid," +
                    "((" + GeneralSync.x_loc + " - cafe.x_)*(" + GeneralSync.x_loc + "-cafe.x_)+(" + GeneralSync.y_loc + " - cafe.y_)*(" + GeneralSync.y_loc + " - cafe.y_)) as distance," +
                    "campaing.picture_url," +
                    "cafe.preminium_type," +
                    "campaing.detail," +
                    "cafe.name," +
                    "cafe.x_,"+
                    "cafe.y_,"+
                    "campaing.category as campaing_category,"+
                    "cafe.category as cafe_category " +
                    "FROM " + "campaing JOIN cafe on campaing.cafe_id=cafe.id where cafe.preminium_type=0 "+q_loc + " ORDER BY distance ASC");
        }

    }

    @Override
    protected void data_received(int i) { //bu metodu biraz kurcaladım

        if(i==0)
            try {
                main_data = super.result_json;
                Log.v("MAIN DATA JSON", main_data.toString());
                if (main_data.getJSONArray("campaing").length() > 0)
                    set_main_gui();
                //   set_top_gui(); // top gui buraya ekledim. ayrı durumlarda güncellenmektense birlikte güncellensinler
                //set top for preminium data
                super.sync_finish(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        if(i==1){
            try {
                main_data = super.result_json;
                if (main_data.getJSONArray("campaing").length() > 0)
                    Log.v("CAMPAING JSON", main_data.getJSONArray("campaing").toString());

                    set_top_gui(); // top gui buraya ekledim. ayrı durumlarda güncellenmektense birlikte güncellensinler
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    private void set_main_gui() {


        adj_items();
    }

    private void set_top_gui(){  // bu metodu biraz kurcaladım
        ImageAdapter adapter = new ImageAdapter(a.getBaseContext(),this);
        ViewGroup header = (ViewGroup) a.getLayoutInflater().inflate(R.layout.home_single_item_premium, ((HomeActivity) a).listView, false);
        ViewPager viewPager = (ViewPager) header.findViewById(R.id.view_pager);
        CirclePageIndicator indicator = (CirclePageIndicator) header.findViewById(R.id.titles);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        ((HomeActivity) a).listView.addHeaderView(header);
        HomeGridAdapter adapter_campaing = new HomeGridAdapter(a, this);
        ((HomeActivity) a).listView.setAdapter(adapter_campaing);
        ana_data = main_data;

        //ViewPager   viewPager = (ViewPager)a. findViewById(R.id.view_pager);
        //CirclePageIndicator titleIndicator = (CirclePageIndicator)a.findViewById(R.id.titles);
        //titleIndicator.setViewPager(viewPager);
    }

    public String get_cafe_distance(int json_indis) {

        try {
            Double distance = main_data.getJSONArray("campaing").getJSONObject(json_indis).getDouble("distance");
            return distance.intValue() + " km";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double get_cafe_x(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getDouble("x_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double get_cafe_y(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getDouble("y_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String get_campaing_picture_url(int json_indis) {

        try {
            return ServConnection.get_file_url()+main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("picture_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_campaing_category(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("campaing_category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_cafe_category(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("cafe_category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_campaing_detail(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("detail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafe_name(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafe_id(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getInt("cfid") + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int get_size() {
        try {
            return main_data.getJSONArray("campaing").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    Map<String,String> search_items;
    JSONArray jsonarray;

    private void adj_items(){
        search_items=new HashMap<>();
        try {
            jsonarray= main_data.getJSONArray("campaing");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0;i<jsonarray.length();++i){
            search_items.put(get_cafe_name(i),get_cafe_id(i));

        }
        set_suggestion_adapter_array();
    }

    CursorAdapter cursorAdapter;
    MatrixCursor matrixCursor;

    public void set_suggestion_adapter_array(){
        String[] from = new String[] {"cafe_name"};
        int[] to = new int[] {R.id.suggestionTextView};
        cursorAdapter = new SimpleCursorAdapter(a,
                R.layout.suggestions_single_item,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ((HomeActivity) a).actionView2.setSuggestionsAdapter(cursorAdapter);

    }

    public void search(String key){

        int id;
        double cafe_x = 0.0;
        double cafe_y = 0.0;
        String name = "";
        id=Integer.parseInt(search_items.get(key));
        try {
            JSONArray campaingArray = ana_data.getJSONArray("campaing");
            for (int i = 0; i < campaingArray.length(); i++) {
                if (campaingArray.getJSONObject(i).getInt("cid") == id) {
                    cafe_x = campaingArray.getJSONObject(i).getDouble("x_");
                    cafe_y = campaingArray.getJSONObject(i).getDouble("y_");
                    name = campaingArray.getJSONObject(i).getString("name");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("asfsaf", cafe_x + " " + cafe_y + " " + name + " " + id);
        Intent intent = new Intent(a, CafeActivity.class);
        intent.putExtra("cafe_x", cafe_x);
        intent.putExtra("cafe_y", cafe_y);
        intent.putExtra("name", name);
        intent.putExtra("cafe_id", id + "");
        a.startActivity(intent);
    }


    public void populate(String text){

        matrixCursor = new MatrixCursor(new String[]{ BaseColumns._ID, "cafe_name" });
        for (int i=0;i<jsonarray.length();++i){
            search_items.put(get_cafe_name(i),i+"");
            if (get_cafe_name(i).toLowerCase().startsWith(text.toLowerCase()))
                matrixCursor.addRow(new Object[] {i, get_cafe_name(i)});
        }
        cursorAdapter.changeCursor(matrixCursor);
        cursorAdapter.notifyDataSetChanged();

    }

    public void navigate(int pos){
        String suggestion = getSuggestion(pos);
        search(suggestion);
    }


    public String getSuggestion(int pos){
        Cursor cursor = (Cursor) ((HomeActivity) a).actionView2.getSuggestionsAdapter().getItem(
                pos);
        String suggest1 = cursor.getString(cursor
                .getColumnIndex("cafe_name"));
        return suggest1;
    }

    // *****************************************************************************************

    AlertDialog.Builder builder;

    public void createFilterDialog(){
        filterAdapter adapter = new filterAdapter(a, R.layout.home_filter_single_item, listItems);
        builder = new AlertDialog.Builder(a);
        builder .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                filter(which);
            }
        })
                .create()
                .show();

    }

    public void filter(int pos){
        try {
            if (pos == 0) {
                main_data = new JSONObject(ana_data.toString());
                HomeGridAdapter adapter_campaing = new HomeGridAdapter(a, this);
                ((HomeActivity) a).listView.setAdapter(adapter_campaing);
            } else {
                String choice = listItems[pos];
                int size = ana_data.getJSONArray("campaing").length();
                JSONArray jsonList = new JSONArray();
                boolean flag;
                for (int i = 0; i < size; i++){
                    flag = choice.equalsIgnoreCase(ana_data.getJSONArray("campaing").getJSONObject(i).getString("campaing_category"));
                    if (flag) {
                        jsonList.put(ana_data.getJSONArray("campaing").getJSONObject(i));
                    }
                }

                filter_data = new JSONObject();
                filter_data.put("campaing", jsonList);
                main_data = new JSONObject(filter_data.toString());
                HomeGridAdapter adapter_campaing = new HomeGridAdapter(a, this);
                ((HomeActivity) a).listView.setAdapter(adapter_campaing);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

       /* ((MapActivity)a).mMap.clear();
        if (pos == 0) {
            indis = 0;
            add_marker();
        } else {
            String choice = listItems[pos];
            try {
                for (int i = 0; i < main_data.getJSONArray("cafe").length(); i++) {
                    if (choice.equalsIgnoreCase(get_category(i))) {
                        add_marker_filter(i);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }


    private class filterAdapter extends ArrayAdapter<Object> {

        String[] listItems;
        Context context;
        int layoutID;

        public filterAdapter(Context context, int res, String[] listItems) {
            super(context, res);
            this.context = context;
            this.layoutID = res;
            this.listItems = listItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;
            row = inflater.inflate(layoutID, parent, false);
            TextView filterTextView = (TextView) row.findViewById(R.id.filterTextView);
            ImageView filterImageView = (ImageView) row.findViewById(R.id.filterImageView);
            // buraya res/drawable'dan koyacağız kategori ikonlarını

            filterTextView.setText(listItems[position]);
            return row;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return listItems.length;
        }
    }


}