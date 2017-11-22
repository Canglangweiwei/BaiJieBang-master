package com.water.helper.config.module;

import com.water.helper.config.contract.LoginContract;

import dagger.Module;

@Module
public class LoginModule {

    private final LoginContract.View view;

    /**
     * 构造器
     *
     * @param view 页面
     */
    public LoginModule(LoginContract.View view) {
        this.view = view;
    }
}
