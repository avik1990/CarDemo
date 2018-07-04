package com.app.carcharging.retrofit.api;

import com.app.carcharging.pojo.CertificationMap;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiServices {

    @GET("cqvILfEqcy")
    Call<List<CertificationMap>> getMarkers(@Query("indent") String indent);
}
