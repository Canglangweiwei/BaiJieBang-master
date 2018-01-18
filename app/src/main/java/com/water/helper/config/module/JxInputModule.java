package com.water.helper.config.module;

import com.water.helper.config.contract.JxInputContract;

import dagger.Module;

@Module
public class JxInputModule {

    private final JxInputContract.View view;

    /**
     * 构造器
     *
     * @param view 页面
     */
    public JxInputModule(JxInputContract.View view) {
        this.view = view;
    }
}
