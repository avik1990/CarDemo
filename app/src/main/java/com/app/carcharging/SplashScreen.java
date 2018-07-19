package com.app.carcharging;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.carcharging.helper.CUtils;

import net.alexandroid.gps.GpsStatusDetector;

import java.util.Arrays;

public class SplashScreen extends AppCompatActivity implements GpsStatusDetector.GpsStatusDetectorCallBack {

    private static final int SPLASH_TIME = 2 * 1000;
    Context mContext;
    boolean LOGIN_flag = false;
    private GpsStatusDetector mGpsStatusDetector;
    private static final int REQUEST_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;
        LOGIN_flag = CUtils.getisVerifiedPreferences(mContext);
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
    }

    @Override
    public void onGpsSettingStatus(boolean enabled) {
        if (enabled) {
            if (LOGIN_flag) {
                goToHomeActivity();
            } else {
                goToLoginActivity();
            }
        } else {
            CUtils.showToastShort(mContext, "Turn on GPS");
        }
    }

    @Override
    public void onGpsAlertCanceledByUser() {
        CUtils.showToastShort(mContext, "Can not Proceed with out GPS ON");

    }

    private void goToHomeActivity() {
        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_TIME);
                    if (CUtils.getRoleid(mContext).equalsIgnoreCase("4")) {
                        if (!CUtils.getisCertiVerifiedPreferences(mContext)) {
                            Intent i = new Intent(mContext, TechnicianVerification.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        } else {
                            Intent i = new Intent(mContext, Dashboard.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    } else {
                        Intent i = new Intent(mContext, Dashboard.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }

    private void goToLoginActivity() {
        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_TIME);
                    Intent i = new Intent(mContext, LoginOptions.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }


   /* public static void checkPermissions(Context mContext) {
        boolean permissionsGranted = checkPermission(PERMISSIONS_REQUIRED, mContext);
        if (permissionsGranted) {
            // Toast.makeText(this, "You've granted all required permissions!", Toast.LENGTH_SHORT).show();
        } else {
            boolean showRationale = true;
            for (String permission : PERMISSIONS_REQUIRED) {
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission);
                if (!showRationale) {
                    break;
                }
            }
            ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);
        }
    }*/

    public static boolean checkPermission(String permissions[], Context mContext) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MainActivity", "requestCode: " + requestCode);
        Log.d("MainActivity", "Permissions:" + Arrays.toString(permissions));
        Log.d("MainActivity", "grantResults: " + Arrays.toString(grantResults));

        if (requestCode == REQUEST_PERMISSIONS) {
            boolean hasGrantedPermissions = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasGrantedPermissions = false;
                    break;
                }
            }

            if (!hasGrantedPermissions) {
                finish();
            }

        } else {
            finish();
        }
    }
}
