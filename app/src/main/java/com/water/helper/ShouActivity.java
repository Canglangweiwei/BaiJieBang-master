package com.water.helper;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.water.helper.webservice.RequestType;
import com.water.helper.widget.CalcEditLenView;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;

import java.util.ArrayList;
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
    private ArrayList<GoodsModel> goodsList;

    private CommonFilterHotelListAdapter hotelListAdapter;
    private CommonFilterHotelLzListAdapter hotelLzListAdapter;
    private int mSelectedHotelId, mSelectedHotelLzId;
    private String hotelName, hotelLcName;

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

        // 设置宾馆列表的监听事件
        s_bg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HotelBean hotelBean = (HotelBean) parent.getSelectedItem();
                mSelectedHotelId = hotelBean.getId();
                hotelName = hotelBean.getName();
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
                hotelLcName = hotelLzBean.getName();
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
    public void getAddType(ArrayList<GoodsModel> goodsModels) {
        if (goodsModels == null || goodsModels.size() == 0)
            return;
        goodsList = goodsModels;
        mAdapter.resetData(goodsModels);
    }

    /**
     * 添加收货信息
     *
     * @param message 提示信息
     */
    @Override
    public void add(String message) {
        ToastUitl.showShort(message);
        // ①跳转打印机页面
        Bundle bundle = new Bundle();
        bundle.putString("hotel", hotelName);
        bundle.putString("hotelLc", hotelLcName);
        bundle.putString("beizhu", etContent.getEditTextContent());
        bundle.putParcelableArrayList("printer_list", goodsList);
        startNextActivity(bundle, PrinterActivity.class);
        // ②清空提交数据
        etContent.clear();
        for (GoodsModel model : goodsList) {
            model.setNum(0);
            model.setNum_wu(0);
        }
        mAdapter.reset();
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        ToastUitl.showShort("请检查网络连接");
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        ToastUitl.showShort(errorMsg);
    }

    /**
     * 商品+1
     *
     * @param view     view
     * @param position 下标
     */
    @Override
    public void addGoods(View view, int position) {
        GoodsModel model = goodsList.get(position);
        model.setNum(model.getNum() + 1);
        mAdapter.reset();
        // 更新待提交的数据源
        // .............
    }

    /**
     * 商品输入
     *
     * @param position 下标
     * @param s        输入
     */
    @Override
    public void inputGoods(int position, String s) {
        GoodsModel model = goodsList.get(position);
        model.setNum(Integer.parseInt(s));
        mAdapter.reset();
    }

    /**
     * 商品-1
     *
     * @param position 下标
     */
    @Override
    public void reduceGoods(int position) {
        GoodsModel model = goodsList.get(position);
        model.setNum(model.getNum() - 1);
        mAdapter.reset();
        // 更新待提交的数据源
        // .............
    }

    /**
     * 重污+1
     *
     * @param view     view
     * @param position 下标
     */
    @Override
    public void addWu(View view, int position) {
        GoodsModel model = goodsList.get(position);
        model.setNum_wu(model.getNum_wu() + 1);
        mAdapter.reset();
    }

    /**
     * 重污-1
     *
     * @param position 下标
     */
    @Override
    public void reduceWu(int position) {
        GoodsModel model = goodsList.get(position);
        model.setNum_wu(model.getNum_wu() - 1);
        mAdapter.reset();
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
                    .append("/")
                    .append(model.getNum_wu())
                    .append(",");
        }
        String username = mBaseUserBean.getUsername();
        String beizhu = etContent.getEditTextContent();
        System.out.println(builder.toString());
        presenter.add(builder.toString(), username, mSelectedHotelId, mSelectedHotelLzId, beizhu);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onLoadSuccessCallBack(String dataJson, RequestType type) {

    }
}
