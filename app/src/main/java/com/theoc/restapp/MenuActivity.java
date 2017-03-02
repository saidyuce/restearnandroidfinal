package com.theoc.restapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.theoc.restapp.dataorganization.barcode.ReadBarcode;
import com.theoc.restapp.dataorganization.screendata.GetDataMenu;
import com.theoc.restapp.helper.MenuInterface;
import com.theoc.restapp.helper.SepetDialog;
import com.theoc.restapp.helper.SlidingTabLayout;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    // fab basınca mapler boş dönüyor, null değil
    public ViewPager pager;
    public SlidingTabLayout tabs;
    public FrameLayout sefFrame;
    public RelativeLayout oneriRelative;
    public boolean orjinal = true;
    GetDataMenu getDataMenu;
    String user_point = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        user_point = intent.getStringExtra("point");
        Log.v("USERPOINT=", user_point);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SepetDialog sepetDialog = new SepetDialog(MenuActivity.this,
                        getDataMenu.get_sepetDict(),
                        getDataMenu.get_sepetDictPrice(),
                        getDataMenu.get_sepetDictPoint(),
                        getDataMenu.get_sepetDictCategory(),
                        getDataMenu,
                        user_point);
                sepetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                sepetDialog.show();
            }
        });
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        oneriRelative = (RelativeLayout) findViewById(R.id.oneriRelative);
        sefFrame = (FrameLayout) findViewById(R.id.sefFrame);
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pager.getLayoutParams();

                sefFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orjinal) {

                    oneriRelative.animate().translationY(-oneriRelative.getHeight());
                    tabs.animate().translationY(-oneriRelative.getHeight());
                    pager.animate().translationY(-oneriRelative.getHeight());
                    params.weight = 84;
                    pager.setLayoutParams(params);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        sefFrame.setElevation(1f);
                    }else {
                        sefFrame.bringToFront();
                        ViewCompat.setElevation(sefFrame, 16f);
                    }

                    orjinal = false;
                } else {

                    oneriRelative.animate().translationY(0);
                    tabs.animate().translationY(0);
                    pager.animate().translationY(0);
                    params.weight = 59;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pager.setLayoutParams(params);
                        }
                    },250);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        sefFrame.setElevation(8f);
                    }else {
                        sefFrame.bringToFront();
                        ViewCompat.setElevation(sefFrame, 16f);
                    }
                    orjinal = true;
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabs.setElevation(8f);
            sefFrame.setElevation(8f);
        } else {
            tabs.bringToFront();
            sefFrame.bringToFront();
            ViewCompat.setElevation(sefFrame, 8f);
            ViewCompat.setElevation(tabs, 8f);
        }

        start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void start() {
        getDataMenu = new GetDataMenu(this);
        getDataMenu.start_paralel(Integer.parseInt(ReadBarcode.cafe_id + ""));
    }
}
