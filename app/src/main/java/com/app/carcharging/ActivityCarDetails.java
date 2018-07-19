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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.app.carcharging.databinding.ActivityCardetailsBinding;
import com.app.carcharging.helper.CUtils;
import com.app.carcharging.helper.ConnectionDetector;
import com.app.carcharging.pojo.GetCarModelFilterSet;
import com.app.carcharging.pojo.GetCarModelFilterSetPost;
import com.app.carcharging.retrofit.api.ApiServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.CAMERA;

public class ActivityCarDetails extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    GetCarModelFilterSet getCarModelFilterSet;
    String st_version = "", st_city = "", st_color = "", st_fuel = "", st_transmission = "";
    GetCarModelFilterSetPost postbody;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ActivityCardetailsBinding binding;
    String manufacturer_id = "", model_id = "";
    ProgressDialog pdialog;
    ConnectionDetector cd;
    String[] version_name_arr, version_id_arr;
    String[] city_name_arr, city_id_arr;
    String[] color_name_arr, color_id_arr;
    String[] fuel_name_arr, fuel_id_arr;
    String[] trans_name_arr, trans_id_arr;

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
        pdialog = new ProgressDialog(mContext);
        manufacturer_id = getIntent().getExtras().getString("manufacturer_id");
        model_id = getIntent().getExtras().getString("model_id");

        postbody = new GetCarModelFilterSetPost();
        postbody.setCarmodelid(model_id);
        postbody.setCountryid("1");

        if (cd.isConnected()) {
            parsejsondata();
        }


        initview();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cardetails);

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

    private void parsejsondata() {
        pdialog.show();
        String BASE_URL = getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<GetCarModelFilterSet> call = redditAPI.GetCarModelFilterSet(postbody);
        call.enqueue(new Callback<GetCarModelFilterSet>() {

            @Override
            public void onResponse(Call<GetCarModelFilterSet> call, retrofit2.Response<GetCarModelFilterSet> response) {
                if (response.isSuccessful()) {
                    Log.d("Response", response.toString());
                    getCarModelFilterSet = response.body();
                    if (!getCarModelFilterSet.getStatus().equalsIgnoreCase("0")) {
                        if (getCarModelFilterSet.getData().size() > 0) {
                            inflatespinner();
                        }
                    } else {
                        CUtils.showToastShort(mContext, getCarModelFilterSet.getMessage());
                    }
                }
                pdialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetCarModelFilterSet> call, Throwable t) {
                pdialog.dismiss();
            }
        });
    }

    private void inflatespinner() {

        ////////for version
        version_name_arr = new String[getCarModelFilterSet.getData().get(0).getCarversion().size() + 1];
        version_id_arr = new String[getCarModelFilterSet.getData().get(0).getCarversion().size() + 1];
        version_name_arr[0] = "Select Version";
        version_id_arr[0] = "";

        for (int i = 0; i < getCarModelFilterSet.getData().get(0).getCarversion().size(); i++) {
            version_name_arr[i + 1] = getCarModelFilterSet.getData().get(0).getCarversion().get(i).getVersionname();
            version_id_arr[i + 1] = getCarModelFilterSet.getData().get(0).getCarversion().get(i).getVersionid();
        }
        ArrayAdapter<String> sp_manufacturer = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview_small, version_name_arr
        );
        sp_manufacturer.setDropDownViewResource(R.layout.spinner_custom_textview_small);
        binding.spVersion.setAdapter(sp_manufacturer);

        binding.spVersion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    st_version = binding.spVersion.getSelectedItem().toString();
                    CUtils.showToastShort(mContext, st_version);
                } else {
                  /*  binding.vSp2.setVisibility(View.GONE);
                    binding.divider1.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /////////////////////////

        ////////for city
        city_name_arr = new String[getCarModelFilterSet.getData().get(0).getCity().size() + 1];
        city_id_arr = new String[getCarModelFilterSet.getData().get(0).getCity().size() + 1];
        city_name_arr[0] = "Select City";
        city_id_arr[0] = "";

        for (int i = 0; i < getCarModelFilterSet.getData().get(0).getCity().size(); i++) {
            city_name_arr[i + 1] = getCarModelFilterSet.getData().get(0).getCity().get(i).getCityname();
            city_id_arr[i + 1] = getCarModelFilterSet.getData().get(0).getCity().get(i).getCityid();
        }
        ArrayAdapter<String> sp_city = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview_small, city_name_arr
        );
        sp_city.setDropDownViewResource(R.layout.spinner_custom_textview_small);
        binding.spCity.setAdapter(sp_city);

        binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    st_city = binding.spCity.getSelectedItem().toString();
                    CUtils.showToastShort(mContext, st_city);
                } else {
                  /*  binding.vSp2.setVisibility(View.GONE);
                    binding.divider1.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //////////////////////////////

        ////////for color
        color_name_arr = new String[getCarModelFilterSet.getData().get(0).getCarmodelcolor().size() + 1];
        color_id_arr = new String[getCarModelFilterSet.getData().get(0).getCarmodelcolor().size() + 1];
        color_name_arr[0] = "Select Color";
        color_id_arr[0] = "";

        for (int i = 0; i < getCarModelFilterSet.getData().get(0).getCarmodelcolor().size(); i++) {
            color_name_arr[i + 1] = getCarModelFilterSet.getData().get(0).getCarmodelcolor().get(i).getColorname();
            color_id_arr[i + 1] = getCarModelFilterSet.getData().get(0).getCarmodelcolor().get(i).getColorid();
        }
        ArrayAdapter<String> sp_color = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview_small, color_name_arr
        );
        sp_color.setDropDownViewResource(R.layout.spinner_custom_textview_small);
        binding.spColor.setAdapter(sp_color);

        binding.spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    st_color = binding.spColor.getSelectedItem().toString();
                    CUtils.showToastShort(mContext, st_color);
                } else {
                  /*  binding.vSp2.setVisibility(View.GONE);
                    binding.divider1.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ////////for fuel type
        fuel_name_arr = new String[getCarModelFilterSet.getData().get(0).getFueltype().size() + 1];
        fuel_id_arr = new String[getCarModelFilterSet.getData().get(0).getFueltype().size() + 1];
        fuel_name_arr[0] = "Select Fuel Type";
        fuel_id_arr[0] = "";

        for (int i = 0; i < getCarModelFilterSet.getData().get(0).getFueltype().size(); i++) {
            fuel_name_arr[i + 1] = getCarModelFilterSet.getData().get(0).getFueltype().get(i).getFueltypename();
            fuel_id_arr[i + 1] = getCarModelFilterSet.getData().get(0).getFueltype().get(i).getFueltypeid();
        }
        ArrayAdapter<String> sp_fueltype = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview_small, fuel_name_arr
        );
        sp_fueltype.setDropDownViewResource(R.layout.spinner_custom_textview_small);
        binding.spFueltype.setAdapter(sp_fueltype);

        binding.spFueltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    st_fuel = binding.spFueltype.getSelectedItem().toString();
                    CUtils.showToastShort(mContext, st_fuel);
                } else {
                  /*  binding.vSp2.setVisibility(View.GONE);
                    binding.divider1.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ////////for transmission type
        trans_name_arr = new String[getCarModelFilterSet.getData().get(0).getTransmissiontype().size() + 1];
        trans_id_arr = new String[getCarModelFilterSet.getData().get(0).getTransmissiontype().size() + 1];
        trans_name_arr[0] = "Select Transmission Type";
        trans_id_arr[0] = "";

        for (int i = 0; i < getCarModelFilterSet.getData().get(0).getTransmissiontype().size(); i++) {
            trans_name_arr[i + 1] = getCarModelFilterSet.getData().get(0).getTransmissiontype().get(i).getTransmissionname();
            trans_id_arr[i + 1] = getCarModelFilterSet.getData().get(0).getTransmissiontype().get(i).getTransmissiontypeid();
        }
        ArrayAdapter<String> sp_transmissiontype = new ArrayAdapter<>(
                this, R.layout.spinner_custom_textview_small, trans_name_arr
        );
        sp_transmissiontype.setDropDownViewResource(R.layout.spinner_custom_textview_small);
        binding.spTransmission.setAdapter(sp_transmissiontype);

        binding.spTransmission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    st_transmission = binding.spTransmission.getSelectedItem().toString();
                    CUtils.showToastShort(mContext, st_transmission);
                } else {
                  /*  binding.vSp2.setVisibility(View.GONE);
                    binding.divider1.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


}
