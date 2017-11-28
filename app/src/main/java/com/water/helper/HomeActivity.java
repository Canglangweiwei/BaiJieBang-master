package com.water.helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.DataCleanManager;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.base.AbsBaseApplication;
import com.water.helper.bean.UserBean;
import com.water.helper.webservice.RequestType;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;

import org.litepal.crud.DataSupport;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class HomeActivity extends AbsBaseActivity {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.home_tv_zhaohu)
    TextView mTvzhaohu;

    private MDAlertDialog mdAlertDialog;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_home;
    }

    @Override
    protected void parseIntent() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Intent i = new Intent(HomeActivity.this, ShouListActivity.class);
            i.putExtra("id", bundle.getString("id"));
            startActivity(i);
        }
    }

    @Override
    protected void setupComponent(AbsAppComponent component) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null)
            return;
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Intent i = new Intent(this, ShouListActivity.class);
            i.putExtra("id", bundle.getString("id"));
            startActivity(i);
        }
    }

    @Override
    protected void initUi() {
        ntb.setTitleText(getString(R.string.app_name));
        ntb.setBackVisibility(false);
        mTvzhaohu.setText(mBaseUserBean == null ? "您好" : mBaseUserBean.getZhaohu());
    }

    @Override
    protected void initDatas() {

    }

    @OnClick({R.id.am_ll_notice, R.id.am_ll_shgl,
            R.id.am_ll_shtj, R.id.am_ll_songhgl})
    void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.am_ll_notice:// 物业通知
                ToastUitl.showShort("该功能暂未开放");
                break;
            case R.id.am_ll_shgl:// 收货管理
                startNextActivity(null, ShouActivity.class);
                break;
            case R.id.am_ll_shtj:// 收货统计
                startNextActivity(null, ShouListActivity.class);
                break;
            case R.id.am_ll_songhgl:// 送货管理
                startNextActivity(null, FaActivity.class);
                break;
        }
    }

    @Override
    protected void initListener() {
        // 右侧按钮
        ntb.setRightImagSrc(R.drawable.main_ic_logout);
        ntb.setOnRightImagListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mdAlertDialog = new MDAlertDialog.Builder(HomeActivity.this)
                        .setHeight(0.25f)  //屏幕高度*0.3
                        .setWidth(0.7f)  //屏幕宽度*0.7
                        .setTitleVisible(true)
                        .setTitleText("温馨提示")
                        .setTitleTextColor(R.color.black_light)
                        .setContentText("是否要注销登录？\n\n注销后会清空用户名和密码\n该过程不可逆")
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
                                // 重置账户信息
                                resetAccountInfo();
                            }
                        })
                        .build();
                if (!isFinishing())
                    mdAlertDialog.show();
            }
        });
    }

    private long newTime;

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - newTime > 2000) {
            newTime = System.currentTimeMillis();
            ToastUitl.showShort(getString(R.string.press_twice_exit));
        } else {
            AbsBaseApplication.sApp.finishAllActivity();
        }
    }

    @Override
    public void onLoadSuccessCallBack(String jsonData, RequestType type) {

    }

    /**
     * 重置账户信息
     */
    private void resetAccountInfo() {
        JPushInterface.stopPush(this);
        DataCleanManager.clearAllCache(this);
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        DataSupport.deleteAll(UserBean.class);
        AbsBaseApplication.sApp.setUserInfo(null);
        AbsBaseApplication.sApp.finishAllActivity();
    }
}
