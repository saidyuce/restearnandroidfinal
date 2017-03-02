package com.theoc.restapp;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.theoc.restapp.dataorganization.screendata.GetDataPointsDetail;

public class MyPointsDetailActivity extends AppCompatActivity {

    String cafe_id;
    public ListView listView;
    int point=0;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_points_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        if (intent.getStringExtra("point")!=null&&!intent.getStringExtra("point").equals("")) {
            point = Integer.parseInt(intent.getStringExtra("point")) % 360;
        } else {
            point = 0;
        }
        counter = Integer.parseInt(intent.getStringExtra("point")) / 360;
        cafe_id = intent.getStringExtra("cafe_id");
        String cafe_name = intent.getStringExtra("cafe_name");
        toolbar.setTitle(cafe_name);
        setSupportActionBar(toolbar);
        TextView dereceTV = (TextView) findViewById(R.id.dereceTextView);
        dereceTV.setText(String.valueOf(point));
        listView = (ListView) findViewById(R.id.listView2);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, point); // desired degree
        animation.setDuration (1500); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.start ();
            }
        }, 500);
        start();
    }

    public void start(){
        GetDataPointsDetail getDataPointsDetail=new GetDataPointsDetail(this, cafe_id);
        getDataPointsDetail.start_paralel();
    }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
