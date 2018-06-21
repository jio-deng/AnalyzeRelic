package com.opengl.deng.testnewrelic;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.opengl.deng.testnewrelic.analyzesdk.AnalyzeRelic;

/**
 * @Description Created by deng on 2018/6/21.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
        AnalyzeRelic.getInstance().withApplication(this);
    }
}
