package com.app.carcharging.pojo;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CertificationMap extends BaseObservable {

    @SerializedName("distance")
    @Expose
    String distance;

    @SerializedName("longitude")
    @Expose
    String longitude;

    @SerializedName("latitude")
    @Expose
    String latitude;

    @SerializedName("ratings_val")
    @Expose
    String ratings_val;

    @SerializedName("_id")
    @Expose
    String _id;

    @SerializedName("centername")
    @Expose
    String centername;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getRatings_val() {
        return ratings_val;
    }

    public void setRatings_val(String ratings_val) {
        this.ratings_val = ratings_val;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCentername() {
        return centername;
    }

    public void setCentername(String centername) {
        this.centername = centername;
    }
}
