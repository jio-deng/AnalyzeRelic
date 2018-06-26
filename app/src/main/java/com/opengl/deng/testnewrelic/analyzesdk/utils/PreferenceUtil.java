package com.opengl.deng.testnewrelic.analyzesdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @Description Created by deng on 2018/6/20.
 */
public class PreferenceUtil {
    private static final String SP_NAME = "analyze_relic";

    private static final String USER_ID_ATTRIBUTE = "userId";
    private static final String APP_BUILD_ATTRIBUTE = "appBuild";
    private static final String APP_NAME_ATTRIBUTE = "appName";
    private static final String APPLICATION_PLATFORM_ATTRIBUTE = "platform";
    private static final String APPLICATION_PLATFORM_VERSION_ATTRIBUTE = "platformVersion";
    private static final String OS_NAME_ATTRIBUTE = "osName";
    private static final String OS_VERSION_ATTRIBUTE = "osVersion";
    private static final String DEVICE_ID_ATTRIBUTE = "deviceId";

    /** 单例模式初始化 */
    private SharedPreferences sp;

    private static PreferenceUtil instance;
    private PreferenceUtil(){}

    public static void init(Context context) {
        if (instance != null) {
            throw new RuntimeException("PreferenceUtil has already bean initialized.");
        } else {
            instance = new PreferenceUtil();
            initSp(context, instance);
        }
    }

    public static PreferenceUtil getInstance() {
        return instance;
    }

    private static void initSp(Context context, PreferenceUtil instance) {
        instance.initSp(context);
    }

    private void initSp(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /** 写入sp */
    public void writeToPreferences(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void writeToPreferences(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void writeToPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /** 从sp读取数据 */
    public String readStringPreference(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public int readIntPreference(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public boolean readBooleanPreference(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /** 从sp删除数据 */
    public void removePreference(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }
}
