package com.app.carcharging;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LoginOptions extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginoptions);

        mContext = this;
       /* CUtils.checkPermissions(mContext);
        CUtils.getdeviceid(mContext);*/
    }
}