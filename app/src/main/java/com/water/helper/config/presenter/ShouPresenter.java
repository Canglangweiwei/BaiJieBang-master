package com.water.helper.config.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.basebean.BaseResponse;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.GoodsModel;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.HotelLzBean;
import com.water.helper.config.contract.ShouContract;
import com.water.helper.config.httpclient.HttpPost;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * 用户登录
 */
@SuppressWarnings("ALL")
public class ShouPresenter implements ShouContract.Presenter {

    private ShouContract.View view;

    @Inject
    public ShouPresenter() {

    }

    @Override
    public void getBinGuanInfo(String a_id) {
        HttpPost httpPost = new HttpPost();
        httpPost.Get_BinGuanInfo(a_id, new Subscriber<BaseResponse<List<HotelBean>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.onFailureCallback(e);
            }

            @Override
            public void onNext(BaseResponse<List<HotelBean>> binGuanBean) {
                if (binGuanBean == null) {
                    view.onFailureCallback(1001, "宾馆信息获取失败");
                    return;
                }
                if (!binGuanBean.isSuccess()) {
                    int code = binGuanBean.getCode();
                    String message = binGuanBean.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }
                // 解析宾馆信息
                view.getBinGuanInfo(binGuanBean.getData());
            }
        });
    }

    @Override
    public void getLoucInfo(int hid) {
        HttpPost httpPost = new HttpPost();
        httpPost.Get_LoucengInfo(hid, new Subscriber<BaseResponse<List<HotelLzBean>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.onFailureCallback(e);
            }

            @Override
            public void onNext(BaseResponse<List<HotelLzBean>> loucBean) {
                if (loucBean == null) {
                    view.onFailureCallback(1002, "楼层信息获取失败");
                    return;
                }
                if (!loucBean.isSuccess()) {
                    int code = loucBean.getCode();
                    String message = loucBean.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }
                // 解析宾馆信息
                view.getLoucInfo(loucBean.getData());
            }
        });
    }

    @Override
    public void getAddType(int hid) {
        HttpPost httpPost = new HttpPost();
        httpPost.Get_AddType(hid, new Subscriber<BaseModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.onFailureCallback(e);
            }

            @Override
            public void onNext(BaseModel baseModel) {
                if (baseModel == null) {
                    view.onFailureCallback(1002, "收货类型信息获取失败");
                    return;
                }
                if (!baseModel.isSuccess()) {
                    int code = baseModel.getCode();
                    String message = baseModel.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }

                Gson mGson = new Gson();
                Type type = new TypeToken<List<GoodsModel>>() {
                }.getType();
                List<GoodsModel> goodsModels = mGson.fromJson(baseModel.getData(), type);

                // 解析收货类型
                view.getAddType(goodsModels);
            }
        });
    }

    @Override
    public void add(String dataJson, String username, int hid, int lc, String beizhu) {
        HttpPost httpPost = new HttpPost();
        httpPost.Upload(dataJson, username, hid, lc, beizhu, new Subscriber<BaseModel>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.onFailureCallback(e);
            }

            @Override
            public void onNext(BaseModel baseModel) {
                if (baseModel == null) {
                    view.onFailureCallback(1002, "收货信息提交失败");
                    return;
                }
                if (!baseModel.isSuccess()) {
                    int code = baseModel.getCode();
                    String message = baseModel.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }
                // 解析收货类型
                view.add(baseModel.getData());
            }
        });
    }

    @Override
    public void attachView(@NonNull ShouContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
