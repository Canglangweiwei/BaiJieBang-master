package com.water.helper.config.module;

import com.water.helper.config.contract.FaContract;

import dagger.Module;

@Module
public class FaModule {

    private final FaContract.View view;

    /**
     * 构造器
     *
     * @param view 页面
     */
    public FaModule(FaContract.View view) {
        this.view = view;
    }
}
