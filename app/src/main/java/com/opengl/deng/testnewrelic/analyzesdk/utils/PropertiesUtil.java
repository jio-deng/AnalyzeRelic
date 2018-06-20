package com.opengl.deng.testnewrelic.analyzesdk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;

import com.opengl.deng.testnewrelic.BuildConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Description to get Android Sdk version
 * including:
 * 1.app_channel
 * 2.platform,platformVersion
 * 3.userId
 * 4.osName,osVersion
 * 5.appName,appVersion,appBuild
 * 6.deviceId
 *
 * Created by deng on 2018/6/20.
 */
public class PropertiesUtil {

    private static final String USER_ID_ATTRIBUTE = "userId";
    private static final String KEY_APP_CHANNEL = "relic_channel";
    private static String APP_CHANNEL;

    /**
     * 获取平台版本，如19（KITKAT），21等
     * @return platform version
     */
    public static int getPlatformVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取平台（Android）
     * @return platform
     */
    public static String getPlatform() {
        return "Android";
    }

    /**
     * 获取userId，用户唯一标识
     * @return userId
     */
    public static String getUserId() {
        String userId = PreferenceUtil.getInstance().readStringPreference(USER_ID_ATTRIBUTE, "");
        if (TextUtils.isEmpty(userId)) {
            userId = getRandomUserId();
            PreferenceUtil.getInstance().writeToPreferences(USER_ID_ATTRIBUTE, userId);
        }
        return userId;
    }

    /**
     * 生成11位随机userId，供游客用户使用 : 6位日期 + 5位随机数
     * @return randomUserId
     */
    private static String getRandomUserId() {
        String dateFormat = "yyMMdd";
        DateFormat sdf = SimpleDateFormat.getDateInstance();
        String date = sdf.format(new Date(dateFormat));

        Random random = new Random();
        int result = random.nextInt(90000) + 10000;
        return date + String.valueOf(result);
    }

    /**
     * 获取项目包名
     * @return product name
     */
    public static String getAppName() {
        return BuildConfig.APPLICATION_ID;
    }

    /**
     * 获取项目version
     * @return version
     */
    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 获取项目build
     * @return build
     */
    public static int getAppBuild() {
        return BuildConfig.VERSION_CODE;
    }

    /**
     * 从app manifest中获取到channel，即渠道
     * @param context app context
     */
    public static void setAppChannel(Context context) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        String channel = appInfo.metaData.getString(KEY_APP_CHANNEL, "");
        if (TextUtils.isEmpty(channel)) {
            channel = "default";
        }
        APP_CHANNEL = channel;
    }

    /**
     * 获取渠道
     * @return channel
     */
    public static String getAppChannel() {
        return APP_CHANNEL;
    }

    /**
     * 获取手机类型
     * @return 手机类型
     */
    public static String getOsName() {
        return Build.MODEL;
    }

    /**
     * 获取手机系统版本
     * @return 手机系统版本
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

}
