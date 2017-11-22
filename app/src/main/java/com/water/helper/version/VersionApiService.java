package com.water.helper.version;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.jaydenxiao.common.commonwidget.LoadingDialog;
import com.water.helper.bean.BaseModel;
import com.water.helper.config.httpclient.HttpPost;

import rx.Subscriber;

/**
 * <p>
 * 获取版本信息
 * </p>
 * Created by Administrator on 2017/9/4 0004.
 */
public class VersionApiService {

    private Context mContext;// 上下文环境

    /**
     * 构造器
     */
    public VersionApiService(Context context) {
        this.mContext = context;
    }

    /**
     * 检测是否需要更新版本
     */
    public void checkVersionRequest(final boolean showToast) {
        if (showToast) {
            LoadingDialog.showDialogForLoading((Activity) mContext);
        }
        HttpPost httpPost = new HttpPost();
        httpPost.Get_Version(new Subscriber<BaseModel>() {

            @Override
            public void onCompleted() {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseModel appUpdateInfoBaseResponse) {
                if (appUpdateInfoBaseResponse == null)
                    return;
                if (!appUpdateInfoBaseResponse.isSuccess())
                    return;
                UpdateVersionController controller = UpdateVersionController.getInstance(mContext, showToast);

                Gson mGson = new Gson();
                AppUpdateInfo appUpdateInfo = mGson.fromJson(appUpdateInfoBaseResponse.getData(), AppUpdateInfo.class);
                controller.normalCheckUpdateInfo(appUpdateInfo);
            }
        });
    }
}
