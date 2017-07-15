package com.locanthach.sharefood.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.locanthach.sharefood.R;

//Use for test
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }


    public void QR(View view) {
        Intent intent = new Intent(Main2Activity.this,QRScannerActivity.class);
        startActivity(intent);
    }
}
