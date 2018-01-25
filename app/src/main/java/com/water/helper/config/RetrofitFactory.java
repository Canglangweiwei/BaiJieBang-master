package com.water.helper.config;

import android.content.Context;

import com.jaydenxiao.common.commonutils.XgoLog;
import com.water.helper.base.AbsBaseApplication;
import com.water.helper.config.api.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>
 * 初始化网络请求工具
 * </p>
 * Created by weiwei on 2017/6/23.
 */
@SuppressWarnings("ALL")
public class RetrofitFactory {

    private static ApiService apiService;

    private static RetrofitFactory retrofitFactory;

    private RetrofitFactory(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(getHtttpLoggingInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit httpRetrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        apiService = httpRetrofit.create(ApiService.class);
    }

    /**
     * 日志打印
     */
    private static HttpLoggingInterceptor getHtttpLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                // 过滤BOM头
                message = message.replaceAll("\ufeff", "");
                XgoLog.loge("结果集：", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * 单例模式
     */
    public static synchronized RetrofitFactory get() {
        RetrofitFactory tmp = retrofitFactory;
        if (tmp == null) {
            synchronized (RetrofitFactory.class) {
                tmp = retrofitFactory;
                if (tmp == null) {
                    if (AbsBaseApplication.sApp != null) {
                        tmp = new RetrofitFactory(AbsBaseApplication.sApp);
                    } else {
                        tmp = new RetrofitFactory(null);
                    }
                    retrofitFactory = tmp;
                }
            }
        }
        return tmp;
    }

    /**
     * 获取用户信息
     */
    public ApiService getApiService() {
        return apiService;
    }
}
