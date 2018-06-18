package com.app.carcharging.helper;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

public class CUtils {

    static ConnectionDetector cd;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final String PERMISSIONS_REQUIRED[] = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static void showToastShort(Context mContext, String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastLong(Context mContext, String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static ProgressDialog initializeProgressDialog(Context context) {
        ProgressDialog pDialog;
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("Please wait");
        return pDialog;
    }

    public static void phoneCall(String number, Context mContext) {
        TelephonyManager telMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        switch (simState) {
            case 0:
            case 1:
                showToastShort(mContext, "Call cannot be placed");
                break;

            case TelephonyManager.SIM_STATE_READY:
                try {

                    mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static String getdeviceid(Context mContext) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (!telephonyManager.getDeviceId().equals(null)) {
            }
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }




    /*public static void openNavDrawer(int id, final Context mContext) {
        cd = new ConnectionDetector(mContext);
        if (id == nav_dashboard) {
            *//*if (!(mContext instanceof TripList)) {
                Intent intent = new Intent(mContext, TripList.class);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_in_reverse, R.anim.anim_out_reverse);
            }*//*
            if ((mContext instanceof Dashboard_F)) {
                String dialog_msg;
                if (!KPHashmapUtils.hashmap.containsKey("1") && !KPHashmapUtils.hashmap.containsKey("2")) {
                    // dialog_msg = "Trip is Completed";
                    dialog_msg = "Are you sure to go back?";
                } else {
                    *//*dialog_msg = "Trip is running. Are you sure want to go back?";*//*
                    dialog_msg = "Are you sure to go back?";
                }
                callUpBack(dialog_msg, mContext);
            } else if ((mContext instanceof Dashboard_Log)) {
                String dialog_msg;
                if (KPHashmapUtils.m_route_details1.size() > 0) {
                    //dialog_msg = "Trip is running. Are you sure want to go back?";
                    dialog_msg = "Are you sure to go back?";
                } else {
                    dialog_msg = "Are you sure to go back?";
                    // dialog_msg = "Trip is Completed";
                }
                callUpBack(dialog_msg, mContext);
            } else if ((mContext instanceof TripList)) {
               *//* Intent intent = new Intent(mContext, TripList.class);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_in_reverse, R.anim.anim_out_reverse);*//*
            } else {
                Intent intent = new Intent(mContext, TripList.class);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_in_reverse, R.anim.anim_out_reverse);
            }
        } else if (id == nav_location) {
            if (!(mContext instanceof TrackDriver)) {
                if (!(mContext instanceof TrackDriver)) {
                    Intent intent = new Intent(mContext, TrackDriver.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.anim_in_reverse, R.anim.anim_out_reverse);
                }
            }
        } else if (id == nav_student_details) {
            if (!(mContext instanceof StudentDetails)) {
                Intent intent = new Intent(mContext, StudentDetails.class);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_in_reverse, R.anim.anim_out_reverse);
            }

        } else if (id == nav_logout) {
            if (!KPHashmapUtils.started_flag.contains("1")) {
                showLogoutAlert(mContext, "Are you sure want to logout?", "Logout");
            } else {
                CUtils.showToastLong(mContext, "Trip is already started you can not go offline when trip is ongoing");
            }
        }
    }*/

    /*public static void callUpBack(String dialog_msg, final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Message");
        builder.setMessage(dialog_msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i = new Intent(mContext, TripList.class);
                ((Activity) mContext).startActivity(i);
                ((Activity) mContext).finishAffinity();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(mContext.getResources().getColor(R.color.black));
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(mContext.getResources().getColor(R.color.black));
    }*/

    /*public static void showLogoutAlert(final Context context, String msg, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cleardata(context);
                Intent profileintent = new Intent(context, Login.class);
                context.startActivity(profileintent);
                ((Activity) context).overridePendingTransition(R.anim.anim_in_reverse, R.anim.anim_out_reverse);
                ((Activity) context).finishAffinity();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(context.getResources().getColor(R.color.black));
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(context.getResources().getColor(R.color.black));
    }*/

    /*public static void cleardata(Context mContext) {
        mContext.stopService(new Intent(mContext, BikeTracker.class));
        SharedPreferences settings = mContext.getSharedPreferences("Kppref", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        try {
            KPHashmapUtils.m_update_driver.clear();
            KPHashmapUtils.m_ride_driver_details.clear();
            KPHashmapUtils.m_kid_details_up.clear();
            KPHashmapUtils.m_ride_route_details_up.clear();
            KPHashmapUtils.m_schedule_route_details.clear();
            KPHashmapUtils.m_route_details1.clear();
            KPHashmapUtils.hashmap.clear();
            KPHashmapUtils.m_route_details2.clear();
            KPHashmapUtils.m_kp_kid_details.clear();
            KPHashmapUtils.m_children_name.clear();
            KPHashmapUtils.m_ConRoute.clear();
            KPHashmapUtils.m_routestat.clear();
            KPHashmapUtils.m_kid_log.clear();
            KPHashmapUtils.pick_list_id.clear();
            KPHashmapUtils.started_flag.clear();
        } catch (Exception e) {
        }
    }*/

    public static String getOTP_Text(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String name = preferences.getString("otp_text", "");
        return name;
    }

    public static void setOTP_Text(Context mContext, String cat) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("otp_text", cat);
        editor.commit();
    }

    public static boolean getisVerifiedPreferences(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        boolean flag = loginPreferences.getBoolean("isVerified", false);
        return flag;
    }

    public static void setisVerfiedPreferences(Context mContext, boolean isVerified) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isVerified", isVerified);
        editor.commit();
    }

}
