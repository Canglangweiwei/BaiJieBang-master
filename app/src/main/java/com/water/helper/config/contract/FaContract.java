package com.water.helper.config.contract;

import com.water.helper.base.BasePresenter;
import com.water.helper.base.BaseView;
import com.water.helper.bean.FaResultBean;
import com.water.helper.bean.HotelBean;

import java.util.List;

@SuppressWarnings("ALL")
public interface FaContract {

    interface View extends BaseView {

        void getBinGuanInfo(List<HotelBean> bins);

        void getFaList(FaResultBean resultBean);
    }

    interface Presenter extends BasePresenter<View> {
        void getBinGuanInfo();

        void getList(int hid, String selectDate, String username);
    }
}