package com.opengl.deng.testnewrelic.analyzesdk.analyze;

import android.content.Context;
import android.text.TextUtils;

import com.opengl.deng.testnewrelic.analyzesdk.bean.UserEventBean;
import com.opengl.deng.testnewrelic.analyzesdk.bean.UserPerformBean;
import com.opengl.deng.testnewrelic.analyzesdk.utils.db.DBOperator;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description cache for data before sow into database
 * Created by deng on 2018/6/21.
 */
public class UserCache {
    private DBOperator operator;
    private final Map<String, Long> cacheMap = new HashMap<>();
    private static UserEventBean userEventBean = new UserEventBean();//TODO
    private static UserPerformBean userPerformBean = new UserPerformBean();

    public UserCache(Context context) {
        operator = DBOperator.getInstance(context);
    }

    /**
     * onResume时写入key和当前时间
     * @param key surface name
     * @return isSuccess
     */
    public boolean sowIntoOnResume(String key) {
        if (!TextUtils.isEmpty(key)) {
            synchronized (cacheMap) {
                cacheMap.put(key, System.currentTimeMillis());
            }
            return true;
        }
        return false;
    }

    /**
     * onPause时在jsonObject中写入key，起止时间和时长
     * @param key surface name
     * @return isSuccess
     */
    public boolean sowIntoOnPause(String key) {
        if (!TextUtils.isEmpty(key)) {
            Long startTime;
            synchronized (cacheMap) {
                startTime = cacheMap.remove(key);
            }

            if (startTime == null) {
                return false;
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            synchronized (userPerformBean) {
                userPerformBean.setSurface(key);
                userPerformBean.setStartTime(startTime);
                userPerformBean.setEndTime(endTime);
                userPerformBean.setDuration(duration);
                operator.insertUserPerform(userPerformBean);
            }
            return true;
        }
        return false;
    }
}
