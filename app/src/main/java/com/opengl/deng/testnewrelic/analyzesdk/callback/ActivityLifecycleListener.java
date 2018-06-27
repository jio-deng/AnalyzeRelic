package com.opengl.deng.testnewrelic.analyzesdk.callback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.opengl.deng.testnewrelic.analyzesdk.AnalyzeRelic;
import com.opengl.deng.testnewrelic.analyzesdk.bean.LocationBean;
import com.opengl.deng.testnewrelic.analyzesdk.utils.LocationUtil;
import com.opengl.deng.testnewrelic.analyzesdk.utils.PermissionUtil;
import com.opengl.deng.testnewrelic.analyzesdk.utils.PreferenceUtil;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.opengl.deng.testnewrelic.analyzesdk.utils.PermissionUtil.READ_PHONE_STATE;
import static com.opengl.deng.testnewrelic.analyzesdk.utils.PermissionUtil.GET_PHONE_LOCATION;

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
    private boolean hasDeviceId = false;
    private boolean hasLocation = false;

    public ActivityLifecycleListener() {
        createActivityList = new ArrayList<>();
        visableActivityList = new ArrayList<>();
        invisableActivityList = new ArrayList<>();
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated: " + activity.getClass().getName());
        createActivityList.add(activity);
        //检查权限
        if (!hasDeviceId) {
            if (PermissionUtil.getPermission(activity, READ_PHONE_STATE, 0)) {
                TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null) {
                    PreferenceUtil.getInstance().writeToPreferences("deviceId", tm.getDeviceId());
                    PreferenceUtil.getInstance().writeToPreferences("serialId", tm.getSimSerialNumber());
                    hasDeviceId = true;
                }
            }
        }
        if (!hasLocation) {
            if (PermissionUtil.getPermission(activity, 1, GET_PHONE_LOCATION)) {
                LocationUtil.getInstance()
                        .getLocation(activity)
                        .subscribe(new Consumer<LocationBean>() {
                            @Override
                            public void accept(LocationBean locationBean) throws Exception {
                                PreferenceUtil.getInstance().writeToPreferences("location", locationBean.toString());
                                hasLocation = true;
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "getLocation: throwable->" + throwable.getMessage());
                            }
                        });
            }
        }
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
