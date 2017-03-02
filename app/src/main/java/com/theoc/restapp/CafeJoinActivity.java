package com.theoc.restapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;
import com.theoc.restapp.dataorganization.SocketMessage;
import com.theoc.restapp.dataorganization.barcode.ReadBarcode;
import com.theoc.restapp.dataorganization.screendata.GetDataCafeJoin;

public class CafeJoinActivity extends AppCompatActivity {

    ReadBarcode readBarcode;
    public TextView mainTextView;
    GetDataCafeJoin getDataCafeJoin;
    public    ImageView ımageView;
    String cafe_id;
    public    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_join);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.textView34).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CafeJoinActivity.this, MenuActivity.class);
                intent.putExtra("point", getDataCafeJoin.get_cafe_point());
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        findViewById(R.id.textViewAnket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CafeJoinActivity.this, AnketActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // burayı QR'dan çekeceğiz.
        Toast.makeText(this, getIntent().getStringExtra("qrText"), Toast.LENGTH_LONG).show();
        readBarcode=new ReadBarcode(this);
        readBarcode.read(getIntent().getStringExtra("qrText"));
    }

    public void start(){

        //ReadBarcode.temp_point
       // ReadBarcode.temp_key_siparis
        //R
        if (ReadBarcode.durum.equals("fail")){

            Toast.makeText(this.getBaseContext(),"Lütfen tekrar giriş yapınız!!",Toast.LENGTH_LONG).show();
            this.finish();
        }
        else if (ReadBarcode.durum.equals("wrong_barcode")){
            Toast.makeText(this.getBaseContext(),"Yanlış barcode okuttunuz!!1",Toast.LENGTH_LONG).show();
            this.finish();
        }else {


            Toast.makeText(this.getBaseContext(), ReadBarcode.durum, Toast.LENGTH_LONG).show();
            LoginActivity.socket_message.oturum_Ac(ReadBarcode.durum, ReadBarcode.cafe_id + "", GeneralSync.id + "");

            cafe_id = ReadBarcode.cafe_id + "";
            getDataCafeJoin = new GetDataCafeJoin(this, Integer.parseInt(cafe_id));
            findViewById(R.id.textView35).setOnClickListener(getDataCafeJoin.onClickListener_textview);
            mainTextView = (TextView) findViewById(R.id.mainTextView);
            ımageView = (ImageView) findViewById(R.id.imageView11);
            getDataCafeJoin.start_paralel();

            Log.v("TEMPPOINT=", String.valueOf(ReadBarcode.temp_point));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.CafeJoinScreen);
    }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void start_error(){

        if(  ReadBarcode.durum.equals("wrong_barcode")){
            Toast.makeText(this,"Yanlış barcode okuttunuz!!",Toast.LENGTH_LONG).show();
            this.finish();
        }else      if(  ReadBarcode.durum.equals("fail")){
            Toast.makeText(this,"Kullanıcı ismi veya şifre yanlış!!",Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

}
