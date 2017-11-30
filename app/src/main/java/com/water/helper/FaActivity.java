package com.water.helper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.DateTimeUtil;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.water.helper.adapter.CommonFilterHotelListAdapter;
import com.water.helper.adapter.FaListExpandableListViewAdapter;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.bean.FaResultBean;
import com.water.helper.bean.HotelBean;
import com.water.helper.config.component.DaggerFaPresenterComponent;
import com.water.helper.config.contract.FaContract;
import com.water.helper.config.module.FaModule;
import com.water.helper.config.presenter.FaPresenter;
import com.water.helper.webservice.RequestType;
import com.wevey.selector.dialog.DateType;
import com.wevey.selector.dialog.TimePickerDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>
 * 送货管理
 * </p>
 * Created by Administrator on 2017/10/31 0031.
 */
public class FaActivity extends AbsBaseActivity
        implements FaContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSRLayout;

    @Bind(R.id.add_goods_list_spinner_b)
    Spinner s_bg;   // 宾馆
    @Bind(R.id.tv_tj_date)
    TextView mTvDate;   // 时间

    @Bind(R.id.expandableListView)
    ExpandableListView mExpandableListView;

    private FaListExpandableListViewAdapter mExpandableListViewAdapter;

    private CommonFilterHotelListAdapter adapter;
    private String mSelectedDateTime;
    private int mSelectedHotelID;

    @Inject
    FaPresenter presenter;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_fa;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void setupComponent(AbsAppComponent component) {
        DaggerFaPresenterComponent.builder()
                .absAppComponent(component)
                .faModule(new FaModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initUi() {
        ntb.setTitleText("送货管理");
        mSelectedDateTime = DateTimeUtil.getClientDateFormat("yyyy-MM-dd");
        mTvDate.setText(DateTimeUtil.getClientDateFormat("yyyy-MM-dd"));

        // 初始化页面刷新工具
        initSwipeRefreshLayout();
        // 初始化宾馆菜单
        initBgMenu();
        // 初始化列表
        initExpandListView();
    }

    /**
     * 初始化页面刷新工具
     */
    private void initSwipeRefreshLayout() {
        mSRLayout.setOnRefreshListener(this);
        mSRLayout.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright, android.R.color.holo_green_light);
        mSRLayout.setDistanceToTriggerSync(400);
    }

    /**
     * 初始化宾馆列表菜单
     */
    private void initBgMenu() {
        adapter = new CommonFilterHotelListAdapter();
        s_bg.setAdapter(adapter);
    }

    /**
     * 初始化页面
     */
    private void initExpandListView() {
        mExpandableListViewAdapter = new FaListExpandableListViewAdapter(this);
        mExpandableListView.setAdapter(mExpandableListViewAdapter);
        mExpandableListView.setGroupIndicator(null);

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                                        long id) {
                return false;
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                return false;
            }
        });
    }

    @Override
    protected void initDatas() {
        // 绑定页面
        presenter.attachView(this);
        presenter.getBinGuanInfo(TextUtils.isEmpty(mBaseUserBean.getA_id()) ? "" : mBaseUserBean.getA_id());
    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // 右侧按钮
        ntb.setRightImagSrc(R.drawable.btn_add_goods);
        ntb.setOnRightImagListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startNextActivityForResult(null, ShouActivity.class, 1001);
            }
        });

        // 设置宾馆列表的监听事件
        s_bg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HotelBean hotelBean = (HotelBean) parent.getSelectedItem();
                mSelectedHotelID = hotelBean.getId();
                presenter.getList(mSelectedHotelID, mSelectedDateTime, mBaseUserBean.getUsername());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick({R.id.ll_tj_date, R.id.tv_tj_date})
    void onClickSelectDate(View v) {
        TimePickerDialog pickerDialog = new TimePickerDialog(this);
        pickerDialog.setDateType(DateType.DATE);
        pickerDialog.setListener(new TimePickerDialog.OnTimePickerDialogListener() {
            @Override
            public void onTimeSelect(String timeSelect) {
                mSelectedDateTime = timeSelect;
                mTvDate.setText(timeSelect);
                presenter.getList(mSelectedHotelID, mSelectedDateTime, mBaseUserBean.getUsername());
            }

            @Override
            public void onErrorDate(String errorDate) {
                ToastUitl.showShort(errorDate + "是无效日期");
            }
        });
        if (!isFinishing())
            pickerDialog.show();
    }

    @Override
    public void getBinGuanInfo(List<HotelBean> bins) {
        if (bins == null)
            return;
        adapter.addData(bins);
    }

    @Override
    public void getFaList(FaResultBean resultBean) {
        // 停止刷新
        mSRLayout.setRefreshing(false);
        mExpandableListViewAdapter.resetData(resultBean.getGroup(), resultBean.getChild());

        mExpandableListView.collapseGroup(0);
        mExpandableListView.expandGroup(0);
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        // 停止刷新
        mSRLayout.setRefreshing(false);
        ToastUitl.showShort("请检查网络连接");
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        // 停止刷新
        mSRLayout.setRefreshing(false);
        ToastUitl.showShort(errorMsg);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onLoadSuccessCallBack(String dataJson, RequestType type) {

    }

    @Override
    public void onRefresh() {
        presenter.getList(mSelectedHotelID, mSelectedDateTime, mBaseUserBean.getUsername());
    }
}
