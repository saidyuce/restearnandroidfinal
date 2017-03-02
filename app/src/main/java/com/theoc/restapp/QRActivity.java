package com.theoc.restapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class QRActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    QRCodeReaderView qrreader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

//
        qrreader = (QRCodeReaderView) findViewById(R.id.qrreader);
        qrreader.setOnQRCodeReadListener(this);



    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Intent intent = new Intent(this, CafeJoinActivity.class);
        intent.putExtra("qrText", text);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        qrreader.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrreader.getCameraManager().stopPreview();
    }
}
