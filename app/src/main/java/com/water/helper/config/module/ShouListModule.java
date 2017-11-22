package com.water.helper.config.module;

import com.water.helper.config.contract.ShouListContract;

import dagger.Module;

@Module
public class ShouListModule {

    private final ShouListContract.View view;

    /**
     * 构造器
     *
     * @param view 页面
     */
    public ShouListModule(ShouListContract.View view) {
        this.view = view;
    }
}
