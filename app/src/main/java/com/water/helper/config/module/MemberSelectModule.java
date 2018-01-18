package com.water.helper.config.module;

import com.water.helper.config.contract.MemberSelectContract;

import dagger.Module;

@Module
public class MemberSelectModule {

    private final MemberSelectContract.View view;

    /**
     * 构造器
     *
     * @param view 页面
     */
    public MemberSelectModule(MemberSelectContract.View view) {
        this.view = view;
    }
}
