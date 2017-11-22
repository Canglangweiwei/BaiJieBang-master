package com.water.helper.config.contract;

import com.water.helper.base.BasePresenter;
import com.water.helper.base.BaseView;
import com.water.helper.bean.UserBean;

@SuppressWarnings("ALL")
public interface LoginContract {

    interface View extends BaseView {

        void getUserinfo(UserBean userBean);
    }

    interface Presenter extends BasePresenter<View> {
        void login(String username, String password);
    }
}