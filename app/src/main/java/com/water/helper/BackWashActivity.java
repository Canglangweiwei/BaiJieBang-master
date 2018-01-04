package com.water.helper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aspsine.irecyclerview.view.DividerItemDecoration;
import com.jaydenxiao.common.commonutils.DateTimeUtil;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.water.helper.adapter.ChukuMxAdapter;
import com.water.helper.adapter.CommonFilterHotelListAdapter;
import com.water.helper.adapter.CommonFilterHotelLzListAdapter;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.bean.ChuKuMxBean;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.HotelLzBean;
import com.water.helper.config.component.DaggerBackWahPresenterComponent;
import com.water.helper.config.contract.BackWashContract;
import com.water.helper.config.module.BackWashModule;
import com.water.helper.config.presenter.BackWashPresenter;
import com.water.helper.webservice.RequestType;
import com.wevey.selector.dialog.DateType;
import com.wevey.selector.dialog.TimePickerDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>
 * 出库明细页面
 * </p>
 * Created by Administrator on 2018/1/3 0003.
 */
public class BackWashActivity extends AbsBaseActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        BackWashContract.View {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.back_wash_spinner_b)
    Spinner s_bg;   // 宾馆
    @Bind(R.id.back_wash_spinner_l)
    Spinner s_lc;   // 楼层
    @Bind(R.id.tv_tj_date)
    TextView mTvDate;   // 时间

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSRLayout;
    @Bind(R.id.fpl_rv_list)
    RecyclerView myRecycler;

    private CommonFilterHotelListAdapter hotelListAdapter;
    private CommonFilterHotelLzListAdapter hotelLzListAdapter;

    private int mSelectedHotelId, mSelectedHotelLzId;
    private String mSelectedDateTime;

    private ChukuMxAdapter mxAdapter;

    @Inject
    BackWashPresenter presenter;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_back_wash_manager;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void setupComponent(AbsAppComponent component) {
        DaggerBackWahPresenterComponent.builder()
                .absAppComponent(component)
                .backWashModule(new BackWashModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initUi() {
        ntb.setTitleText("回换管理");
        mSelectedDateTime = DateTimeUtil.getClientDateFormat("yyyy-MM-dd");
        mTvDate.setText(DateTimeUtil.getClientDateFormat("yyyy-MM-dd"));

        mSRLayout.setOnRefreshListener(this);
        mSRLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        mSRLayout.setDistanceToTriggerSync(400);

        // 最后一个参数代表是否反转
        myRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 分割线
        myRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // 初始化宾馆信息
        initBgMenu();
        // 初始化楼层信息
        initLcMenu();
    }

    /**
     * 初始化宾馆列表菜单
     */
    private void initBgMenu() {
        hotelListAdapter = new CommonFilterHotelListAdapter();
        s_bg.setAdapter(hotelListAdapter);
    }

    /**
     * 初始化楼层列表菜单
     */
    private void initLcMenu() {
        hotelLzListAdapter = new CommonFilterHotelLzListAdapter();
        s_lc.setAdapter(hotelLzListAdapter);
    }

    @Override
    protected void initDatas() {
        // TODO 添加列表适配器等操作
        // 绑定页面
        presenter.attachView(this);
        presenter.getBinGuanInfo(TextUtils.isEmpty(mBaseUserBean.getA_id()) ? "" : mBaseUserBean.getA_id());
        mxAdapter = new ChukuMxAdapter(this, presenter);
        myRecycler.setAdapter(mxAdapter);
    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 设置宾馆列表的监听事件
        s_bg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HotelBean hotelBean = (HotelBean) parent.getSelectedItem();
                mSelectedHotelId = hotelBean.getId();
                presenter.getLoucInfo(mSelectedHotelId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 设置楼层列表的监听事件
        s_lc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HotelLzBean hotelLzBean = (HotelLzBean) parent.getSelectedItem();
                mSelectedHotelLzId = hotelLzBean.getId();
                presenter.getChuKuMx(mSelectedHotelId, mSelectedHotelLzId, mSelectedDateTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onRefresh() {
        presenter.getChuKuMx(mSelectedHotelId, mSelectedHotelLzId, mSelectedDateTime);
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
                presenter.getChuKuMx(mSelectedHotelId, mSelectedHotelLzId, mSelectedDateTime);
            }

            @Override
            public void onErrorDate(String errorDate) {
                ToastUitl.showShort(errorDate + "是无效日期");
            }
        });
        if (!isFinishing())
            pickerDialog.show();
    }

    /**
     * 获取宾馆信息
     *
     * @param bins 宾馆列表
     */
    @Override
    public void getBinGuanInfo(List<HotelBean> bins) {
        if (bins == null || bins.size() == 0)
            return;
        hotelListAdapter.addData(bins);
    }

    /**
     * 获取楼层信息
     *
     * @param lous 楼层列表
     */
    @Override
    public void getLoucInfo(List<HotelLzBean> lous) {
        if (lous == null || lous.size() == 0)
            return;
        hotelLzListAdapter.addData(lous);
        // 获取默认的第一个item的值
        mSelectedHotelLzId = lous.get(0).getId();
    }

    @Override
    public void getChuKuMxInfo(List<ChuKuMxBean> chuKuMxList) {
        mSRLayout.setRefreshing(false);
        if (chuKuMxList == null)
            return;
        mxAdapter.reset(chuKuMxList);
    }

    @Override
    public void huiHuanSuccess(String message) {
        // 提示消息
        ToastUitl.showShort(message);
        presenter.getChuKuMx(mSelectedHotelId, mSelectedHotelLzId, mSelectedDateTime);
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        mSRLayout.setRefreshing(false);
        // 提示消息
        ToastUitl.showShort("请检查网络连接");
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        mSRLayout.setRefreshing(false);
        // 提示消息
        ToastUitl.showShort(errorMsg);
        if (mxAdapter != null) {
            mxAdapter.clear();
        }
    }

    @Override
    public void onLoadSuccessCallBack(String dataJson, RequestType type) {

    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
