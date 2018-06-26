package com.opengl.deng.testnewrelic.analyzesdk.harvest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.opengl.deng.testnewrelic.analyzesdk.bean.UserPerformBean;
import com.opengl.deng.testnewrelic.analyzesdk.utils.db.DBHelper;
import com.opengl.deng.testnewrelic.analyzesdk.utils.db.DBOperator;
import com.opengl.deng.testnewrelic.analyzesdk.utils.net.UpdateUserData;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * @Description sdk < 21
 * Created by deng on 2018/6/23.
 */
public class SchedulerService extends Service {
    private static final String TAG = "SchedulerService";

    private static final String IS_SUCCESS = "isSuccess";
    private static final String MSG = "msg";

    private DBOperator operator;
    private UpdateUserData updateUserData;

    private Binder binder;

    public SchedulerService() {
        binder = new Binder();
        operator = DBOperator.getInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")
                .build();
        updateUserData = retrofit.create(UpdateUserData.class);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 上传用户行为
     * success:回调，删除数据库中对应数据
     * failed :log输出
     */
    public void harvestPerformance(final long startTime, final long endTime, Observable<List<UserPerformBean>> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<UserPerformBean>>() {
                    @Override
                    public void accept(List<UserPerformBean> list) throws Exception {
                        Log.d(TAG, "accept: harvestPerformance data -> " + list.toString());
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<List<UserPerformBean>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(List<UserPerformBean> list) throws Exception {
                        if (list != null && list.size() != 0) {
                            return updateUserData.updateUserPerform("1", list.toString());
                        }
                        return null;
                    }
                }).map(new Function<String, JSONObject>() {
            @Override
            public JSONObject apply(String s) throws Exception {
                return new JSONObject(s);
            }
        }).retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(JSONObject jsonObject) throws Exception {
                        boolean isSuccess = jsonObject.optBoolean(IS_SUCCESS);
                        if (isSuccess) {
                            DBOperator.getInstance().deleteTable(DBHelper.TABLE_USER_PERFORM, startTime, endTime);
                        } else {
                            String msg = jsonObject.optString(MSG);
                            Log.e(TAG, "harvestPerformance failed! Msg : " + msg);
                        }
                    }
                });
    }

    /**
     * 内部类binder，用于返回当前Service
     */
    public class Binder extends android.os.Binder {
        public Service getService() {
            return SchedulerService.this;
        }
    }
}
