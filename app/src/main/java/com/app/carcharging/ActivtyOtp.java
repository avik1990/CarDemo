package com.app.carcharging;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.app.carcharging.databinding.ActivityOtpBinding;
import com.app.carcharging.helper.CUtils;
import com.app.carcharging.pojo.OtpPost;
import com.app.carcharging.pojo.RegisterResponse;
import com.app.carcharging.retrofit.api.ApiServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivtyOtp extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ActivityOtpBinding binding;
    String st_rid = "", phoneno = "", email = "", country_id = "", user_id = "", st_password = "", otp = "";
    ProgressDialog pdialog;
    boolean click_success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        pdialog = CUtils.initProgressdialog(mContext, "Loading...");
        st_rid = getIntent().getExtras().getString("st_rid");
        phoneno = getIntent().getExtras().getString("phoneno");
        st_password = getIntent().getExtras().getString("password");
        user_id = getIntent().getExtras().getString("user_id");
        email = getIntent().getExtras().getString("email");
        otp = getIntent().getExtras().getString("otp");
        country_id = getIntent().getExtras().getString("country_id");
        CUtils.showToastShort(mContext,otp);
        initview();
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        binding.btnSignin.setOnClickListener(this);
        binding.pinView.setAnimationEnable(true);
        binding.pinView.setCursorVisible(true);
        binding.pinView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black_1, getTheme()));
        binding.pinView.setTextColor(ResourcesCompat.getColorStateList(getResources(), R.color.black_1, getTheme()));
        binding.pinView.setLineColor(ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
        binding.pinView.setLineColor(ResourcesCompat.getColorStateList(getResources(), R.color.line_colors, getTheme()));
        binding.pinView.setItemCount(4);
        binding.pinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        binding.pinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        binding.pinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        binding.pinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        binding.pinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        binding.pinView.setCursorColor(ResourcesCompat.getColor(getResources(), R.color.green_color, getTheme()));
        binding.pinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));

        binding.pinView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    /*String Userid;
                    String otpstatus;
                    String contactno;
                    String password;
                    String latitude;
                    String longitude;*/

                    click_success = true;
                    if (otp.equalsIgnoreCase(binding.pinView.getText().toString().trim())) {
                        OtpPost otpPost = new OtpPost();
                        otpPost.setUserid(user_id);
                        otpPost.setOtpstatus("1");
                        otpPost.setContactno(phoneno);
                        otpPost.setPassword(st_password);
                        otpPost.setLatitude("0.00");
                        otpPost.setLongitude("0.00");
                        PostOTPData(otpPost);
                    } else {
                        CUtils.showToastShort(mContext, "Your OTP doesn't matches");
                    }


                    CUtils.showToastShort(mContext, binding.pinView.getText().toString().trim());
                } else {
                    click_success = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSignin) {
            if (click_success) {
                if (st_rid.equalsIgnoreCase("4")) {
                    Intent i = new Intent(mContext, TechnicianVerification.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(mContext, Dashboard.class);
                    startActivity(i);
                }
            } else {
                CUtils.showToastShort(mContext, "Please enter the OTP");
            }
        }
    }

    private void PostOTPData(OtpPost registerPojo) {
        pdialog.show();
        String BASE_URL = getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<RegisterResponse> call = redditAPI.VerifyOTP(registerPojo);
        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    RegisterResponse rp = response.body();
                    if (rp.getStatus().equalsIgnoreCase("1")) {
                        CUtils.setisVerfiedPreferences(mContext, true);
                        CUtils.setUserid(mContext, rp.getData().get(0).getUserid());
                        CUtils.setUsername(mContext, rp.getData().get(0).getUsername());
                        Intent i = new Intent(mContext, Dashboard.class);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(i);
                        finishAffinity();
                    } else {

                    }
                }
                pdialog.dismiss();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                pdialog.dismiss();
            }
        });
    }

}
