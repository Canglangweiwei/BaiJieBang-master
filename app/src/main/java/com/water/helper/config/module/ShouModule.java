package com.water.helper.config.module;

import com.water.helper.config.contract.ShouContract;

import dagger.Module;

@Module
public class ShouModule {

    private final ShouContract.View view;

    /**
     * 构造器
     *
     * @param view 页面
     */
    public ShouModule(ShouContract.View view) {
        this.view = view;
    }
}
