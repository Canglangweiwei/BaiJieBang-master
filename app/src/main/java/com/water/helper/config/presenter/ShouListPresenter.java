package com.water.helper.config.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.basebean.BaseResponse;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.ShouResultBean;
import com.water.helper.config.contract.ShouListContract;
import com.water.helper.config.httpclient.HttpPost;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * 用户登录
 */
@SuppressWarnings("ALL")
public class ShouListPresenter implements ShouListContract.Presenter {

    private ShouListContract.View view;

    private Gson mGson = new Gson();

    @Inject
    public ShouListPresenter() {

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
    public void getList(int hid, String selectDate, String username) {
        HttpPost httpPost = new HttpPost();
        httpPost.Get_ShouhuoList(hid, selectDate, username, new Subscriber<BaseModel>() {
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
                    view.onFailureCallback(1001, "收货列表加载失败");
                    return;
                }
                if (!baseModel.isSuccess()) {
                    int code = baseModel.getCode();
                    String message = baseModel.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }

                Type type = new TypeToken<ShouResultBean>(){}.getType();
                ShouResultBean sBean = mGson.fromJson(baseModel.getData(), type);
                // 解析收货统计列表
                view.getShouList(sBean);
            }
        });
    }

    @Override
    public void attachView(@NonNull ShouListContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
