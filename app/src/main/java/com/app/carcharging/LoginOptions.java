package com.app.carcharging;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.app.carcharging.databinding.ActivityLoginoptionsBinding;

public class LoginOptions extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ActivityLoginoptionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        initview();

    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loginoptions);
        binding.btnCreateaccount.setOnClickListener(this);
        binding.btnSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSignin) {
            Intent i = new Intent(mContext, SignIn.class);
            startActivity(i);
        }
        if (v == binding.btnCreateaccount) {
            Intent i = new Intent(mContext, ActivitySelectCountry.class);
            startActivity(i);
        }
    }
}
