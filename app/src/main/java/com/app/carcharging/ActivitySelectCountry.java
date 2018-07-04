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
import android.widget.ArrayAdapter;


import com.app.carcharging.databinding.ActivitySelectcountryBinding;

import static android.Manifest.permission.CAMERA;

public class ActivitySelectCountry extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    String[] plants = new String[]{
            "Please Select Country",
            "India",
            "France",
            "England",
            "Asia"
    };
    private static final int PERMISSION_REQUEST_CODE = 200;
    ActivitySelectcountryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        initview();
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_selectcountry);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_custom_textview, plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.spinnerCustom.setAdapter(spinnerArrayAdapter);


        binding.btnProceed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnProceed) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                //CUtils.showToastShort(mContext, "Permission granted");
                Intent i = new Intent(mContext, ActivityQRScanner.class);
                startActivity(i);
            }
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
