package com.water.helper;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.water.helper.adapter.CommonFilterHotelListAdapter;
import com.water.helper.adapter.CommonFilterHotelLzListAdapter;
import com.water.helper.adapter.GoodsAdapter;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.bean.GoodsModel;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.HotelLzBean;
import com.water.helper.config.component.DaggerShouPresenterComponent;
import com.water.helper.config.contract.ShouContract;
import com.water.helper.config.module.ShouModule;
import com.water.helper.config.presenter.ShouPresenter;
import com.water.helper.widget.CalcEditLenView;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>
 * 收货管理
 * </p>
 * Created by Administrator on 2017/10/31 0031.
 */
public class ShouActivity extends AbsBaseActivity
        implements ShouContract.View,
        GoodsAdapter.GoodsActionCallback {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.add_goods_spinner_b)
    Spinner s_bg;   // 宾馆
    @Bind(R.id.add_goods_spinner_l)
    Spinner s_lc;   // 楼层

    @Bind(R.id.add_goods_list)
    ListView listView;

    @Bind(R.id.calcEditLenView)
    CalcEditLenView etContent;

    private GoodsAdapter mAdapter;
    private List<GoodsModel> goodsList;

    private CommonFilterHotelListAdapter hotelListAdapter;
    private CommonFilterHotelLzListAdapter hotelLzListAdapter;
    private int mSelectedHotelId, mSelectedHotelLzId;

    @Inject
    ShouPresenter presenter;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_shou;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void setupComponent(AbsAppComponent component) {
        DaggerShouPresenterComponent.builder()
                .absAppComponent(component)
                .shouModule(new ShouModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initUi() {
        ntb.setTitleText("收货管理");
        initBgMenu();
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
        mAdapter = new GoodsAdapter();
        mAdapter.setCallback(this);
        listView.setAdapter(mAdapter);
        mAdapter.resetData(goodsList);
        // 绑定页面
        presenter.attachView(this);
        presenter.getBinGuanInfo();
    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 设置宾馆列表的监听事件
        s_bg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HotelBean hotelBean = (HotelBean) parent.getSelectedItem();
                mSelectedHotelId = hotelBean.getId();
                presenter.getAddType(mSelectedHotelId);
                presenter.getLoucInfo(hotelBean.getId());
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void getBinGuanInfo(List<HotelBean> bins) {
        if (bins == null)
            return;
        hotelListAdapter.addData(bins);
    }

    @Override
    public void getLoucInfo(List<HotelLzBean> lous) {
        if (lous == null)
            return;
        hotelLzListAdapter.addData(lous);
    }

    @Override
    public void getAddType(List<GoodsModel> goodsModels) {
        if (goodsModels == null || goodsModels.size() == 0)
            return;
        goodsList = goodsModels;
        mAdapter.resetData(goodsModels);
    }

    @Override
    public void add(String message) {
        ToastUitl.showShort(message);
        onBackPressed();
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        ToastUitl.showShort("请检查网络连接");
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        ToastUitl.showShort(errorMsg);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void addGoods(View view, int position) {
        GoodsModel model = goodsList.get(position);
        model.setNum(model.getNum() + 1);
        mAdapter.reset();
        // 更新待提交的数据源
        // .............
    }

    @Override
    public void inputGoods(int position, String s) {
        GoodsModel model = goodsList.get(position);
        model.setNum(Integer.parseInt(s));
        mAdapter.reset();
    }

    @Override
    public void reduceGoods(int position) {
        GoodsModel model = goodsList.get(position);
        model.setNum(model.getNum() - 1);
        mAdapter.reset();
        // 更新待提交的数据源
        // .............
    }

    private MDAlertDialog mdAlertDialog;

    @OnClick({R.id.add_goods_btn_submit})
    void submit(View view) {
        mdAlertDialog = new MDAlertDialog.Builder(ShouActivity.this)
                .setHeight(0.25f)  //屏幕高度*0.3
                .setWidth(0.7f)  //屏幕宽度*0.7
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.black_light)
                .setContentText("是否要提交收货信息？")
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.black_light)
                .setRightButtonText("确认")
                .setRightButtonTextColor(R.color.gray)
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setButtonTextSize(14)
                .setOnclickListener(new DialogOnClickListener() {

                    @Override
                    public void clickLeftButton(View view) {
                        mdAlertDialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        mdAlertDialog.dismiss();
                        // 提交收货信息
                        upload();
                    }
                })
                .build();
        if (!isFinishing())
            mdAlertDialog.show();
    }

    /**
     * 提交数据
     */
    private void upload() {
        StringBuilder builder = new StringBuilder();
        for (GoodsModel model : goodsList) {
            builder.append(model.getId())
                    .append("/")
                    .append(model.getTitle())
                    .append("/")
                    .append(model.getNum())
                    .append(",");
        }
        String username = mBaseUserBean.getUsername();
        String beizhu = etContent.getEditTextContent();
        System.out.println(builder.toString());
        presenter.add(builder.toString(), username, mSelectedHotelId, mSelectedHotelLzId, beizhu);
    }
}
