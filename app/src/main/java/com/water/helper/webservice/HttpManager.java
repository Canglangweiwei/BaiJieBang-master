package com.water.helper.webservice;

import com.google.gson.GsonBuilder;
import com.jaydenxiao.common.commonutils.XgoLog;
import com.water.helper.config.AppConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求管理类
 */
@SuppressWarnings("ALL")
public class HttpManager {

    private static final String SERVER_URL = AppConfig.sServiceUrl;
    private static HttpManager mInstance;
    private Retrofit mRetrofit;
    private WebAPI mWebAPI;

    /*
     * 单例模式，创建实例
     */
    public static synchronized HttpManager getInstance() {
        if (mInstance == null) {
            mInstance = new HttpManager();
        }
        return mInstance;
    }

    private HttpManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))
                .client(genericClient())
                .build();
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

    private OkHttpClient genericClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = chain
                                        .request()
                                        .newBuilder()
                                        .addHeader("Content-Type",
                                                "application/x-www-form-urlencoded; charset=UTF-8")
                                        .addHeader("Accept-Encoding", "gzip, deflate")
                                        .addHeader("Connection", "keep-alive")
                                        .addHeader("Accept", "*/*")
                                        .addHeader("Cookie", "add cookies here")
                                        .build();
                                return chain.proceed(request);
                            }
                        })
                .addInterceptor(getHtttpLoggingInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public synchronized WebAPI createService() {
        if (mWebAPI == null) {
            mWebAPI = mRetrofit.create(WebAPI.class);
        }
        return mWebAPI;
    }
}