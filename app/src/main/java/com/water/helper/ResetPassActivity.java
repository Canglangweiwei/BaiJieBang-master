package com.water.helper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jaydenxiao.common.commonutils.MD5Util;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.config.component.DaggerResetPassPresenterComponent;
import com.water.helper.config.contract.ResetPassContract;
import com.water.helper.config.module.ResetPassModule;
import com.water.helper.config.presenter.ResetPassPresenter;
import com.water.helper.webservice.RequestType;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>
 * 修改密码
 * </p>
 * Created by Administrator on 2018/1/10 0010.
 */
public class ResetPassActivity extends AbsBaseActivity implements ResetPassContract.View {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    @Bind(R.id.et_reset_old_pass)
    EditText oldPassEdit;
    @Bind(R.id.et_reset_new_pass)
    EditText newPassEdit;

    private String checkName;

    @Inject
    ResetPassPresenter mPresenter;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_reset_pass;
    }

    @Override
    protected void parseIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            checkName = bundle.getString("check_name");
        }
    }

    @Override
    protected void setupComponent(AbsAppComponent component) {
        DaggerResetPassPresenterComponent.builder()
                .absAppComponent(component)
                .resetPassModule(new ResetPassModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initUi() {
        ntb.setTitleText("修改密码");
    }

    @Override
    protected void initDatas() {
        mPresenter.attachView(this);
    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.btn_reset_verfiy_commit})
    void commit(View view) {
        if (TextUtils.isEmpty(checkName)) {
            ToastUitl.showShort("用户名获取失败");
            return;
        }
        String old_pass = oldPassEdit.getText().toString().trim();
        if (TextUtils.isEmpty(old_pass)) {
            ToastUitl.showShort("请输入原密码");
            return;
        }
        String new_pass = newPassEdit.getText().toString().trim();
        if (TextUtils.isEmpty(new_pass)) {
            ToastUitl.showShort("请输入新密码");
            return;
        }
        // 显示加载框
        startProgressDialog();
        // 将密码进行md516位加密
        String md5_oldPassword = MD5Util.getMD5String(old_pass, 16, false);
        String md5_newPassword = MD5Util.getMD5String(new_pass, 16, false);
        // 修改密码
        mPresenter.resetPass(checkName, md5_oldPassword, md5_newPassword);
    }

    @Override
    public void getResetPassResult(String message) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort(message);
        // 退出
        onBackPressed();
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort("请检查网络连接");
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort(errorMsg);
    }

    @Override
    public void onLoadSuccessCallBack(String dataJson, RequestType type) {

    }
}
