package com.opengl.deng.testnewrelic.analyzesdk;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.opengl.deng.testnewrelic.analyzesdk.analyze.Sowing;
import com.opengl.deng.testnewrelic.analyzesdk.bean.UserPerformBean;
import com.opengl.deng.testnewrelic.analyzesdk.callback.ActivityLifecycleListener;
import com.opengl.deng.testnewrelic.analyzesdk.harvest.Harvest;
import com.opengl.deng.testnewrelic.analyzesdk.utils.PreferenceUtil;
import com.opengl.deng.testnewrelic.analyzesdk.utils.PropertiesUtil;

import java.util.List;

import io.reactivex.Observable;

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
    private static int performNum = 5;

    private Context mContext;
    private Sowing mSowing;
    private Harvest mHarvest;

    /** 单例模式获取AnanlyzeRelic对象 */
    private static AnalyzeRelic instance;
    private AnalyzeRelic(){}
    public static AnalyzeRelic getInstance() {
        if (instance == null) {
            instance = new AnalyzeRelic();
        }
        return instance;
    }

    /** 设置用户行为上传条数 */
    public void setMaxPerformToUpdate(int num) {
        performNum = num;
    }

    //TODO:user event

    /** 设置app context */
    public void withApplication(Context context) {
        if (mContext != null) {
            Log.d(TAG, "AnalyzeRelic has already bean initialized.");
        } else {
            mContext = context;
            init(context);
        }
    }

    /**
     * 初始化数据
     * @param context app context
     */
    private void init(Context context) {
        //init sp
        PreferenceUtil.init(context);
        //set app channel
        PropertiesUtil.setAppChannel(context);
        //init sowing
        mSowing = new Sowing(context, performNum);
        //init harvest
        mHarvest = new Harvest(context);
        //set activity lifecycle listener
        if(context.getApplicationContext() instanceof Application) {
            Application application = (Application)context.getApplicationContext();
            ActivityLifecycleListener listener = new ActivityLifecycleListener();
            application.registerActivityLifecycleCallbacks(listener);
        }
    }

    /**
     * 保存用户行为信息
     * @param surface 界面名称
     */
    public void sowIntoOnResume(String surface) {
        boolean result = mSowing.sowIntoOnResume(surface);
        if (!result) {
            Log.e(TAG, "sowIntoOnResume: failed! Surface is " + surface + " , can't not sow in on resume.");
        }
    }

    public void sowIntoOnPause(String surface) {
        boolean result = mSowing.sowIntoOnPause(surface);
        if (!result) {
            Log.e(TAG, "sowIntoOnPause: failed! Surface is " + surface + " , can't not sow in on pause.");
        }
    }

    /**
     * 用户信息收集到一定数目时调用上传
     * @param startTime 数据开始时间
     * @param endTime 数据结束时间
     * @param observable 查询数据observable
     */
    public void updatePerformData(long startTime, long endTime, Observable<List<UserPerformBean>> observable) {
        mHarvest.updatePerformData(startTime, endTime, observable);
    }

}
