package com.water.helper.config.contract;

import com.water.helper.base.BasePresenter;
import com.water.helper.base.BaseView;
import com.water.helper.bean.ChuKuMxBean;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.HotelLzBean;

import java.util.List;

/**
 * 回换管理
 */
@SuppressWarnings("ALL")
public interface BackWashContract {

    interface View extends BaseView {
        void getBinGuanInfo(List<HotelBean> bins);

        void getLoucInfo(List<HotelLzBean> lous);

        void getChuKuMxInfo(List<ChuKuMxBean> chuKuMxList);

        void huiHuanSuccess(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void getBinGuanInfo(String a_id);

        void getLoucInfo(int hid);

        void getChuKuMx(int hid, int loc, String rqi);

        void doHuiHuan(int cid);
    }
}