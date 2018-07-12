package com.app.carcharging.retrofit.api;

import com.app.carcharging.pojo.CertificationMap;
import com.app.carcharging.pojo.Country;
import com.app.carcharging.pojo.GetUserRole;
import com.app.carcharging.pojo.OtpPost;
import com.app.carcharging.pojo.RegisterPojo;
import com.app.carcharging.pojo.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiServices {

    @GET("cqvILfEqcy")
    Call<List<CertificationMap>> getMarkers(@Query("indent") String indent);

    @POST("Country/GetCountry")
    Call<Country> getCountryList();

    @POST("Role/GetUserRole")
    Call<GetUserRole> GetUserRole();

    @POST("User/CreateRegistration")
    Call<RegisterResponse> CreateRegistration(@Body RegisterPojo register);

    @POST("User/UserLogin")
    Call<RegisterResponse> VerifyOTP(@Body OtpPost otpPost);


}
