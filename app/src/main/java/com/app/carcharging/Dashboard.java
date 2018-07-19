package com.app.carcharging;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.carcharging.databinding.ActivityDashboardBinding;
import com.app.carcharging.helper.CUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Context mContext;
    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/univers.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_dashboard);
        mContext = this;

        initview();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.navigation.setOnNavigationItemSelectedListener(this);
        CUtils.removeShiftMode(binding.navigation);

        binding.vNewcar.setOnClickListener(this);
        binding.vSummary.setOnClickListener(this);
        binding.vOrder.setOnClickListener(this);
        binding.vCharge.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        CUtils.openBottomNav(id, mContext, binding.navigation);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CUtils.updateNavigationBarState(R.id.navigation_home, binding.navigation);
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
        } else if (v == binding.vCharge) {
            Intent i = new Intent(mContext, ActivityCharging.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(getApplicationContext(), "Android Clicked", Toast.LENGTH_LONG).show();
                return true;


            default:

                super.onOptionsItemSelected(item);

        }
        return true;

    }
}
