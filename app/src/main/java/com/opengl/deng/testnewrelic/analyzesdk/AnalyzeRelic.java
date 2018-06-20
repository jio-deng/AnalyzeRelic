package com.opengl.deng.testnewrelic.analyzesdk;

import android.content.Context;

import com.opengl.deng.testnewrelic.analyzesdk.utils.PreferenceUtil;
import com.opengl.deng.testnewrelic.analyzesdk.utils.PropertiesUtil;

/**
 * @Description Android analyze entrance:
 * 1.analyze your app including user performance, activities used frequency and so on
 * 2.collect your app's crash logs
 * 3.LogUtil added
 *
 * Created by deng on 2018/6/19.
 */
public class AnalyzeRelic {
    private static final String TAG = "AnalyzeRelic";

    private static AnalyzeRelic instance;
    private static Context mContext;

    /** 单例模式获取AnanlyzeRelic对象 */
    public static AnalyzeRelic getInstance() {
        if (instance == null) {
            instance = new AnalyzeRelic();
        }
        return instance;
    }

    /** 设置app context */
    public static void withApplication(Context context) {
        mContext = context;


        PreferenceUtil.init(context);
        init(context);
    }

    private AnalyzeRelic(){}

    /**
     * 初始化数据
     * @param context app context
     */
    private static void init(Context context) {
        //set app channel
        PropertiesUtil.setAppChannel(context);


    }





}
