package com.app.carcharging;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.app.carcharging.databinding.ActivityVerificationBinding;
import com.app.carcharging.helper.CUtils;
import com.app.carcharging.helper.ConnectionDetector;

import net.alexandroid.gps.GpsStatusDetector;

public class TechnicianVerification extends AppCompatActivity implements View.OnClickListener, GpsStatusDetector.GpsStatusDetectorCallBack {

    Context mContext;
    ActivityVerificationBinding binding;
    ConnectionDetector cd;
    private GpsStatusDetector mGpsStatusDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();
        cd = new ConnectionDetector(mContext);
        initview();

    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification);
        binding.btnVerify.setOnClickListener(this);
        binding.btnCenter.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnVerify) {
            if (cd.isConnected()) {
                verifycertificate();
            } else {
                CUtils.showToastShort(mContext, "No Internet Connection");
            }
        } else if (v == binding.btnCenter) {
            if (cd.isConnected()) {
                Intent i = new Intent(mContext, ActivityCertificationCenter.class);
                startActivity(i);
            } else {
                CUtils.showToastShort(mContext, "No Internet Connection");
            }
        }
    }

    private void verifycertificate() {
        if (!TextUtils.isEmpty(binding.etCeritificateno.getText().toString().trim())) {
            Intent i = new Intent(mContext, ActivtyJobListing.class);
            startActivity(i);
        } else {
            binding.etCeritificateno.requestFocus();
            CUtils.showToastShort(mContext, "Enter Certification no.");
        }
    }

    @Override
    public void onGpsSettingStatus(boolean enabled) {
    }

    @Override
    public void onGpsAlertCanceledByUser() {
        finish();
        CUtils.showToastShort(mContext, "Can not Proceed without GPS ON");
    }
}
