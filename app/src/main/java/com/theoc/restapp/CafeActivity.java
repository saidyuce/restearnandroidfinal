package com.theoc.restapp;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;

public class CafeActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    String cafe_id;
    String name;
    double cafe_x;
    double cafe_y;
    String face, twitter, instagram, web, tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        cafe_id = intent.getStringExtra("cafe_id");
        cafe_x = intent.getDoubleExtra("cafe_x", 0.0);
        cafe_y = intent.getDoubleExtra("cafe_y", 0.0);
        face = intent.getStringExtra("face");
        twitter = intent.getStringExtra("twitter");
        instagram = intent.getStringExtra("instagram");
        web = intent.getStringExtra("web");
        tel = intent.getStringExtra("tel");

        findViewById(R.id.fbImageButton).setOnClickListener(this);
        findViewById(R.id.twitterImageButton).setOnClickListener(this);
        findViewById(R.id.instaImageButton).setOnClickListener(this);
        findViewById(R.id.phoneImageButton).setOnClickListener(this);
        findViewById(R.id.webImageButton).setOnClickListener(this);

        TextView cafeTextView = (TextView) findViewById(R.id.cafeTextView);
        cafeTextView.setText(intent.getStringExtra("cafe_detail"));

        Glide.with(this)
                .load(intent.getStringExtra("large_image"))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .crossFade()
                .into((ImageView) findViewById(R.id.largeImageView));

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.CafeDetailScreen);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + cafe_x + "," + cafe_y));
                startActivity(intent);
            }
        });
        LatLng location = new LatLng(cafe_x, cafe_y);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 18);
        googleMap.addMarker(new MarkerOptions().position(location).title("Yol Tarifi Al"));
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fbImageButton:
                if (!face.equals("") && !face.equals("yok")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(face));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(this, "Facebook bilgisi bulunmuyor", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.twitterImageButton:
                if (!twitter.equals("") && !twitter.equals("yok")) {
                    Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                    startActivity(browserIntent2);
                } else {
                    Toast.makeText(this, "Twitter bilgisi bulunmuyor", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.instaImageButton:
                if (!instagram.equals("") && !instagram.equals("yok")) {
                    Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(instagram));
                    startActivity(browserIntent3);
                } else {
                    Toast.makeText(this, "Instagram bilgisi bulunmuyor", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.webImageButton:
                if (!web.equals("") && !web.equals("yok")) {
                    Intent browserIntent4 = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                    startActivity(browserIntent4);
                } else {
                    Toast.makeText(this, "Website bilgisi bulunmuyor", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.phoneImageButton:
                if (!tel.equals("") && !tel.equals("yok")) {
                    ActivityCompat.requestPermissions(CafeActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                } else {
                    Toast.makeText(this, "Telefon numarasi bulunmuyor", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + tel));
                    if (ActivityCompat.checkSelfPermission(CafeActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(CafeActivity.this, "Arama izni vermediniz. Bir mekani arayabilmek icin arama izni vermeniz gerekiyor", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
