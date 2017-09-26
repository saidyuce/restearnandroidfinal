package com.theoc.restapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class QRActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    QRCodeReaderView qrreader;
    boolean flashBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        qrreader = (QRCodeReaderView) findViewById(R.id.qrreader);
        qrreader.setOnQRCodeReadListener(this);
        qrreader.setQRDecodingEnabled(true);
        qrreader.setAutofocusInterval(2000L);
        findViewById(R.id.flashImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashBool) {
                    qrreader.setTorchEnabled(false);
                    flashBool = false;
                } else {
                    qrreader.setTorchEnabled(true);
                    flashBool = true;
                }
            }
        });
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
       // Intent intent = new Intent(this, CafeJoinActivity.class);
        qrreader.stopCamera();
        Intent intent = new Intent(this,MyPointsActivity.class);
        intent.putExtra("qrText", text);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(QRActivity.this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(QRActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        1);

        } else {
            qrreader.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrreader.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    qrreader.startCamera();
                } else {
                    Toast.makeText(this, "Kamera izni vermediniz. QR kod okutmak için kamera izni vermelisiniz", Toast.LENGTH_SHORT).show();
                }
                break;    
            }
        }
    }
}
