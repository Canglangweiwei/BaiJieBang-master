package com.water.helper.webservice;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface WebAPI {

    // 登录接口
    @FormUrlEncoded
    @POST("user/userLogin")
    Call<Map<String, Object>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @PUT("user/register")
    Call<Map<String, Object>> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common!uploadLog.action")
    Call<Map<String, Object>> uploadLog(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("app/public!versionUp.action")
    Call<Map<String, Object>> getVersionUp(@FieldMap Map<String, String> params);
}
