package com.water.helper.config.httpclient;

import com.jaydenxiao.common.basebean.BaseResponse;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.HotelLzBean;
import com.water.helper.config.RetrofitFactory;

import java.util.List;

import rx.Subscriber;

public class HttpPost {

    private RetrofitFactory retrofitFactory;

    public HttpPost() {
        super();
        retrofitFactory = RetrofitFactory.get();
    }

    /**
     * 获取版本信息
     */
    public void Get_Version(Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().Get_Version(), subscriber);
    }

    /**
     * 登录
     */
    public void login(String username, String password, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().userLogin(username, password), subscriber);
    }

    /**
     * 宾馆信息
     */
    public void Get_BinGuanInfo(String a_id, Subscriber<BaseResponse<List<HotelBean>>> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().Get_BinguanInfo(a_id), subscriber);
    }

    /**
     * 楼层信息
     */
    public void Get_LoucengInfo(int hid, Subscriber<BaseResponse<List<HotelLzBean>>> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().Get_LoucengInfo(hid), subscriber);
    }

    /**
     * 收货列表
     */
    public void Get_ShouhuoList(int hid, String selectDate, String username, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().Get_ShouhuoList(hid, selectDate, username), subscriber);
    }

    /**
     * 送货列表
     */
    public void Get_FahuoList(int hid, String selectDate, String username, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().Get_FahuoList(hid, selectDate, username), subscriber);
    }

    /**
     * 收货类型
     */
    public void Get_AddType(int hid, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().Get_AddType(hid), subscriber);
    }

    /**
     * 添加收货信息
     */
    public void Upload(String dataJson, String username, int hid, int lc, String beizhu, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().Upload(dataJson, username, hid, lc, beizhu), subscriber);
    }

    /**
     * 添加新的收货类型
     */
    public void AddNewType(int hotel_id, String type_name, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().AddNewType(hotel_id, type_name), subscriber);
    }

    /**
     * 出库明细
     */
    public void Get_ChuKuMx(int hid, int loc, String rqi, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().Get_ChuKuMx(hid, loc, rqi), subscriber);
    }

    /**
     * 回换
     */
    public void HuiHuan(int cid, String username, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().HuiHuan(cid, username), subscriber);
    }

    /**
     * 验证用户信息
     */
    public void checkUinfo(String username, String hotels, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().checkUser(username, hotels), subscriber);
    }

    /**
     * 修改密码
     */
    public void resetPass(String username, String old_pass, String new_pass, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().resetPass(username, old_pass, new_pass), subscriber);
    }

    /**
     * 绩效录入
     */
    public void jxInput(String name, String days, String bans, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().jxInput(name, days, bans), subscriber);
    }

    /**
     * 成员列表
     */
    public void members(String searchname, int pageindex, Subscriber<BaseModel> subscriber) {
        retrofitFactory.httpSubscribe(retrofitFactory.getApiService().members(searchname, pageindex), subscriber);
    }
}
