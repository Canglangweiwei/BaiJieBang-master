package com.water.helper;

import android.view.View;
import android.widget.EditText;

import com.jaydenxiao.common.commonutils.MD5Util;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.base.AbsBaseApplication;
import com.water.helper.bean.UserBean;
import com.water.helper.config.component.DaggerLoginPresenterComponent;
import com.water.helper.config.contract.LoginContract;
import com.water.helper.config.module.LoginModule;
import com.water.helper.config.presenter.LoginPresenter;
import com.water.helper.webservice.RequestType;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>
 * 用户登录
 * </p>
 * Created by Administrator on 2017/7/6 0006.
 */
@SuppressWarnings("ALL")
public class LoginActivity extends AbsBaseActivity implements LoginContract.View {

    @Bind(R.id.et_login_account)
    EditText mEditUsername;// 账号
    @Bind(R.id.et_login_pwd)
    EditText mEditPassword;// 密码

    @Inject
    LoginPresenter mPresenter;

    private String username, password;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void setupComponent(AbsAppComponent component) {
        DaggerLoginPresenterComponent.builder()
                .absAppComponent(component)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initUi() {

    }

    @Override
    protected void initDatas() {
        UserBean userBean = AbsBaseApplication.sApp.getUserInfo();
        if (userBean != null) {
            mEditUsername.setText(userBean.getUsername());
            mEditPassword.setText(userBean.getPassword());
        }
        // 绑定页面
        mPresenter.attachView(this);
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.tv_resetPass_login})
    void resetPass(View view) {
        startNextActivity(null, CheckUserInfoActivity.class);
    }

    /**
     * 开始登录
     */
    @OnClick({R.id.btn_login})
    void btnLogin() {
        username = mEditUsername.getText().toString().trim();
        password = mEditPassword.getText().toString().trim();
        startProgressDialog();
        // 将密码进行md516位加密
        String md5Password = MD5Util.getMD5String(password, 16, false);
        mPresenter.login(username, md5Password);
    }

    /**
     * 获取用户信息
     *
     * @param userBean 用户信息
     */
    @Override
    public void getUserinfo(UserBean userBean) {
        stopProgressDialog();
        if (userBean == null)
            return;
        userBean.setPassword(password);
        // 保存用户信息
        AbsBaseApplication.sApp.setUserInfo(userBean);
        // 跳转到主页面
        startNextActivity(null, HomeActivity.class, true);
    }

    /**
     * 返回登录失败信息
     *
     * @param throwable
     */
    @Override
    public void onFailureCallback(Throwable throwable) {
        stopProgressDialog();
        ToastUitl.showShort("请检查网络连接是否正常");
    }

    /**
     * 返回登录失败信息
     *
     * @param errorCode 错误代码
     * @param errorMsg  错误信息
     */
    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        stopProgressDialog();
        ToastUitl.showShort(errorMsg);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onLoadSuccessCallBack(String dataJson, RequestType type) {

    }
}
