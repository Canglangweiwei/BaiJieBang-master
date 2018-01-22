package com.water.helper.config.contract;

import com.water.helper.base.BasePresenter;
import com.water.helper.base.BaseView;

/**
 * 绩效录入
 */
@SuppressWarnings("ALL")
public interface JxInputContract {

    interface View extends BaseView {
        void addSuccess(String message);
    }

    interface Presenter extends BasePresenter<View> {

        void add(String name, String days, String bans, String date);
    }
}