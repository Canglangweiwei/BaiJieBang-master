package com.jaydenxiao.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.Stack;

/**
 * APPLICATION
 */
@SuppressWarnings("ALL")
public class BaseApplication extends LitePalApplication {

    private static BaseApplication baseApplication;

    private Stack<Activity> activityStack;// activity栈

    protected Activity curActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        LitePal.initialize(this);
    }

    public static Context get() {
        return baseApplication;
    }

    public static Resources getAppResources() {
        return baseApplication.getResources();
    }

    /**
     * 把一个activity压入栈列中
     */
    public void pushActivityToStack(Activity actvity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        curActivity = actvity;
        activityStack.add(actvity);
    }

    /**
     * 获取栈顶的activity，先进后出原则
     */
    private Activity getLastActivityFromStack() {
        return activityStack.lastElement();
    }

    /**
     * 从栈列中移除一个activity
     */
    private void popActivityFromStack(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
                activity = null;
            }
        }
    }

    /**
     * 退出所有activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivityFromStack();
                if (activity == null) {
                    break;
                }
                popActivityFromStack(activity);
            }
        }
    }

    /**
     * 分包
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
