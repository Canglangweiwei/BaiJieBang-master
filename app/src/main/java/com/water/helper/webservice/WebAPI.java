package com.water.helper.webservice;

import com.water.helper.bean.BaseBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WebAPI {

    // 登录接口
    @FormUrlEncoded
    @POST("common/login.php")
    Call<Map<String, Object>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("test/testJson1.php")
    Call<BaseBean> testJson(@FieldMap Map<String, String> params);
}
