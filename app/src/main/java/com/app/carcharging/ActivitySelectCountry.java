package com.app.carcharging;

import android.app.ProgressDialog;
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
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.app.carcharging.databinding.ActivitySelectcountryBinding;
import com.app.carcharging.helper.CUtils;
import com.app.carcharging.helper.ConnectionDetector;
import com.app.carcharging.pojo.Country;
import com.app.carcharging.retrofit.api.ApiServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.CAMERA;

public class ActivitySelectCountry extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ConnectionDetector cd;
    ProgressDialog pdialog;

    Country cList;
    String[] country_name, country_id;
    String st_cid;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ActivitySelectcountryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/univers.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pdialog = CUtils.initProgressdialog(mContext, "Loading...");
        initview();
        if (cd.isConnected()) {
            parsejsondata();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_selectcountry);
        binding.btnProceed.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == binding.btnProceed) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                if (!TextUtils.isEmpty(st_cid)) {
                    Intent i = new Intent(mContext, ActivityQRScanner.class);
                    i.putExtra("from", "ActivitySelectCountry");
                    i.putExtra("country_id", st_cid);
                    startActivity(i);
                } else {
                    CUtils.showToastShort(mContext, "Please Select Country");
                }
            }
        } else if (v == binding.ivBack) {
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
                       /* Intent i = new Intent(mContext, ActivityQRScanner.class);
                        startActivity(i);*/
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


    private void parsejsondata() {
        pdialog.show();
        String BASE_URL = getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Country> call = redditAPI.getCountryList();
        call.enqueue(new Callback<Country>() {

            @Override
            public void onResponse(Call<Country> call, retrofit2.Response<Country> response) {
                if (response.isSuccessful()) {
                    cList = response.body();
                    if (cList.getData().size() > 0) {
                        country_name = new String[cList.getData().size() + 1];
                        country_id = new String[cList.getData().size() + 1];
                        country_name[0] = "Select Country";
                        country_id[0] = "";

                        for (int i = 0; i < cList.getData().size(); i++) {
                            country_name[i + 1] = cList.getData().get(i).getCountryname();
                            country_id[i + 1] = cList.getData().get(i).getCountryid();
                        }
                        inflatespinner();
                        //Toast.makeText(mContext, "" + cList.getData().size(), Toast.LENGTH_SHORT).show();
                    }
                }
                pdialog.dismiss();
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                pdialog.dismiss();
            }
        });
    }

    private void inflatespinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_textview, country_name);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.spinnerCustom.setAdapter(spinnerArrayAdapter);

        binding.spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    st_cid = cList.getData().get(i - 1).getCountryid();
                    CUtils.showToastShort(mContext, st_cid);
                } else {
                    st_cid = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
