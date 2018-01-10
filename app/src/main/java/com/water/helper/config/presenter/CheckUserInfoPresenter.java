package com.water.helper.config.presenter;

import android.support.annotation.NonNull;

import com.jaydenxiao.common.basebean.BaseResponse;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.HotelBean;
import com.water.helper.config.contract.CheckUserInfoContract;
import com.water.helper.config.httpclient.HttpPost;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * 收货管理
 */
@SuppressWarnings("ALL")
public class CheckUserInfoPresenter implements CheckUserInfoContract.Presenter {

    private CheckUserInfoContract.View view;

    @Inject
    public CheckUserInfoPresenter() {

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

    @Override
    public void checkUinfo(String username, String hotels) {
        HttpPost httpPost = new HttpPost();
        httpPost.checkUinfo(username, hotels, new Subscriber<BaseModel>() {

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
                    view.onFailureCallback(1001, "用户信息校验失败");
                    return;
                }
                if (!baseModel.isSuccess()) {
                    int code = baseModel.getCode();
                    String message = baseModel.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }
                view.getCheckUinfoResult(baseModel.getData());
            }
        });
    }

    @Override
    public void attachView(@NonNull CheckUserInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
