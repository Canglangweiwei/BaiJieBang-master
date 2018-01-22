package com.water.helper.config.presenter;

import android.support.annotation.NonNull;

import com.water.helper.bean.BaseModel;
import com.water.helper.config.contract.JxInputContract;
import com.water.helper.config.httpclient.HttpPost;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * <p>
 * 绩效录入
 * </p>
 * Created by Administrator on 2018/1/18 0018.
 */
public class JxInputPresenter implements JxInputContract.Presenter {

    private JxInputContract.View view;

    @Inject
    public JxInputPresenter() {

    }

    @Override
    public void add(String name, String days, String bans, String date) {
        HttpPost httpPost = new HttpPost();
        httpPost.jxInput(name, days, bans, date, new Subscriber<BaseModel>() {
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
                view.addSuccess(baseModel.getData());
            }
        });
    }

    @Override
    public void attachView(@NonNull JxInputContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
