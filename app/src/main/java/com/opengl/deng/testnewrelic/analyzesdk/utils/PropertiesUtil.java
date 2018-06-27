package com.opengl.deng.testnewrelic.analyzesdk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
public class  PropertiesUtil {

    private static final String USER_ID_ATTRIBUTE = "userId";
    private static final String USER_DEVICE_ID = "deviceId";
    private static final String USER_SERIAL_ID = "serialId";
    private static final String USER_LOCATION = "location";
    private static final String KEY_APP_CHANNEL = "relic_channel";
    private static String APP_CHANNEL;

    /**
     * 获取平台版本，如19（KITKAT），21等
     * @return platform version
     */
    private static int getPlatformVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取平台（Android）
     * @return platform
     */
    private static String getPlatform() {
        return "Android";
    }

    /**
     * 设置userId
     * @param userId userId(tel:)
     */
    public static void setUserId(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            PreferenceUtil.getInstance().writeToPreferences(USER_ID_ATTRIBUTE, userId);
        }
    }

    /**
     * 获取userId，用户唯一标识
     * @return userId
     */
    private static String getUserId() {
        String userId = PreferenceUtil.getInstance().readStringPreference(USER_ID_ATTRIBUTE, "");
        if (TextUtils.isEmpty(userId)) {
            userId = getRandomUserId();
            PreferenceUtil.getInstance().writeToPreferences(USER_ID_ATTRIBUTE, userId);
        }
        return userId;
    }

    /**
     * 获取deviceId，设备标识
     * @return deviceId
     */
    private static String getDeviceId() {
        return PreferenceUtil.getInstance().readStringPreference(USER_DEVICE_ID, "");
    }

    /**
     * 获取serialId，sim卡序列号
     * @return serialId
     */
    private static String getSerialId() {
        return PreferenceUtil.getInstance().readStringPreference(USER_SERIAL_ID, "");
    }

    private static String getUserLocation() {
        return PreferenceUtil.getInstance().readStringPreference(USER_LOCATION, "");
    }

    /**
     * 生成11位随机userId，供游客用户使用 : 6位日期 + 5位随机数
     * @return randomUserId
     */
    private static String getRandomUserId() {
        String dateFormat = "yyMMdd";
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        String date = sdf.format(new Date(System.currentTimeMillis()));

        Random random = new Random();
        int result = random.nextInt(90000) + 10000;
        return date + String.valueOf(result);
    }

    /**
     * 获取项目包名
     * @return product name
     */
    private static String getAppName() {
        return BuildConfig.APPLICATION_ID;
    }

    /**
     * 获取项目version
     * @return version
     */
    private static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 获取项目build
     * @return build
     */
    private static int getAppBuild() {
        return BuildConfig.VERSION_CODE;
    }

    /**
     * 从app manifest中获取到channel，即渠道
     * @param context app context
     */
    public static void setAppChannel(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String channel = appInfo.metaData.getString(KEY_APP_CHANNEL, "");
            if (TextUtils.isEmpty(channel)) {
                channel = "default";
            }
            APP_CHANNEL = channel;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            APP_CHANNEL = "default";
        }
    }

    /**
     * 获取渠道
     * @return channel
     */
    private static String getAppChannel() {
        return APP_CHANNEL;
    }

    /**
     * 获取手机类型
     * @return 手机类型
     */
    private static String getOsName() {
        return Build.MODEL;
    }

    /**
     * 获取手机系统版本
     * @return 手机系统版本
     */
    private static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前用户信息
     * @return json
     */
    public static String currentUserData() {
        return "{" + "userId : " + getUserId() + ","
                + "appBuild : " + getAppBuild() + ","
                + "appVersion : " + getAppVersion() + ","
                + "appName : " + getAppName() + ","
                + "appChannel : " + getAppChannel() + ","
                + "platform : " + getPlatform() + ","
                + "platformVersion : " + getPlatformVersion() + ","
                + "osName : " + getOsName() + ","
                + "osVersion : " + getOsVersion() + ","
                + "deviceId : " + getDeviceId() + ","
                + "serialId : " + getSerialId() + ","
                + "location : " + getUserLocation() + "}";
    }
}
