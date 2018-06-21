package com.opengl.deng.testnewrelic.analyzesdk.callback;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

import com.opengl.deng.testnewrelic.analyzesdk.AnalyzeRelic;

/**
 * @Description life cycle listener:
 * 1.save user performance on activity resumed/paused
 * 2.judge if app is foreground or background
 * 3.todo:when onTrimMemory level is low, do sth.
 *
 * Created by deng on 2018/6/21.
 */
public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    /**
     * onResume和onPause中记录当前界面
     * 实现IUserDefineName的getName()方法可实现自定义key
     * @param activity 当前activity
     */
    @Override
    public void onActivityResumed(Activity activity) {
        String key;
        if (activity instanceof IUserDefineName) {
            key = ((IUserDefineName) activity).getSurfaceName();
        } else {
            key = activity.getClass().getName();
        }
        AnalyzeRelic.getInstance().sowIntoOnResume(key);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        String key;
        if (activity instanceof IUserDefineName) {
            key = ((IUserDefineName) activity).getSurfaceName();
        } else {
            key = activity.getClass().getName();
        }
        AnalyzeRelic.getInstance().sowIntoOnPause(key);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }
}
