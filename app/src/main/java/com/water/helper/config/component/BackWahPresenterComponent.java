package com.water.helper.config.component;

import com.water.helper.BackWashActivity;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.config.module.BackWashModule;

import dagger.Component;

/**
 * 简单说就是，可以通过Component访问到Module中提供的依赖注入对象。
 * 假设，如果有两个Module，AModule、BModule，
 * 如果Component只注册了AModule，而没有注册BModule，那么BModule中提供的对象，无法进行依赖注入！
 */
@Component(dependencies = AbsAppComponent.class, modules = {BackWashModule.class})
public interface BackWahPresenterComponent {

    // 可以注册多个页面
    void inject(BackWashActivity faActivity);
}
