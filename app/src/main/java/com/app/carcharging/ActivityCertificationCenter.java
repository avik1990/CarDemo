package com.app.carcharging;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.app.carcharging.helper.CUtils;
import com.app.carcharging.helper.ConnectionDetector;
import com.app.carcharging.pojo.CertificationMap;
import com.app.carcharging.pojo.InfoWindowData;
import com.app.carcharging.retrofit.api.ApiServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityCertificationCenter extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    ConnectionDetector cd;
    Context mContext;
    ProgressDialog pDialog;
    CertificationMap certificationMap;
    private List<LatLng> bangaloreRoute = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        mContext = this;
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        cd = new ConnectionDetector(mContext);

    }

    private void fetchdata() {
        pDialog.show();
        String BASE_URL = "http://www.json-generator.com/api/json/get/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<List<CertificationMap>> call = redditAPI.getMarkers("2");
        call.enqueue(new Callback<List<CertificationMap>>() {

            @Override
            public void onResponse(Call<List<CertificationMap>> call, retrofit2.Response<List<CertificationMap>> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    List<CertificationMap> rs = response.body();
                    if (rs.size() > 0) {
                       // CUtils.showToastShort(mContext, "" + rs.size());
                        mMap.clear();
                        bangaloreRoute.clear();
                        for (int i = 0; i < rs.size(); i++) {
                            double lat = Double.parseDouble(rs.get(i).getLatitude());
                            double lng = Double.parseDouble(rs.get(i).getLongitude());
                            bangaloreRoute.add(new LatLng(lat, lng));
                            InfoWindowData mapdata = new InfoWindowData();
                            mapdata.set_id(rs.get(i).get_id());
                            mapdata.setLatitude(rs.get(i).getLatitude());
                            mapdata.setLongitude(rs.get(i).getLongitude());
                            mapdata.setCentername(rs.get(i).getCentername());
                            mapdata.setRatings_val(rs.get(i).getRatings_val());
                            mapdata.setDistance(rs.get(i).getDistance());

                            LatLng latLng = new LatLng(lat, lng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_demo_markers));
                            markerOptions.getPosition();
                            /*markerOptions.snippet("Population: 776733");
                            CUtils.showToastShort(mContext, "" + markerOptions.getPosition());*/
                            //mMap.addMarker(markerOptions);

                            Marker m = mMap.addMarker(markerOptions);
                            m.setTag(mapdata);
                            //m.showInfoWindow();
                            // m.setOnc
                        }
                        if(bangaloreRoute.size()>0){
                            zoomRoute(mMap, bangaloreRoute);
                        }

                        final CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(mContext);
                        mMap.setInfoWindowAdapter(customInfoWindow);
                    }
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<CertificationMap>> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {
        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);
        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    private void drawmarkerstomap(Double latitude, Double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_demo_markers));
        markerOptions.getPosition();

        mMap.addMarker(markerOptions);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        if (cd.isConnected()) {
            fetchdata();
        } else {
            CUtils.showToastShort(mContext, "No Internet Connection");
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getBaseContext(),
                "Info Window clicked@" + marker.getPosition(),
                Toast.LENGTH_SHORT).show();

        Intent i = new Intent(mContext, ActivityCertificationNavigation.class);
        i.putExtra("coordinates", "" + marker.getPosition());
        startActivity(i);

    }
}

