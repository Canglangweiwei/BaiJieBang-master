package com.water.helper;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.DateTimeUtil;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.water.helper.adapter.CategoryListAdapter;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.config.component.DaggerJxInputPresenterComponent;
import com.water.helper.config.contract.JxInputContract;
import com.water.helper.config.module.JxInputModule;
import com.water.helper.config.presenter.JxInputPresenter;
import com.water.helper.webservice.RequestType;
import com.wevey.selector.dialog.DateType;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;
import com.wevey.selector.dialog.TimePickerDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>
 * 绩效录入
 * </p>
 * Created by Administrator on 2018/1/16 0016.
 */
public class JxInputActivity extends AbsBaseActivity implements JxInputContract.View {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.edit_jxlr)
    TextView mEditBh;   // 员工编号
    @Bind(R.id.s_gw)
    Spinner s_gw;       // 岗位
    @Bind(R.id.s_bc)
    Spinner s_bc;       // 班次
    @Bind(R.id.edit_jxlr_birth)
    TextView mEditRq;   // 日期

    private String mSelectGwName, mSelectBcName, mSelectedDateTime;

    @Inject
    JxInputPresenter presenter;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_jxlr;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void setupComponent(AbsAppComponent component) {
        DaggerJxInputPresenterComponent.builder()
                .absAppComponent(component)
                .jxInputModule(new JxInputModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initUi() {
        ntb.setTitleText("绩效录入");
        mSelectedDateTime = DateTimeUtil.getClientDateFormat("yyyy-MM-dd HH:mm:SS");
        mEditRq.setText(mSelectedDateTime);

        presenter.attachView(this);
    }

    private MDAlertDialog mdAlertDialog;

    @OnClick({R.id.edit_jxlr, R.id.btn_jxlr_commit})
    void commit(View view) {
        switch (view.getId()) {
            case R.id.edit_jxlr:// 员工选择
                startNextActivityForResult(null, MemberSelectActivity.class, 1001);
                break;
            case R.id.btn_jxlr_commit:// 确认提交
                final String name = mEditBh.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUitl.showShort("请选择员工");
                    return;
                }
                mdAlertDialog = new MDAlertDialog.Builder(JxInputActivity.this)
                        .setHeight(0.25f)  //屏幕高度*0.3
                        .setWidth(0.7f)  //屏幕宽度*0.7
                        .setTitleVisible(true)
                        .setTitleText("温馨提示")
                        .setTitleTextColor(R.color.black_light)
                        .setContentText("确定要提交？")
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
                                commit(name);
                            }
                        })
                        .build();
                if (!isFinishing())
                    mdAlertDialog.show();
                break;
        }
    }

    /**
     * 提交
     *
     * @param name 员工姓名
     */
    private void commit(String name) {
        startProgressDialog();
        presenter.add(name, mSelectGwName, mSelectBcName, mSelectedDateTime);
    }

    @Override
    protected void initDatas() {
        List<String> mListGw = new ArrayList<>();// 岗位列表
        mListGw.add("分拣班");
        mListGw.add("机洗班");
        mListGw.add("烫熨班");
        mListGw.add("毛巾班");
        CategoryListAdapter gAdapter = new CategoryListAdapter();
        s_bc.setAdapter(gAdapter);
        gAdapter.addData(mListGw);

        List<String> mListBc = new ArrayList<>();// 班次列表
        mListBc.add("白班");
        mListBc.add("夜班");
        CategoryListAdapter bAdapter = new CategoryListAdapter();
        s_gw.setAdapter(bAdapter);
        bAdapter.addData(mListBc);
    }

    @Override
    protected void initListener() {
        // 设置返回键监听事件
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 设置岗位列表的监听事件
        s_gw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectGwName = (String) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 设置班次列表的监听事件
        s_bc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectBcName = (String) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick({R.id.edit_jxlr_birth})
    void onClickSelectDate(View v) {
        TimePickerDialog pickerDialog = new TimePickerDialog(this);
        pickerDialog.setDateType(DateType.TIME);
        pickerDialog.setListener(new TimePickerDialog.OnTimePickerDialogListener() {
            @Override
            public void onTimeSelect(String timeSelect) {
                mSelectedDateTime = timeSelect;
                mEditRq.setText(timeSelect);
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
    public void addSuccess(String message) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort(message);
        // 退出当前页面
        finish();
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort(errorMsg);
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort("请检查网络连接");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1001) {
                mEditBh.setText(data.getStringExtra("name"));
            }
        }
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
