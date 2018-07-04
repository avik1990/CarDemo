package com.app.carcharging;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.app.carcharging.databinding.ActivityScannerBinding;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ActivityQRScanner extends AppCompatActivity implements View.OnClickListener, ZXingScannerView.ResultHandler {

    Context mContext;
    ActivityScannerBinding binding;
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        initview();

    }

    private void initview() {
        mScannerView = new ZXingScannerView(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scanner);
        binding.contentFrame.addView(mScannerView);
        binding.tvFootertext.setOnClickListener(this);
        /// binding.tvSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.tvFootertext) {
            Intent i = new Intent(mContext, ActivityRegisterAfterScan.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ActivityQRScanner.this);
            }
        }, 2000);

        if (!rawResult.getText().toString().isEmpty()) {
            Intent i = new Intent(mContext, ActivityRegisterAfterScan.class);
            startActivity(i);
        }
    }
}
