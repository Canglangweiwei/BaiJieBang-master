package com.water.helper.config.module;

import com.water.helper.config.contract.ResetPassContract;

import dagger.Module;

@Module
public class ResetPassModule {

    private final ResetPassContract.View view;

    /**
     * 构造器
     *
     * @param view 页面
     */
    public ResetPassModule(ResetPassContract.View view) {
        this.view = view;
    }
}
