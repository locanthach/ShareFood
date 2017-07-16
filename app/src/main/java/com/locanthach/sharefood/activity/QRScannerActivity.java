package com.locanthach.sharefood.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.common.Constant;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by An Lee on 7/16/2017.
 */
public class QRScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        contentFrame.addView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(this, "Contents = " + result.getText() +
                ", Format = " + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(() -> mScannerView.resumeCameraPreview(QRScannerActivity.this), 2000);

        String scanResult = result.getText();
        Intent data = new Intent();
        data.putExtra(Constant.SCAN_RESULT, scanResult);
        setResult(RESULT_OK, data);
        finish();
    }
}
