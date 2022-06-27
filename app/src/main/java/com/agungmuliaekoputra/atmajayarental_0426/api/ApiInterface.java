package com.agungmuliaekoputra.atmajayarental_0426.api;


import com.agungmuliaekoputra.atmajayarental_0426.DriverActivity;
import com.agungmuliaekoputra.atmajayarental_0426.adapters.LoginRequest;
import com.agungmuliaekoputra.atmajayarental_0426.models.Driver;
import com.agungmuliaekoputra.atmajayarental_0426.models.DriverResponse;
import com.agungmuliaekoputra.atmajayarental_0426.models.LoginResponse;
import com.agungmuliaekoputra.atmajayarental_0426.models.MobilResponse;
import com.agungmuliaekoputra.atmajayarental_0426.models.Transaksi;
import com.agungmuliaekoputra.atmajayarental_0426.models.TransaksiResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @Headers({"Accept: application/json"})
    @GET("transaksiByCustomer/{id}")
    Call<TransaksiResponse> getTransaksiByCustomer(@Path("id") int id,@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("transaksiByDriver/{id}")
    Call<TransaksiResponse> getTransaksiByDriver(@Path("id") int id,@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("pendapatan")
    Call<TransaksiResponse> getPendapatan(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("jumlahPendapatan")
    Call<TransaksiResponse> getPenyewaanMobil(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("promoTransaksi")
    Call<TransaksiResponse> getPromoTransaksi(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("brosur")
    Call<MobilResponse> getBrosur(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("topDriver")
    Call<TransaksiResponse> getTopDriver(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("topCustomer")
    Call<TransaksiResponse> getTopCustomer(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("topDriver")
    Call<TransaksiResponse> getPerformaDriver(@Header("Authorization") String token);


    @POST("login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @Headers({"Accept: application/json"})
    @GET("transaksiMobile/{id}")
    Call<TransaksiResponse> getTransaksiById(@Path("id") int id,@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @PUT("rating/{id}")
    Call<TransaksiResponse> updateTransaksi(@Path("id") int ID_TRANSAKSI,
                                            @Body Transaksi transaksi,
                                            @Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("driverByTransaksi/{id}")
    Call<TransaksiResponse> getDriverById(@Path("id") int id, @Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @PUT("driverMobile/{id}")
    Call<DriverResponse> updateDriver(@Path("id") int ID_DRIVER,
                                      @Body Driver driver,
                                      @Header("Authorization") String token);




}
