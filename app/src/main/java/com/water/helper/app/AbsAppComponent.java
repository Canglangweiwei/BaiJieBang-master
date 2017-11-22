package com.water.helper.app;

import com.water.helper.base.AbsBaseApplication;

import dagger.Component;

@Component()
public interface AbsAppComponent {
    void inject(AbsBaseApplication absBaseApplication);
}
