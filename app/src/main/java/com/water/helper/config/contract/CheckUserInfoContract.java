package com.water.helper.config.contract;

import com.water.helper.base.BasePresenter;
import com.water.helper.base.BaseView;
import com.water.helper.bean.HotelBean;

import java.util.List;

/**
 * 用户校验
 */
@SuppressWarnings("ALL")
public interface CheckUserInfoContract {

    interface View extends BaseView {
        void getBinGuanInfo(List<HotelBean> bins);

        void getCheckUinfoResult(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void getBinGuanInfo(String a_id);

        void checkUinfo(String username, String hotels);
    }
}