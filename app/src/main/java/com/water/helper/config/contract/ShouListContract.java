package com.water.helper.config.contract;

import com.water.helper.base.BasePresenter;
import com.water.helper.base.BaseView;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.ShouResultBean;

import java.util.List;

@SuppressWarnings("ALL")
public interface ShouListContract {

    interface View extends BaseView {

        void getBinGuanInfo(List<HotelBean> bins);

        void getShouList(ShouResultBean resultBean);
    }

    interface Presenter extends BasePresenter<View> {
        void getBinGuanInfo(String a_id);

        void getList(int hid, String selectDate, String username);
    }
}