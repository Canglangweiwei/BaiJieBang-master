package com.water.helper.base;

@SuppressWarnings("ALL")
public interface BaseView {
    void onFailureCallback(Throwable throwable);

    void onFailureCallback(int errorCode, String errorMsg);
}
