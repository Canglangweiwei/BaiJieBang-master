package com.water.helper.config.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.UserBean;
import com.water.helper.config.contract.LoginContract;
import com.water.helper.config.httpclient.HttpPost;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * 用户登录
 */
@SuppressWarnings("ALL")
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    private Gson mGson = new Gson();

    @Inject
    public LoginPresenter() {

    }

    @Override
    public void login(String username, String password) {
        HttpPost httpPost = new HttpPost();
        httpPost.login(username, password, new Subscriber<BaseModel>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailureCallback(1001, "请检查网络连接");
                    }

                    @Override
                    public void onNext(BaseModel loginResultBean) {
                        if (null == loginResultBean) {
                            view.onFailureCallback(1001, "用户信息获取失败");
                            return;
                        }
                        if (!loginResultBean.isSuccess()) {
                            int code = loginResultBean.getCode();
                            String message = loginResultBean.getMessage();
                            view.onFailureCallback(code, message);
                            return;
                        }
                        UserBean userBean = mGson.fromJson(loginResultBean.getData(), UserBean.class);
                        // 解析用户信息
                        view.getUserinfo(userBean);
                    }
                }
        );
    }

    @Override
    public void attachView(@NonNull LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
