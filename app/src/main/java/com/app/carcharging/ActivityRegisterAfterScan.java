package com.app.carcharging;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import com.app.carcharging.databinding.ActivityRegisteruserafterscanBinding;
import com.app.carcharging.helper.CUtils;

import net.alexandroid.gps.GpsStatusDetector;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class ActivityRegisterAfterScan extends AppCompatActivity implements View.OnClickListener, GpsStatusDetector.GpsStatusDetectorCallBack {

    Context mContext;
    ActivityRegisteruserafterscanBinding binding;
    String radio_txt = "";
    String toggle_txt = "certified";
    private GpsStatusDetector mGpsStatusDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();
        initview();
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registeruserafterscan);
        binding.btnSignup.setOnClickListener(this);

        binding.rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_user:
                        binding.vToggle.setVisibility(View.GONE);
                        radio_txt = "user";
                        // do operations specific to this selection
                        break;
                    case R.id.rb_technician:
                        binding.vToggle.setVisibility(View.GONE);
                        radio_txt = "technician";
                        break;
                    case R.id.rb_communit_owner:
                        binding.vToggle.setVisibility(View.VISIBLE);
                        radio_txt = "owner";
                        break;
                    case R.id.rb_cab_owner:
                        binding.vToggle.setVisibility(View.GONE);
                        radio_txt = "driver";
                        break;

                }
            }
        });

        binding.bedSwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                //Log.d(TAG, "Now Selected : " + direction.name() + ", Current Text : " + text);
                //CUtils.showToastShort(mContext, "" + direction.name());
                if (direction.name().equalsIgnoreCase("Right")) {
                    toggle_txt = "non-certified";
                } else {
                    toggle_txt = "certified";
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSignup) {
            validateform();
            /*Intent i = new Intent(mContext, Dashboard.class);
            startActivity(i);*/
        }
    }

    private void validateform() {
        if (!TextUtils.isEmpty(binding.etPhoneno.getText().toString().trim()) && binding.etPhoneno.getText().toString().trim().length() >= 10) {
            if (!TextUtils.isEmpty(binding.etPassword.getText().toString().trim()) && binding.etPassword.getText().toString().trim().length() > 6) {
                if (!TextUtils.isEmpty(radio_txt)) {
                    Intent i = new Intent(mContext, ActivtyOtp.class);
                    i.putExtra("type", radio_txt);
                    startActivity(i);
                } else {
                    binding.etPassword.clearFocus();
                    binding.etPhoneno.clearFocus();
                    CUtils.showToastLong(mContext, "Please select your role");
                }
            } else {
                binding.etPassword.requestFocus();
                CUtils.showToastShort(mContext, "Please must be greater than 6 digits");
            }
        } else {
            binding.etPhoneno.requestFocus();
            CUtils.showToastShort(mContext, "Please enter valid phone no.");

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
