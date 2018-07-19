package com.app.carcharging;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.carcharging.helper.OnInfoWindowElemTouchListener;
import com.app.carcharging.pojo.InfoWindowData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class CustInfoWindowMap implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    private OnInfoWindowElemTouchListener infoButtonListener;

    public CustInfoWindowMap(Context ctx) {
        mContext = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.custom_map_window, null);
        LatLng latLng = marker.getPosition();
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        TextView tvLat = v.findViewById(R.id.tv_name);
        TextView tvLng = v.findViewById(R.id.tv_ratings);
        TextView tv_distance = v.findViewById(R.id.tv_distance);
        Button btn_go = v.findViewById(R.id.btn_go);
        tvLat.setText(infoWindowData.getCentername());
        tvLng.setText(infoWindowData.getRatings_val());
        tv_distance.setText(infoWindowData.getDistance() + ", distance");
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
