package com.water.helper.config.module;

import com.water.helper.config.contract.CheckUserInfoContract;

import dagger.Module;

@Module
public class CheckUserInfoModule {

    private final CheckUserInfoContract.View view;

    /**
     * 构造器
     *
     * @param view 页面
     */
    public CheckUserInfoModule(CheckUserInfoContract.View view) {
        this.view = view;
    }
}
