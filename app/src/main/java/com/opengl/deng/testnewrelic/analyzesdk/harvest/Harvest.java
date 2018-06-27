package com.opengl.deng.testnewrelic.analyzesdk.harvest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.opengl.deng.testnewrelic.analyzesdk.bean.UserPerformBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * @Description Class for data collecting
 * 1.data collecting
 * 2.data saving
 * 3.upload user performance
 * 4.upload crash logs
 *
 * Created by deng on 2018/6/19.
 */
public class Harvest {
    private static final String TAG = "Harvest";

    private Context context;
    private SchedulerService service;
    private ServiceConnection connection;

    public Harvest(Context context) {
        this.context = context;
        context.startService(new Intent(context, SchedulerService.class));
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                service = (SchedulerService) ((SchedulerService.Binder) binder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                service = null;
            }
        };
        context.bindService(new Intent(context, SchedulerService.class), connection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 用户信息收集到一定数目时调用上传
     * @param startTime 数据开始时间
     * @param endTime 数据结束时间
     * @param observable 查询数据observable
     */
    public void updatePerformData(long startTime, long endTime, Observable<List<UserPerformBean>> observable) {
        if (service == null) {
            Log.e(TAG, "updatePerformData: service is null! Start a new one!");
            if (context != null) {
                context.bindService(new Intent(context, SchedulerService.class), connection, Context.BIND_AUTO_CREATE);
            } else {
                Log.e(TAG, "updatePerformData: context is null!");
            }
        }

        service.harvestPerformance(startTime, endTime, observable);
    }

}
