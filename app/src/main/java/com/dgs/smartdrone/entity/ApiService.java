package com.dgs.smartdrone.entity;

import com.dgs.smartdrone.entity.Auth.ResponeLogin;

import dji.thirdparty.retrofit2.Call;
import dji.thirdparty.retrofit2.http.Field;
import dji.thirdparty.retrofit2.http.FormUrlEncoded;
import dji.thirdparty.retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<ResponeLogin> postAuth(@Field("email") String email,
                                @Field("email") String password);;
}