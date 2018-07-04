package com.app.carcharging;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.app.carcharging.databinding.ActivityCertificationnavigationBinding;
import com.app.carcharging.helper.CUtils;
import com.app.carcharging.helper.ConnectionDetector;
import com.app.carcharging.helper.DirectionsJSONParser;
import com.app.carcharging.helper.MapAnimator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.alexandroid.gps.GpsStatusDetector;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ActivityCertificationNavigation extends FragmentActivity implements
        OnMapReadyCallback,
        View.OnClickListener,
        GpsStatusDetector.GpsStatusDetectorCallBack, com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    ConnectionDetector cd;
    Context mContext;
    ProgressDialog pDialog;
    ActivityCertificationnavigationBinding binding;
    private GpsStatusDetector mGpsStatusDetector;
    private List<LatLng> bangaloreRoute = new ArrayList<>();
    private static final LatLng POINT_A = new LatLng(22.4989311, 88.2968939);
    LatLng POINT_B;
    private static final int PERMISSION_REQUEST_CODE = 200;
    boolean boolpathdrawn = false;
    boolean gpssettings = false;

    //////////////////////////////
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    LocationRequest mLocationRequest;
    private static final long INTERVAL = 0;
    private static final long FASTEST_INTERVAL = 0;
    double c_lat = 0.0, c_lng = 0.0;

    /////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initview();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        if (!checkPermission()) {
            requestPermission();
        } else {

        }

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    //boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean access_fine_location = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (access_fine_location) {
                        // CUtils.showToastShort(mContext, "Permission granted");
                        if (mGoogleApiClient.isConnected()) {
                            startLocationUpdates();
                        }
                        if (cd.isConnected()) {
                            pDialog.show();
                            collectlatlng();
                        } else {
                            CUtils.showToastShort(mContext, "No Internet Connection");
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION},
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

    private void startLocationUpdates() {
        @SuppressLint("MissingPermission")
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
    }

    private void initview() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);


        cd = new ConnectionDetector(mContext);

        mGpsStatusDetector = new GpsStatusDetector(this);
        //mGpsStatusDetector.checkGpsStatus();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_certificationnavigation);
        binding.toolbar.tvTitle.setText("Route Map");
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.btnNavigation.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.toolbar.ivBack) {
            onBackPressed();
        } else if (v == binding.btnNavigation) {
            //22.4989311, 88.2968939
            String uri = "http://maps.google.com/maps?saddr=" + c_lat + "," + c_lng + "&daddr=" + "22.4989311" + "," + "88.2968939";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            //intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {
        if (binding.slidingLayout != null &&
                (binding.slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || binding.slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            binding.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
            finish();
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

    private void collectlatlng() {
        String url = getDirectionsUrl(POINT_A, POINT_B);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (null != mCurrentLocation) {
            c_lat = mCurrentLocation.getLatitude();
            c_lng = mCurrentLocation.getLongitude();
            POINT_B = new LatLng(c_lat, c_lng);

            if (!boolpathdrawn) {
                if (gpssettings) {
                    if (!checkPermission()) {
                    } else {
                        if (cd.isConnected()) {
                            if (POINT_B != null) {
                                pDialog.show();
                                collectlatlng();
                                boolpathdrawn = true;
                            }
                        } else {
                            CUtils.showToastShort(mContext, "No Internet Connection");
                        }
                    }
                }
            }
            // Toast.makeText(mContext, "" + POINT_B, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(mContext, "Hello Not Connected", Toast.LENGTH_SHORT).show();
        }

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                //Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception while", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        //Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            bangaloreRoute.clear();
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    bangaloreRoute.add(new LatLng(lat, lng));
                }
                if (bangaloreRoute.size() > 0) {
                    startAnim();
                    zoomRoute(mMap, bangaloreRoute);
                }
            }
            pDialog.dismiss();
        }
    }

    private void startAnim() {
        if (mMap != null) {
            MapAnimator.getInstance().animateRoute(mMap, bangaloreRoute);
            dropPinEffect(mMap.addMarker(new MarkerOptions().position(POINT_B).title("Drop Point")));
            dropPinEffect(mMap.addMarker(new MarkerOptions().position(POINT_A).title("Pick Point")));
        } else {
            //Toast.makeText(getApplicationContext(), "Map not ready", Toast.LENGTH_LONG).show();
        }
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

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (gpssettings) {
            mGoogleApiClient.connect();
            onLocationChanged(mCurrentLocation);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isGpsEnabled(ActivityCertificationNavigation.this)) {
            gpssettings = false;
            mGpsStatusDetector.checkGpsStatus();
        } else {
            gpssettings = true;
        }
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    private boolean isGpsEnabled(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}

