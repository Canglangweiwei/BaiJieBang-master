package com.water.helper.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;
import com.water.helper.R;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.bean.BaseBean;
import com.water.helper.bean.UserBean;
import com.water.helper.config.AppConfig;
import com.water.helper.webservice.HttpManager;
import com.water.helper.webservice.RequestType;
import com.water.helper.webservice.WebAPI;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p>
 * AbsBaseActivity
 * </p>
 * Created by Administrator on 2017/9/25 0025.
 */
@SuppressWarnings("ALL")
public abstract class AbsBaseActivity extends AppCompatActivity {

    protected Gson mGson;
    protected UserBean mBaseUserBean;
    protected WebAPI client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AbsBaseApplication.sApp.pushActivityToStack(this);
        setContentView(initLayoutResID());
        ButterKnife.bind(this);
        mGson = new Gson();
        client = HttpManager.getInstance().createService();
        mBaseUserBean = AbsBaseApplication.sApp.getUserInfo();
        parseIntent();
        setupComponent(AbsBaseApplication.sApp.component());
        initUi();
        initDatas();
        initListener();
    }

    /**
     * 页面绑定
     */
    protected abstract int initLayoutResID();

    /**
     * 解析页面间传递的数据
     */
    protected abstract void parseIntent();

    /**
     * Dagger2绑定
     *
     * @param component AppComponent
     */
    protected abstract void setupComponent(AbsAppComponent component);

    /**
     * 初始化UI
     */
    protected abstract void initUi();

    /**
     * 初始化数据
     */
    protected abstract void initDatas();

    /**
     * 初始化监听事件
     */
    protected abstract void initListener();

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    /**
     * 开启浮动加载进度条
     */
    public void startProgressDialog() {
        LoadingDialog.showDialogForLoading(this);
    }

    /**
     * 开启浮动加载进度条
     *
     * @param msg 提示信息
     */
    public void startProgressDialog(String msg) {
        LoadingDialog.showDialogForLoading(this, msg, true);
    }

    /**
     * 停止浮动加载进度条
     */
    public void stopProgressDialog() {
        LoadingDialog.cancelDialogForLoading();
    }

    /**
     * 启动新一个activity
     */
    public void startNextActivity(Bundle bundle, Class<?> tClass) {
        Intent intent = new Intent(this, tClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    /**
     * 最后一个参数为true表示finish当前activity
     */
    public void startNextActivity(Bundle bundle, Class<?> tClass, boolean allowedFinish) {
        Intent intent = new Intent(this, tClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        if (allowedFinish) {
            super.finish();
        }
    }

    /**
     * 请求activity for reslut
     */
    public void startNextActivityForResult(Bundle bundle, Class<?> tClass, int resquestCode) {
        Intent intent = new Intent(this, tClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, resquestCode);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }

    @Override
    public void finish() {
        this.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
        super.finish();
    }

    /**
     * 获取点击事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定是否需要隐藏软键盘
     */
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /*********************
     * 封装网络请求
     *******************/

    public abstract void onLoadSuccessCallBack(String dataJson, RequestType type);

    /**
     * 发起网络请求
     */
    private void sendRequestAsCtrl(Call<Map<String, Object>> response,
                                   final RequestType type, boolean showDialog) {
        // 进度条
        if (showDialog) {
            LoadingDialog.showDialogForLoading(this);
        }

        response.enqueue(new Callback<Map<String, Object>>() {

            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                LoadingDialog.cancelDialogForLoading();
                if (response == null || response.body() == null) {
                    ToastUitl.showShort("未能获取到数据，可能服务器停止服务了");
                    return;
                }

                Map<String, Object> requestResult = response.body();
                if (requestResult.containsKey(AppConfig.API_KEY_RESULT)) {
                    if (!AppConfig.SUCCESS.equals(requestResult.get(AppConfig.API_KEY_RESULT))) {
                        if (requestResult.containsKey(AppConfig.API_KEY_ERROR)) {
                            ToastUitl.showShort(requestResult.get(AppConfig.API_KEY_ERROR).toString());
                        }
                    } else {
                        requestResult.remove(AppConfig.API_KEY_RESULT);
                        requestResult.remove(AppConfig.API_KEY_ERROR);
                        onLoadSuccessCallBack(new Gson().toJson(requestResult), type);
                    }
                } else {
                    ToastUitl.showShort("出错了，但不是知道为什么");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> arg0, Throwable arg1) {
                LoadingDialog.cancelDialogForLoading();
                if (arg1 instanceof UnknownHostException) {
                    ToastUitl.showShort("无法连接服务器");
                } else if (arg1 instanceof SocketTimeoutException) {
                    ToastUitl.showShort("超时,请稍后重试");
                } else {
                    ToastUitl.showShort("服务器无法处理请求");
                }
            }
        });
    }

    /**
     * 发起网络请求
     */
    private final void sendRequestAsCtrl1(Call<BaseBean> response,
                                    final RequestType type, boolean showDialog) {
        // 进度条
        if (showDialog) {
            LoadingDialog.showDialogForLoading(this);
        }

        response.enqueue(new Callback<BaseBean>() {

            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                LoadingDialog.cancelDialogForLoading();
                if (response == null || response.body() == null) {
                    ToastUitl.showShort("未能获取到数据，可能服务器停止服务了");
                    return;
                }
                BaseBean requestResult = response.body();
                if (requestResult == null) {
                    ToastUitl.showShort("出错了，但不是知道为什么");
                } else {
                    int result = requestResult.getResult();
                    if (result == 1) {
                        onLoadSuccessCallBack(requestResult.getData(), type);
                    } else if (result == 0) {
                        ToastUitl.showShort(requestResult.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseBean> arg0, Throwable arg1) {
                LoadingDialog.cancelDialogForLoading();
                if (arg1 instanceof UnknownHostException) {
                    ToastUitl.showShort("无法连接服务器");
                } else if (arg1 instanceof SocketTimeoutException) {
                    ToastUitl.showShort("超时,请稍后重试");
                } else {
                    ToastUitl.showShort("服务器无法处理请求");
                }
            }
        });
    }

    protected void sendRequest(Call<Map<String, Object>> response,
                               final RequestType type, boolean allowShowDialog) {
        sendRequestAsCtrl(response, type, allowShowDialog);
    }

    protected void sendRequest1(Call<BaseBean> response,
                                final RequestType type, boolean allowShowDialog) {
        sendRequestAsCtrl1(response, type, allowShowDialog);
    }
}
