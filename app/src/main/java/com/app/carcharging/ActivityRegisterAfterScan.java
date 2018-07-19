package com.app.carcharging;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.carcharging.databinding.ActivityRegisteruserafterscanBinding;
import com.app.carcharging.helper.CUtils;
import com.app.carcharging.helper.ConnectionDetector;
import com.app.carcharging.pojo.GetUserRole;
import com.app.carcharging.pojo.RegisterPojo;
import com.app.carcharging.pojo.RegisterResponse;
import com.app.carcharging.retrofit.api.ApiServices;

import net.alexandroid.gps.GpsStatusDetector;

import io.ghyeok.stickyswitch.widget.StickySwitch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class ActivityRegisterAfterScan extends AppCompatActivity implements View.OnClickListener, GpsStatusDetector.GpsStatusDetectorCallBack {

    Context mContext;
    ActivityRegisteruserafterscanBinding binding;
    String radio_txt = "";
    String toggle_txt = "certified";
    private GpsStatusDetector mGpsStatusDetector;
    String country_id = "";
    ConnectionDetector cd;
    ProgressDialog pdialog;
    GetUserRole rList;
    String[] role_id, role_name;
    String st_rid, st_password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/univers.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        mContext = this;
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();
        country_id = getIntent().getExtras().getString("country_id");
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pdialog = CUtils.initProgressdialog(mContext, "Loading...");

        initview();

        if (cd.isConnected()) {
            getJsonRoles();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registeruserafterscan);
        binding.btnSignup.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.tvSignin.setOnClickListener(this);

        String str = "<font color='#000000'>Email Address </font> <font color='#8eac28'> (optional) </font> </a>";
        binding.etEmail.setHint(Html.fromHtml(str));

        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append("Already have an account? Sign In");
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getAssets(), "fonts/Univers_bold.otf"));
        sBuilder.setSpan(typefaceSpan, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvSignin.setText(sBuilder, TextView.BufferType.SPANNABLE);


        binding.rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_user:
                        binding.vToggle.setVisibility(View.GONE);
                        radio_txt = "user";
                        // do operations specific to this selection
                        break;
                    case R.id.rb_technician:
                        binding.vToggle.setVisibility(View.GONE);
                        radio_txt = "technician";
                        break;
                    case R.id.rb_communit_owner:
                        binding.vToggle.setVisibility(View.GONE);
                        radio_txt = "owner";
                        break;
                    case R.id.rb_cab_owner:
                        binding.vToggle.setVisibility(View.GONE);
                        radio_txt = "driver";
                        break;

                }
            }
        });

        binding.bedSwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                //Log.d(TAG, "Now Selected : " + direction.name() + ", Current Text : " + text);
                //CUtils.showToastShort(mContext, "" + direction.name());
                if (direction.name().equalsIgnoreCase("Right")) {
                    toggle_txt = "non-certified";
                } else {
                    toggle_txt = "certified";
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSignup) {
            validateform();
        } else if (v == binding.ivBack) {
            onBackPressed();
        } else if (v == binding.tvSignin) {
            Intent i = new Intent(mContext, SignIn.class);
            startActivity(i);
        }
    }

    private void validateform() {
        if (!TextUtils.isEmpty(binding.etPhoneno.getText().toString().trim()) && binding.etPhoneno.getText().toString().trim().length() >= 10) {
            if (!TextUtils.isEmpty(binding.etPassword.getText().toString().trim()) && binding.etPassword.getText().toString().trim().length() > 6) {
                if (!TextUtils.isEmpty(st_rid)) {
                    RegisterPojo registerPojo = new RegisterPojo();
                    registerPojo.setCountryid(country_id);
                    registerPojo.setDocumentno("");
                    registerPojo.setName("Test name");
                    registerPojo.setContactno(binding.etPhoneno.getText().toString().trim());
                    registerPojo.setEmailid(binding.etEmail.getText().toString().trim());
                    registerPojo.setPassword(binding.etPassword.getText().toString().trim());
                    registerPojo.setRoleid(st_rid);
                    registerPojo.setLatitude("0.00");
                    registerPojo.setLongitude("0.00");
                    registerPojo.setDeviceid(CUtils.getdeviceid(mContext));
                    registerPojo.setDevicetype("A");
                    PostRegistrationData(registerPojo);
                    st_password = binding.etPassword.getText().toString().trim();
                } else {
                    binding.etPassword.clearFocus();
                    binding.etPhoneno.clearFocus();
                    CUtils.showToastLong(mContext, "Please select your role");
                }
            } else {
                binding.etPassword.requestFocus();
                CUtils.showToastShort(mContext, "Please must be greater than 6 digits");
            }
        } else {
            binding.etPhoneno.requestFocus();
            CUtils.showToastShort(mContext, "Please enter valid phone no.");

        }

    }

    @Override
    public void onGpsSettingStatus(boolean enabled) {

    }

    @Override
    public void onGpsAlertCanceledByUser() {
        finish();
        CUtils.showToastShort(mContext, "Can not Proceed without GPS ON");
    }


    private void getJsonRoles() {
        pdialog.show();
        String BASE_URL = getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<GetUserRole> call = redditAPI.GetUserRole();
        call.enqueue(new Callback<GetUserRole>() {

            @Override
            public void onResponse(Call<GetUserRole> call, retrofit2.Response<GetUserRole> response) {
                if (response.isSuccessful()) {
                    rList = response.body();
                    if (!rList.getStatus().equalsIgnoreCase("0")) {
                        if (rList.getData().size() > 0) {
                            role_name = new String[rList.getData().size() + 1];
                            role_id = new String[rList.getData().size() + 1];
                            role_name[0] = "Select Role";
                            role_id[0] = "";

                            for (int i = 0; i < rList.getData().size(); i++) {
                                role_name[i + 1] = rList.getData().get(i).getRolename();
                                role_id[i + 1] = rList.getData().get(i).getRoleid();
                            }
                            inflatespinner();
                        }
                    }else {
                        CUtils.showToastShort(mContext,rList.getMessage());
                    }
                }
                pdialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetUserRole> call, Throwable t) {
                pdialog.dismiss();
            }
        });
    }

    private void inflatespinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_textview, role_name);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.spinnerCustom.setAdapter(spinnerArrayAdapter);

        binding.spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    st_rid = rList.getData().get(i - 1).getRoleid();
                } else {
                    st_rid = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void PostRegistrationData(RegisterPojo registerPojo) {
        pdialog.show();
        String BASE_URL = getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<RegisterResponse> call = redditAPI.CreateRegistration(registerPojo);
        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    RegisterResponse rp = response.body();
                    if (rp.getStatus().equalsIgnoreCase("1")) {
                        Intent i = new Intent(mContext, ActivtyOtp.class);
                        i.putExtra("from", "ActivityRegisterAfterScan");
                        i.putExtra("otp", rp.getData().get(0).getOtp());
                        i.putExtra("user_id", rp.getData().get(0).getUserid());
                        i.putExtra("phoneno", rp.getData().get(0).getContactno());
                        i.putExtra("email", rp.getData().get(0).getEmailid());
                        i.putExtra("country_id", rp.getData().get(0).getCountry());
                        i.putExtra("password", st_password);
                        i.putExtra("st_rid", st_rid);
                        startActivity(i);
                    } else {
                        CUtils.showToastLong(mContext, rp.getMessage());
                    }
                }
                pdialog.dismiss();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                pdialog.dismiss();
            }
        });
    }

}
