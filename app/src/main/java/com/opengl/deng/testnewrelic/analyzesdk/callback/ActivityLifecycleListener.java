package com.opengl.deng.testnewrelic.analyzesdk.callback;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.opengl.deng.testnewrelic.analyzesdk.AnalyzeRelic;
import java.util.ArrayList;

/**
 * @Description life cycle listener:
 * 1.save user performance on activity resumed/paused
 * 2.judge if app is foreground or background
 * 3.todo:when onTrimMemory level is low, do sth.
 *
 * Created by deng on 2018/6/21.
 */
public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
    private static final String TAG = "ActivityLifecycleListen";

    private ArrayList<Activity> createActivityList;
    private ArrayList<Activity> visableActivityList;
    private ArrayList<Activity> invisableActivityList;

    public ActivityLifecycleListener() {
        createActivityList = new ArrayList<>();
        visableActivityList = new ArrayList<>();
        invisableActivityList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated: " + activity.getClass().getName());
        createActivityList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(TAG, "onActivityStarted: " + activity.getClass().getName());
    }

    /**
     * onResume和onPause中记录当前界面
     * 实现IUserDefineName的getName()方法可实现自定义key
     * @param activity 当前activity
     */
    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(TAG, "onActivityResumed: " + activity.getClass().getName());
        if (createActivityList.contains(activity)) {
            createActivityList.remove(activity);
            visableActivityList.add(activity);
        } else if (invisableActivityList.contains(activity)) {
            invisableActivityList.remove(activity);
            visableActivityList.add(activity);
        }

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
        Log.i(TAG, "onActivityPaused: " + activity.getClass().getName());

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
        Log.i(TAG, "onActivityStopped: " + activity.getClass().getName());
        if (visableActivityList.contains(activity)) {
            visableActivityList.remove(activity);
            invisableActivityList.add(activity);
        } else if (createActivityList.contains(activity)) {
            createActivityList.remove(activity);
            invisableActivityList.add(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i(TAG, "onActivityDestroyed: " + activity.getClass().getName());
        if (invisableActivityList.contains(activity)) {
            invisableActivityList.remove(activity);
        } else if (visableActivityList.contains(activity)) {
            visableActivityList.remove(activity);
        } else if (createActivityList.contains(activity)) {
            createActivityList.remove(activity);
        }
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
