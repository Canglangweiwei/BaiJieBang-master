package com.water.helper.config.module;

import com.water.helper.config.contract.BackWashContract;

import dagger.Module;

@Module
public class BackWashModule {

    private final BackWashContract.View view;

    /**
     * 构造器
     *
     * @param view 页面
     */
    public BackWashModule(BackWashContract.View view) {
        this.view = view;
    }
}
