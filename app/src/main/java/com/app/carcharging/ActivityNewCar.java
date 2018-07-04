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

import com.app.carcharging.databinding.ActivityNewcarBinding;
import com.app.carcharging.helper.CUtils;

import static android.Manifest.permission.CAMERA;

public class ActivityNewCar extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    String[] manufacturer = new String[]{
            "Select a car manufacturer",
            "BMW",
            "Buggati",
            "England",
            "Asia"
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
    ActivityNewcarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        initview();
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_newcar);
        binding.toolbar.tvTitle.setText("Purchase New Car");
        binding.toolbar.ivBack.setOnClickListener(this);
        /////////////////////////////////
        ArrayAdapter<String> sp_manufacturer = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview, manufacturer
        );
        sp_manufacturer.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.spManufacturer.setAdapter(sp_manufacturer);

        binding.spManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    binding.vSp2.setVisibility(View.VISIBLE);
                    binding.divider1.setVisibility(View.VISIBLE);
                    st_manufacturer = binding.spManufacturer.getSelectedItem().toString();
                    CUtils.showToastShort(mContext, st_manufacturer);
                } else {
                    binding.vSp2.setVisibility(View.GONE);
                    binding.divider1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ///////////////////////////////////////
        ArrayAdapter<String> sp_model = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview, model
        );
        sp_model.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.spModel.setAdapter(sp_model);
        binding.spModel.setAdapter(sp_model);
        binding.spModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    st_model = binding.spModel.getSelectedItem().toString();
                    CUtils.showToastShort(mContext, st_manufacturer);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ////////////////////////////////////

        binding.btnProceed.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnProceed) {
            Intent i = new Intent(mContext, ActivityCarDetails.class);
            startActivity(i);
            /*if (!checkPermission()) {
                requestPermission();
            } else {
                Intent i = new Intent(mContext, ActivityCarDetails.class);
                startActivity(i);
            }*/
        } else if (v == binding.toolbar.ivBack) {
            onBackPressed();
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
