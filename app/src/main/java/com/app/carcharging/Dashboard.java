package com.app.carcharging;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.app.carcharging.databinding.ActivityDashboardBinding;
import com.app.carcharging.helper.CUtils;

public class Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Context mContext;
    ActivityDashboardBinding binding;
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mContext = this;

        initview();
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.navigation.setOnNavigationItemSelectedListener(this);
        CUtils.removeShiftMode(binding.navigation);

        binding.vNewcar.setOnClickListener(this);
        binding.vSummary.setOnClickListener(this);
        binding.vOrder.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        CUtils.openBottomNav(id, mContext);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.vSummary) {
            binding.vContainer.setVisibility(View.VISIBLE);
            binding.vContainer1.setVisibility(View.GONE);
        } else if (v == binding.vOrder) {
            binding.vContainer.setVisibility(View.GONE);
            binding.vContainer1.setVisibility(View.VISIBLE);
        } else if (v == binding.vNewcar) {
            Intent i = new Intent(mContext, ActivityNewCar.class);
            startActivity(i);
        }
    }
}
