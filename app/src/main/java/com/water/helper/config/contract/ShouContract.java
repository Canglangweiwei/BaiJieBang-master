package com.water.helper.config.contract;

import com.water.helper.base.BasePresenter;
import com.water.helper.base.BaseView;
import com.water.helper.bean.GoodsModel;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.HotelLzBean;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public interface ShouContract {

    interface View extends BaseView {

        void getBinGuanInfo(List<HotelBean> bins);

        void getLoucInfo(List<HotelLzBean> lous);

        void getAddType(ArrayList<GoodsModel> goodsModels);

        void add(String message);

        void addNewType(GoodsModel model);
    }

    interface Presenter extends BasePresenter<View> {

        void getBinGuanInfo(String a_id);

        void getLoucInfo(int hid);

        void getAddType(int hid);

        void add(String dataJson, String username, int hid, int lc, String beizhu);

        void addNewType(int hotel_id, String type_name);
    }
}