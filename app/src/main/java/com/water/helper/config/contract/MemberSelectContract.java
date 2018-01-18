package com.water.helper.config.contract;

import com.water.helper.base.BasePresenter;
import com.water.helper.base.BaseView;
import com.water.helper.bean.MemberBean;

import java.util.List;

/**
 * 绩效录入
 */
@SuppressWarnings("ALL")
public interface MemberSelectContract {

    interface View extends BaseView {
        void membersList(List<MemberBean> membersList);
    }

    interface Presenter extends BasePresenter<View> {
        void membersList(String searchname, int pageindex);
    }
}