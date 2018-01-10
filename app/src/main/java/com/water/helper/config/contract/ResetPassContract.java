package com.water.helper.config.contract;

import com.water.helper.base.BasePresenter;
import com.water.helper.base.BaseView;

/**
 * 用户校验
 */
@SuppressWarnings("ALL")
public interface ResetPassContract {

    interface View extends BaseView {
        void getResetPassResult(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void resetPass(String username, String old_pass, String new_pass);
    }
}