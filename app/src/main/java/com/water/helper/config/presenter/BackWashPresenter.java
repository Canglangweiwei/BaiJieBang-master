package com.water.helper.config.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.basebean.BaseResponse;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.ChuKuMxBean;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.HotelLzBean;
import com.water.helper.config.contract.BackWashContract;
import com.water.helper.config.httpclient.HttpPost;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * 收货管理
 */
@SuppressWarnings("ALL")
public class BackWashPresenter implements BackWashContract.Presenter {

    private BackWashContract.View view;

    private Gson mGson = new Gson();

    @Inject
    public BackWashPresenter() {

    }

    /**
     * 获取宾馆信息
     *
     * @param a_id 小区id
     */
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

    /**
     * 获取楼层信息
     *
     * @param hid 宾馆id
     */
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

    /**
     * 获取出库明细
     *
     * @param hid 宾馆
     * @param loc 楼层
     * @param rqi 日期
     */
    @Override
    public void getChuKuMx(int hid, int loc, String rqi) {
        HttpPost httpPost = new HttpPost();
        httpPost.Get_ChuKuMx(hid, loc, rqi, new Subscriber<BaseModel>() {

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
                    view.onFailureCallback(1002, "楼层信息获取失败");
                    return;
                }
                if (!baseModel.isSuccess()) {
                    int code = baseModel.getCode();
                    String message = baseModel.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }
                Type type = new TypeToken<List<ChuKuMxBean>>() {
                }.getType();
                ArrayList<ChuKuMxBean> chuKuMxBeen = mGson.fromJson(baseModel.getData(), type);
                // 解析出库明细信息
                view.getChuKuMxInfo(chuKuMxBeen);
            }
        });
    }

    /**
     * 回换
     *
     * @param cid 出库id
     */
    @Override
    public void doHuiHuan(int cid) {
        HttpPost httpPost = new HttpPost();
        httpPost.HuiHuan(cid, new Subscriber<BaseModel>() {

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
                    view.onFailureCallback(1002, "操作失败");
                    return;
                }
                if (!baseModel.isSuccess()) {
                    int code = baseModel.getCode();
                    String message = baseModel.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }
                // 回换成功
                view.huiHuanSuccess(baseModel.getData());
            }
        });
    }

    @Override
    public void attachView(@NonNull BackWashContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
