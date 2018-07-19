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

import com.app.carcharging.databinding.ActivityNewcarBinding;
import com.app.carcharging.helper.CUtils;
import com.app.carcharging.helper.ConnectionDetector;
import com.app.carcharging.pojo.GetCarManufacturer;
import com.app.carcharging.retrofit.api.ApiServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.CAMERA;

public class ActivityNewCar extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressDialog pdialog;
    ConnectionDetector cd;

    String[] manufacturer_name, manufacturer_id;
    String[] model_name, model_id;
    GetCarManufacturer getCarManufacturer;

    String st_manufacturer_id = "", st_model_id = "";

    private static final int PERMISSION_REQUEST_CODE = 200;
    ActivityNewcarBinding binding;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_newcar);
        binding.toolbar.tvTitle.setText("Purchase New Car");
        binding.toolbar.ivBack.setOnClickListener(this);
        pdialog = CUtils.initProgressdialog(mContext, "Loading...");


        binding.btnProceed.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnProceed) {
            if (!TextUtils.isEmpty(st_manufacturer_id) && !TextUtils.isEmpty(st_model_id)) {
                Intent i = new Intent(mContext, ActivityCarDetails.class);
                i.putExtra("manufacturer_id", st_manufacturer_id);
                i.putExtra("model_id", st_model_id);
                startActivity(i);
            } else {
                CUtils.showToastShort(mContext, "Select Manufacturer and Model");
            }
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

    private void parsejsondata() {
        pdialog.show();
        String BASE_URL = getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<GetCarManufacturer> call = redditAPI.getCarManufacturer();
        call.enqueue(new Callback<GetCarManufacturer>() {

            @Override
            public void onResponse(Call<GetCarManufacturer> call, retrofit2.Response<GetCarManufacturer> response) {
                if (response.isSuccessful()) {
                    getCarManufacturer = response.body();
                    if (!getCarManufacturer.getStatus().equalsIgnoreCase("0")) {
                        if (getCarManufacturer.getData().size() > 0) {
                            manufacturer_name = new String[getCarManufacturer.getData().size() + 1];
                            manufacturer_id = new String[getCarManufacturer.getData().size() + 1];
                            manufacturer_name[0] = "Select a car manufacturer";
                            manufacturer_id[0] = "";

                            for (int i = 0; i < getCarManufacturer.getData().size(); i++) {
                                manufacturer_name[i + 1] = getCarManufacturer.getData().get(i).getManufacturername();
                                manufacturer_id[i + 1] = getCarManufacturer.getData().get(i).getManufacturerid();
                            }
                            inflatespinner();
                        }
                    } else {
                        CUtils.showToastShort(mContext, getCarManufacturer.getMessage());
                    }
                }
                pdialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetCarManufacturer> call, Throwable t) {
                pdialog.dismiss();
            }
        });
    }

    private void inflatespinner() {

        ArrayAdapter<String> sp_manufacturer = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview, manufacturer_name
        );

        sp_manufacturer.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.spManufacturer.setAdapter(sp_manufacturer);

        binding.spManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    binding.vSp2.setVisibility(View.VISIBLE);
                    binding.divider1.setVisibility(View.VISIBLE);
                    st_manufacturer_id = manufacturer_id[position];
                    inflatemodelspinner(position - 1);
                } else {
                    st_manufacturer_id = "";
                    binding.vSp2.setVisibility(View.GONE);
                    binding.divider1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void inflatemodelspinner(int position) {
        model_name = new String[getCarManufacturer.getData().get(position).getCarmodel().size() + 1];
        model_id = new String[getCarManufacturer.getData().get(position).getCarmodel().size() + 1];
        model_name[0] = "Select a car model";
        model_id[0] = "";

        for (int i = 0; i < getCarManufacturer.getData().get(position).getCarmodel().size(); i++) {
            model_name[i + 1] = getCarManufacturer.getData().get(position).getCarmodel().get(i).getCarmodelname();
            model_id[i + 1] = getCarManufacturer.getData().get(position).getCarmodel().get(i).getCarmodelid();
        }

        ArrayAdapter<String> sp_model = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview, model_name
        );
        sp_model.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.spModel.setAdapter(sp_model);
        binding.spModel.setAdapter(sp_model);
        binding.spModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    st_model_id = model_id[position];
                } else {
                    st_model_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
