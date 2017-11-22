package com.water.helper.config.httpclient;

import com.jaydenxiao.common.basebean.BaseResponse;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.HotelLzBean;
import com.water.helper.config.RetrofitFactory;

import java.util.List;

import rx.Subscriber;

public class HttpPost {

    private RetrofitFactory httpRetrofit;

    public HttpPost() {
        httpRetrofit = RetrofitFactory.get();
    }

    /**
     * 获取版本信息
     */
    public void Get_Version(Subscriber<BaseModel> subscriber) {
        httpRetrofit.httpSubscribe(httpRetrofit.getApiService().Get_Version(), subscriber);
    }

    /**
     * 登录
     */
    public void login(String username, String password, Subscriber<BaseModel> subscriber) {
        httpRetrofit.httpSubscribe(httpRetrofit.getApiService().userLogin(username, password), subscriber);
    }

    /**
     * 宾馆信息
     */
    public void Get_BinGuanInfo(Subscriber<BaseResponse<List<HotelBean>>> subscriber) {
        httpRetrofit.httpSubscribe(httpRetrofit.getApiService().Get_BinguanInfo(), subscriber);
    }

    /**
     * 楼层信息
     */
    public void Get_LoucengInfo(int hid, Subscriber<BaseResponse<List<HotelLzBean>>> subscriber) {
        httpRetrofit.httpSubscribe(httpRetrofit.getApiService().Get_LoucengInfo(hid), subscriber);
    }

    /**
     * 收货列表
     */
    public void Get_ShouhuoList(int hid, String selectDate, String username, Subscriber<BaseModel> subscriber) {
        httpRetrofit.httpSubscribe(httpRetrofit.getApiService().Get_ShouhuoList(hid, selectDate, username), subscriber);
    }

    /**
     * 送货列表
     */
    public void Get_FahuoList(String bName, String selectDate, String username, Subscriber<BaseModel> subscriber) {
        httpRetrofit.httpSubscribe(httpRetrofit.getApiService().Get_FahuoList(bName, selectDate, username), subscriber);
    }

    /**
     * 收货类型
     */
    public void Get_AddType(int hid, Subscriber<BaseModel> subscriber) {
        httpRetrofit.httpSubscribe(httpRetrofit.getApiService().Get_AddType(hid), subscriber);
    }

    /**
     * 添加收货信息
     */
    public void Upload(String dataJson, String username, int hid, int lc, String beizhu, Subscriber<BaseModel> subscriber) {
        httpRetrofit.httpSubscribe(httpRetrofit.getApiService().Upload(dataJson, username, hid, lc, beizhu), subscriber);
    }
}
