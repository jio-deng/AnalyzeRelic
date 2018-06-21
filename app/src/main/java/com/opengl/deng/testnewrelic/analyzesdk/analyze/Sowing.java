package com.opengl.deng.testnewrelic.analyzesdk.analyze;

import android.content.Context;

/**
 * @Description Insert into application, collect user info
 * 1.collect user performance, etc. activity uses, time duration
 * 2.to be continued
 *
 * Created by deng on 2018/6/19.
 */
public class Sowing {
    private UserCache userCache;

    public Sowing(Context context) {
        userCache = new UserCache(context);
    }

    /**
     * 将用户行为统计插入到数据库中
     * @param surface surface name
     * @return isSuccess
     */
    public boolean sowIntoOnResume(String surface) {
        return userCache.sowIntoOnResume(surface);
    }

    public boolean sowIntoOnPause(String surface) {
        return userCache.sowIntoOnPause(surface);
    }
}
