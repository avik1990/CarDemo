package com.app.carcharging;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.app.carcharging.databinding.ActivityCardetailsBinding;
import com.app.carcharging.helper.CUtils;

import static android.Manifest.permission.CAMERA;

public class ActivityCarDetails extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    String[] manufacturer = new String[]{
            "Select version",
            "LDI",
            "LXI",
    };

    String[] model = new String[]{
            "Please car model",
            "BMW i",
            "Bugati x10",
            "ZEN",
            "BMW x10"
    };

    String st_manufacturer = "", st_model = "";

    private static final int PERMISSION_REQUEST_CODE = 200;
    ActivityCardetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        initview();
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cardetails);

        /////////////////////////////////
        ArrayAdapter<String> sp_manufacturer = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview_small, manufacturer
        );
        sp_manufacturer.setDropDownViewResource(R.layout.spinner_custom_textview_small);
        binding.spVersion.setAdapter(sp_manufacturer);

        binding.spVersion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    st_manufacturer = binding.spVersion.getSelectedItem().toString();
                    CUtils.showToastShort(mContext, st_manufacturer);
                } else {
                   /* binding.vSp2.setVisibility(View.GONE);
                    binding.divider1.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       /* ///////////////////////////////////////
        ArrayAdapter<String> sp_model = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview, model
        );
        sp_model.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.spModel.setAdapter(sp_model);
        binding.spModel.setAdapter(sp_model);
        binding.spModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0) {
                    st_model = binding.spModel.getSelectedItem().toString();
                    CUtils.showToastShort(mContext, st_manufacturer);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        ////////////////////////////////////
        // binding.btnProceed.setOnClickListener(this);
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.vSummary.setOnClickListener(this);
        binding.vSpecs.setOnClickListener(this);
        binding.vFeatures.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.toolbar.ivBack) {
            onBackPressed();
        } else if (v == binding.vSummary) {
            binding.vSummary.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.tab1_selected));
            binding.vSpecs.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.tab2_2_unselected));
            binding.vFeatures.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.tab2_3));
            binding.tvSummary.setTextColor(getResources().getColor(R.color.white));
            binding.tvSpecs.setTextColor(getResources().getColor(R.color.black));
            binding.tvFeatures.setTextColor(getResources().getColor(R.color.black));
            binding.tv1.setVisibility(View.VISIBLE);
            binding.tv2.setVisibility(View.GONE);
            binding.tv3.setVisibility(View.GONE);
        } else if (v == binding.vSpecs) {
            binding.vSummary.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.tab1));
            binding.vSpecs.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.tab2_2));
            binding.vFeatures.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.tab2_3));
            binding.tvSpecs.setTextColor(getResources().getColor(R.color.white));
            binding.tvSummary.setTextColor(getResources().getColor(R.color.black));
            binding.tvFeatures.setTextColor(getResources().getColor(R.color.black));
            binding.tv1.setVisibility(View.GONE);
            binding.tv2.setVisibility(View.VISIBLE);
            binding.tv3.setVisibility(View.GONE);
        } else if (v == binding.vFeatures) {
            binding.vSummary.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.tab1));
            binding.vSpecs.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.tab2_2_unselected));
            binding.vFeatures.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.tab2));
            binding.tvFeatures.setTextColor(getResources().getColor(R.color.white));
            binding.tvSpecs.setTextColor(getResources().getColor(R.color.black));
            binding.tvSummary.setTextColor(getResources().getColor(R.color.black));
            binding.tv1.setVisibility(View.GONE);
            binding.tv2.setVisibility(View.GONE);
            binding.tv3.setVisibility(View.VISIBLE);
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    //boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Intent i = new Intent(mContext, ActivityQRScanner.class);
                        startActivity(i);
                        //Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    } else {
                        // Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }

                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
