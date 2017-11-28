package com.water.helper;

import android.text.TextUtils;
import android.widget.ImageView;

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

/**
 * 欢迎页面
 */
@SuppressWarnings("ALL")
public class WelcomeActivity extends AbsBaseActivity implements LoginContract.View {

    // 用户名
    private String username;
    // 密码
    private String password;

    @Bind(R.id.image_launcherBg)
    ImageView image_launcherBg;// 背景图片

    @Inject
    LoginPresenter loginPresenter;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_welcome;
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
        loginPresenter.attachView(this);

        UserBean userBean = AbsBaseApplication.sApp.getUserInfo();
        if (userBean == null) {
            startNextActivity(null, LoginActivity.class, true);
            return;
        }
        username = userBean.getUsername();
        password = userBean.getPassword();
        if (TextUtils.isEmpty(username)
                || TextUtils.isEmpty(password)) {
            startNextActivity(null, LoginActivity.class, true);
            return;
        }
        String md5Password = MD5Util.getMD5String(password, 16, false);
        loginPresenter.login(username, md5Password);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void getUserinfo(UserBean userBean) {
        if (userBean == null)
            return;
        userBean.setPassword(password);
        // 保存用户信息
        AbsBaseApplication.sApp.setUserInfo(userBean);
        // 跳转到主页面
        startNextActivity(null, HomeActivity.class, true);
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        ToastUitl.showShort("请检查网络连接是否正常");
        startNextActivity(null, LoginActivity.class, true);
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        ToastUitl.showShort(errorMsg);
        startNextActivity(null, LoginActivity.class, true);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onLoadSuccessCallBack(String jsonData, RequestType type) {

    }
}
