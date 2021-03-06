package com.water.helper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonutils.XgoLog;
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
import com.water.helper.manager.PopupWindowManager;
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

    @Bind(R.id.root)
    View rootView;

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
    private ArrayList<GoodsModel> printList;

    private PopupWindowManager popupWindowManager;

    private CommonFilterHotelListAdapter hotelListAdapter;
    private CommonFilterHotelLzListAdapter hotelLzListAdapter;
    private int mSelectedHotelId, mSelectedHotelLzId;
    private String hotelName, hotelLcName;

    private String mNumber, mServerNumber;

    private boolean isShowHuixi = false;

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
        popupWindowManager = PopupWindowManager.getInstance();

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
        ntb.setRightTitleVisibility(true);
        ntb.setRightTitle("回洗");
        ntb.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowHuixi) {
                    mAdapter.startOper();
                } else {
                    mAdapter.cancelOper();
                }
                isShowHuixi = !isShowHuixi;
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
                hotelLcName = hotelLzBean.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        hotelLcName = lous.get(0).getName();
    }

    /**
     * 获取收货类型
     *
     * @param goodsModels 收货类型列表
     */
    @Override
    public void getAddType(ArrayList<GoodsModel> goodsModels) {
        isShowHuixi = false;
        if (goodsModels == null || goodsModels.size() == 0)
            return;
        goodsList = goodsModels;
        mAdapter.resetData(goodsModels);
    }

    /**
     * 提交收货信息
     *
     * @param message 提示信息
     */
    @Override
    public void add(String message) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort(message);
        // ①跳转打印机页面
        Bundle bundle = new Bundle();
        bundle.putString("hotel", hotelName);
        bundle.putString("hotelLc", hotelLcName);
        bundle.putString("beizhu", etContent.getEditTextContent());
        bundle.putParcelableArrayList("printer_list", printList);
        startNextActivity(bundle, PrinterActivity.class);
        // ②清空提交数据
        etContent.clear();
        for (GoodsModel model : goodsList) {
            model.setNum(0);
            model.setHuixiNum(0);
        }
        mAdapter.reset();
    }

    /**
     * 添加新的收货类型回调
     *
     * @param model 返回新类型
     */
    @Override
    public void addNewType(GoodsModel model) {
        if (model == null) {
            ToastUitl.showShort("类型添加失败");
            return;
        }
        if (TextUtils.isEmpty(mNumber)) {
            model.setNum(0);
        } else {
            model.setNum(Integer.parseInt(mNumber));
        }
        if (TextUtils.isEmpty(mServerNumber)) {
            model.setHuixiNum(0);
        } else {
            model.setHuixiNum(Integer.parseInt(mServerNumber));
        }
        goodsList.add(model);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        isShowHuixi = false;
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort("请检查网络连接");
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        isShowHuixi = false;
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
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
    public void addHuixi(View view, int position) {
        GoodsModel model = goodsList.get(position);
        model.setHuixiNum(model.getHuixiNum() + 1);
        mAdapter.reset();
    }

    /**
     * 重污-1
     *
     * @param position 下标
     */
    @Override
    public void reduceHuixi(int position) {
        GoodsModel model = goodsList.get(position);
        model.setHuixiNum(model.getHuixiNum() - 1);
        mAdapter.reset();
    }

    private MDAlertDialog mdAlertDialog;

    @OnClick({R.id.btn_add_more, R.id.add_goods_btn_submit})
    void submit(View view) {
        switch (view.getId()) {
            case R.id.btn_add_more:// 添加更多
                popupWindowManager.showPopupWindow(rootView);
                popupWindowManager.setPopCallback(new PopupWindowManager.PopCallback() {
                    @Override
                    public void callBack(String type, String number, String serverNumber) {
                        mNumber = number;
                        mServerNumber = serverNumber;
                        presenter.addNewType(mSelectedHotelId, type);
                    }
                });
                break;
            case R.id.add_goods_btn_submit:// 提交
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
                break;
        }
    }

    /**
     * 提交数据
     */
    private void upload() {
        printList = new ArrayList<>();
        // 去除冗余数据
        if (goodsList != null && goodsList.size() > 0) {
            for (GoodsModel model : goodsList) {
                if (model.getNum() != 0
                        || model.getHuixiNum() != 0) {
                    printList.add(model);
                }
            }
        }

        if (printList.size() == 0) {
            ToastUitl.showShort("没有需要入库的数据");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (GoodsModel model : printList) {
            builder.append(model.getId())
                    .append("/")
                    .append(model.getTitle())
                    .append("/")
                    .append(model.getNum())
                    .append("/")
                    .append(model.getHuixiNum())
                    .append("/")
                    .append(model.getTotalNum())
                    .append(",");
        }
        String username = mBaseUserBean.getUsername();
        String beizhu = etContent.getEditTextContent();
        // 显示提交加载框
        startProgressDialog();
        // 参数打印
        XgoLog.logd("参数：dataJson=" + builder.toString()
                + "&username=" + username + "&hid=" + mSelectedHotelId
                + "&lc=" + mSelectedHotelLzId + "&beizhu=" + beizhu);
        // 数据提交
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
