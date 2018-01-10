package com.water.helper.config.presenter;

import android.support.annotation.NonNull;

import com.water.helper.bean.BaseModel;
import com.water.helper.config.contract.ResetPassContract;
import com.water.helper.config.httpclient.HttpPost;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * 修改密码
 */
public class ResetPassPresenter implements ResetPassContract.Presenter {

    private ResetPassContract.View view;

    @Inject
    public ResetPassPresenter() {

    }

    @Override
    public void resetPass(String username, String old_pass, String new_pass) {
        HttpPost httpPost = new HttpPost();
        httpPost.resetPass(username, old_pass, new_pass, new Subscriber<BaseModel>() {

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
                    view.onFailureCallback(1001, "操作失败");
                    return;
                }
                if (!baseModel.isSuccess()) {
                    int code = baseModel.getCode();
                    String message = baseModel.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }
                view.getResetPassResult(baseModel.getData());
            }
        });
    }

    @Override
    public void attachView(@NonNull ResetPassContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
