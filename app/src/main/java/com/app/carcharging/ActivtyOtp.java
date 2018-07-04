package com.app.carcharging;

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

public class ActivtyOtp extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ActivityOtpBinding binding;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        type = getIntent().getExtras().getString("type");
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
                    //Log.d("output", binding.pinView.getText().toString().trim());
                    CUtils.showToastShort(mContext, binding.pinView.getText().toString().trim());
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
            if (type.equalsIgnoreCase("technician")) {
                Intent i = new Intent(mContext, TechnicianVerification.class);
                startActivity(i);
            } else {
                Intent i = new Intent(mContext, Dashboard.class);
                startActivity(i);
            }
        }
    }
}
