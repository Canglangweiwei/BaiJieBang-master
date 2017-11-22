package com.water.helper.config.component;

import com.water.helper.ShouListActivity;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.config.module.ShouListModule;

import dagger.Component;

/**
 * 简单说就是，可以通过Component访问到Module中提供的依赖注入对象。
 * 假设，如果有两个Module，AModule、BModule，
 * 如果Component只注册了AModule，而没有注册BModule，那么BModule中提供的对象，无法进行依赖注入！
 */
@Component(dependencies = AbsAppComponent.class, modules = {ShouListModule.class})
public interface ShouListPresenterComponent {

    /**
     * <p>
     * 可以注册多个页面
     * </p>
     * 登录页面
     */
    void inject(ShouListActivity shouListActivity);
}
