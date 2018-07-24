package com.app.carcharging;

import android.app.ProgressDialog;
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
import com.app.carcharging.pojo.CertificateVeriPost;
import com.app.carcharging.pojo.CertificateVeriResponse;
import com.app.carcharging.retrofit.api.ApiServices;

import net.alexandroid.gps.GpsStatusDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TechnicianVerification extends AppCompatActivity implements View.OnClickListener, GpsStatusDetector.GpsStatusDetectorCallBack {

    Context mContext;
    ActivityVerificationBinding binding;
    ConnectionDetector cd;
    private GpsStatusDetector mGpsStatusDetector;
    ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/univers.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        mContext = this;
        pdialog = new ProgressDialog(mContext);
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();
        cd = new ConnectionDetector(mContext);
        initview();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
            if (cd.isConnected()) {
                CertificateVeriPost cvp = new CertificateVeriPost();
                cvp.setUserid(CUtils.getUserid(mContext));
                cvp.setContactno(CUtils.getPrimaryPhoneid(mContext));
                cvp.setCertificateno(binding.etCeritificateno.getText().toString().trim());
                PostOTPData(cvp);
            }
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

    private void PostOTPData(CertificateVeriPost registerPojo) {
        pdialog.show();
        String BASE_URL = getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CertificateVeriResponse> call = redditAPI.VerifyCertificate(registerPojo);
        call.enqueue(new Callback<CertificateVeriResponse>() {

            @Override
            public void onResponse(Call<CertificateVeriResponse> call, retrofit2.Response<CertificateVeriResponse> response) {
                if (response.isSuccessful()) {
                    CertificateVeriResponse rp = response.body();
                    if (rp.getStatus().equalsIgnoreCase("1")) {
                        CUtils.setisCertiVerfiedPreferences(mContext, true);
                        Intent i = new Intent(mContext, ActivtyJobListing.class);
                        startActivity(i);
                    } else {
                        CUtils.setisCertiVerfiedPreferences(mContext, false);
                    }
                }
                pdialog.dismiss();
            }

            @Override
            public void onFailure(Call<CertificateVeriResponse> call, Throwable t) {
                pdialog.dismiss();
            }
        });
    }
}



