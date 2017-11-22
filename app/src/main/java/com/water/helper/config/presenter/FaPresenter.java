package com.water.helper.config.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.basebean.BaseResponse;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.FaResultBean;
import com.water.helper.bean.HotelBean;
import com.water.helper.config.contract.FaContract;
import com.water.helper.config.httpclient.HttpPost;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

@SuppressWarnings("ALL")
public class FaPresenter implements FaContract.Presenter {

    private FaContract.View view;

    @Inject
    public FaPresenter() {

    }

    @Override
    public void getBinGuanInfo() {
        HttpPost httpPost = new HttpPost();
        httpPost.Get_BinGuanInfo(new Subscriber<BaseResponse<List<HotelBean>>>() {

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
    public void getList(String bName, String selectDate, String username) {
        HttpPost httpPost = new HttpPost();
        httpPost.Get_FahuoList(bName, selectDate, username, new Subscriber<BaseModel>() {

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
                    view.onFailureCallback(1001, "发货列表加载失败");
                    return;
                }
                if (!baseModel.isSuccess()) {
                    int code = baseModel.getCode();
                    String message = baseModel.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }

                Gson mGson = new Gson();
                Type type = new TypeToken<FaResultBean>() {
                }.getType();
                FaResultBean fBean = mGson.fromJson(baseModel.getData(), type);
                // 解析收货统计列表
                view.getFaList(fBean);
            }
        });
    }

    @Override
    public void attachView(@NonNull FaContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
