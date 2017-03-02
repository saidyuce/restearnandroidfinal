package com.theoc.restapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.theoc.restapp.dataorganization.Kayit;
import com.theoc.restapp.dataorganization.KayitType;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.registerButton).setOnClickListener(this);
        kayit=new Kayit(this);
    }



Kayit kayit;





public void kaydol(String username,String password,String device_id,String isim,String soyisim){
    kayit.kayit_ol(username,password,device_id,isim,soyisim);
}



 public void kayit_bitti(KayitType type){
if (type==KayitType.yok){

    // id ve temp key dolu gelcek

}else if (type==KayitType.serversorunu){
    Toast.makeText(this, "Sunucu hatasi olustu. Lutfen tekrar deneyin", Toast.LENGTH_LONG).show();
    //
}
     else if (type==KayitType.var){
    Toast.makeText(this, "Bu bilgilerle kayitli bir kullanici bulunmakta", Toast.LENGTH_LONG).show();
    //daha önce kaydolmuş
}
     else if (type==KayitType.fail){

    //sanırım fail dönmüyo kayıtta ama yinede koydum
}



 }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.registerButton:
                kayit.kayit_ol(((EditText) findViewById(R.id.emailEditText)).getText().toString(),
                        ((EditText) findViewById(R.id.sifreEditText)).getText().toString(),
                        android.provider.Settings.System.getString(RegisterActivity.super.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID),
                        ((EditText) findViewById(R.id.isimEditText)).getText().toString(),
                        ((EditText) findViewById(R.id.soyisimEditText)).getText().toString());
        }
    }
}
