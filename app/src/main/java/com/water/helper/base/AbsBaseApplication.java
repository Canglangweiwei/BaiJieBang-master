package com.water.helper.base;

import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.jaydenxiao.common.base.BaseApplication;
import com.jaydenxiao.common.commonutils.NetUtil;
import com.jaydenxiao.common.commonutils.XgoLog;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.app.DaggerAbsAppComponent;
import com.water.helper.bean.UserBean;
import com.water.helper.config.AppConfig;
import com.water.helper.manager.PopupWindowManager;
import com.water.helper.manager.PrintfManager;
import com.water.helper.message.MessageReceiver;

import org.litepal.crud.DataSupport;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@SuppressWarnings("ALL")
public class AbsBaseApplication extends BaseApplication {

    public static AbsBaseApplication sApp;

    private UserBean usInfo;                    // 用户信息

    private ExecutorService cachedThreadPool;

    public ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        // 是否允许打印日志
        XgoLog.logInit(AppConfig.LOG_DEBUG);
        // Dagger注入
        setApplicationComponent();

        // 初始化蓝牙打印机设置
        initBlePrinter();
        // 极光推送初始化
        // 初始化 JPush
        JPushInterface.init(this);
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(true);
    }

    /**
     * 初始化蓝牙打印机设置
     */
    private void initBlePrinter() {
        cachedThreadPool = Executors.newCachedThreadPool();
        PopupWindowManager.getInstance(getApplicationContext());
        PrintfManager.getInstance(getApplicationContext());
    }

    private AbsAppComponent absAppComponent;

    private void setApplicationComponent() {
        // Dagger开头的注入类DaggerAppComponent
        absAppComponent = DaggerAbsAppComponent.builder()
                .build();
    }

    /**
     * 获取AppComponent
     */
    public AbsAppComponent component() {
        if (absAppComponent == null) {
            setApplicationComponent();
        }
        return absAppComponent;
    }

    /**
     * 获取用户信息
     */
    public void setUserInfo(UserBean userBean) {
        this.usInfo = userBean;
        // 清空数据表
        DataSupport.deleteAll(UserBean.class);
        if (userBean == null) {
            // 登录信息为空，表示退出登录，停止推送
            JPushInterface.stopPush(curActivity);
            return;
        }
        // 初始化极光推送工具
        initPush();
        // 保存用户信息
        userBean.save();
    }

    /**
     * 保存用户信息
     */
    public UserBean getUserInfo() {
        if (usInfo == null) {
            usInfo = DataSupport.findFirst(UserBean.class);
        }
        return usInfo;
    }

    /**
     * 是否已经登录
     */
    public boolean isLogin() {
        return sApp.getUserInfo() != null;
    }

    /**
     * 登录或者注册成功之后，绑定推送
     */
    public void initPush() {
        registerMessageReceiver();
        if (JPushInterface.isPushStopped(curActivity)) {
            JPushInterface.resumePush(curActivity);
        } else {
            JPushInterface.init(curActivity);
        }
        // 绑定推送
        bindPushID();
    }

    /**
     * 绑定推送ID
     */
    private void bindPushID() {
        if (sApp.getUserInfo() == null
                || TextUtils.isEmpty(getUserInfo().getUsername())) {
            return;
        }
        XgoLog.logd("绑定极光推送");
        JPushInterface.setAlias(sApp, getUserInfo().getUsername(),
                new TagAliasCallback() {

                    @Override
                    public void gotResult(int code, String alias, Set<String> tags) {
                        String logs;
                        switch (code) {
                            case 0:
                                logs = "设置别名成功：" + alias;
                                break;
                            case 6002:
                                logs =
                                        "由于超时而设置别名和标记失败,60s后再试一次。";
                                if (NetUtil.getNetworkType() == NetUtil.NETWORK_TYPE_NONE) {
                                    handler.sendMessageDelayed(
                                            handler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                                } else {
                                    logs = "没有网络";
                                }
                                break;
                            default:
                                logs = "错误代码：" + code;
                        }
                        XgoLog.logd(logs);
                    }
                });
        JPushInterface.resumePush(sApp);
    }

    // for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MessageReceiver.MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    private final static int MSG_SET_ALIAS = 0x1000;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    bindPushID();
                    break;
            }
        }
    };

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(mMessageReceiver);
    }
}
