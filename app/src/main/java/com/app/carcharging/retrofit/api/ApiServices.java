package com.app.carcharging.retrofit.api;

import com.app.carcharging.pojo.CertificateVeriPost;
import com.app.carcharging.pojo.CertificateVeriResponse;
import com.app.carcharging.pojo.CertificationMap;
import com.app.carcharging.pojo.Country;
import com.app.carcharging.pojo.GetCarManufacturer;
import com.app.carcharging.pojo.GetCarModelFilterSet;
import com.app.carcharging.pojo.GetCarModelFilterSetPost;
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

    @POST("NewCar/GetCarManufacturer")
    Call<GetCarManufacturer> getCarManufacturer();

    @POST("NewCar/GetCarModelFilterSet")
    Call<GetCarModelFilterSet> GetCarModelFilterSet(@Body GetCarModelFilterSetPost GetCarModelFilterSetPost);

    @POST("Certificate/VerifyCertificate")
    Call<CertificateVeriResponse> VerifyCertificate(@Body CertificateVeriPost otpPost);
}
