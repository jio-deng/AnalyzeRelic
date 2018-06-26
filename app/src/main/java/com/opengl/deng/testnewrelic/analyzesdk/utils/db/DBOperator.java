package com.opengl.deng.testnewrelic.analyzesdk.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.opengl.deng.testnewrelic.analyzesdk.AnalyzeRelic;
import com.opengl.deng.testnewrelic.analyzesdk.bean.UserEventBean;
import com.opengl.deng.testnewrelic.analyzesdk.bean.UserPerformBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @Description Operate database
 * Created by deng on 2018/6/20.
 */
public class DBOperator {
    private static int NUM_TO_UPDATE;
    private static AtomicInteger numOfEvent = new AtomicInteger(0);
    private static AtomicInteger numOfPerform = new AtomicInteger(0);
    private static long ppreUpdateTime = 0;
    private static long pcurrentUpdateTime = 0;

    /** dbHelper类 */
    private DBHelper dbHelper;
    private void initDb(Context context) {
        dbHelper = new DBHelper(context);
    }

    /** 单例模式 */
    private static DBOperator instance;
    private DBOperator(){}

    public static DBOperator getInstance(Context context, int num) {
        if (instance == null) {
            instance = new DBOperator();
            instance.initDb(context);
            NUM_TO_UPDATE = num;
        }
        return instance;
    }
    public static DBOperator getInstance() {
        if (instance == null) {
            throw new RuntimeException("class Sowing must be initialized first!");
        }
        return instance;
    }

    /** insert user event*/
    public void insertUserEvent(UserEventBean bean) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CATEGORY, bean.getCategory());
        values.put(DBHelper.NAME, bean.getName());
        values.put(DBHelper.MESSAGE, bean.getMessage());
        values.put(DBHelper.START_TIME, bean.getStartTime());
        database.insert(DBHelper.TABLE_USER_EVENT, null, values);
        database.close();
    }

    public void insertUserEvent(List<UserEventBean> list) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "insert into " + DBHelper.TABLE_USER_EVENT + "("
                + DBHelper.CATEGORY + " , "
                + DBHelper.NAME + " , "
                + DBHelper.MESSAGE + " , "
                + DBHelper.START_TIME + ")"
                + " values(?,?,?,?)";
        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(sql);
        for (UserEventBean bean : list) {
            insertUserEventData(statement, bean);
            statement.executeInsert();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    private void insertUserEventData(SQLiteStatement statement, UserEventBean bean) {
        statement.bindString(1, bean.getCategory());
        statement.bindString(2, bean.getName());
        statement.bindString(3, bean.getMessage());
        statement.bindLong(4, bean.getStartTime());
    }

    /** insert user perform */
    public void insertUserPerform(UserPerformBean bean) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.SURFACE, bean.getSurface());
        values.put(DBHelper.START_TIME, bean.getStartTime());
        values.put(DBHelper.END_TIME, bean.getEndTime());
        values.put(DBHelper.DURATION, bean.getDuration());
        database.insert(DBHelper.TABLE_USER_PERFORM, null, values);
        database.close();
        //判断当前插入数量是否需要上传
        atomicPerformIncrease();
    }

    public void insertUserPerform(List<UserPerformBean> list) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "insert into " + DBHelper.TABLE_USER_PERFORM + "("
                + DBHelper.SURFACE + " , "
                + DBHelper.START_TIME + " , "
                + DBHelper.END_TIME + " , "
                + DBHelper.DURATION + ")"
                + " values(?,?,?,?)";
        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(sql);
        for (UserPerformBean bean : list) {
            insertUserPerformData(statement, bean);
            statement.executeInsert();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    private void insertUserPerformData(SQLiteStatement statement, UserPerformBean bean) {
        statement.bindString(1, bean.getSurface());
        statement.bindLong(2, bean.getStartTime());
        statement.bindLong(3, bean.getEndTime());
        statement.bindLong(4, bean.getDuration());
    }

    /** query from table */
    public void queryTable(final String tableName) {
        //保存时间
        ppreUpdateTime = pcurrentUpdateTime;
        pcurrentUpdateTime = System.currentTimeMillis();
        final String current = String.valueOf(pcurrentUpdateTime);
//        if (TextUtils.equals(tableName, DBHelper.TABLE_USER_EVENT)) {
//            //TODO:user event
//        } else {
//
//        }

        Observable<List<UserPerformBean>> observable = Observable.create(new ObservableOnSubscribe<List<UserPerformBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UserPerformBean>> e) throws Exception {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = database.query(false, tableName, null, DBHelper.END_TIME + "<?", new String[]{current}, null, null, null, null);
                if (cursor.moveToFirst()) {
                    List<UserPerformBean> list = new ArrayList<>();
                    do {
                        UserPerformBean bean = new UserPerformBean();
                        bean.setSurface(cursor.getString(1));
                        bean.setStartTime(cursor.getLong(2));
                        bean.setEndTime(cursor.getLong(3));
                        bean.setDuration(cursor.getLong(4));
                        list.add(bean);
                    } while (cursor.moveToNext());

                    e.onNext(list);
                    cursor.close();
                    database.close();
                    e.onComplete();
                } else {
                    e.onError(new Throwable("DBOperator: queryTable(Perform Data)->No data founded."));
                }
            }
        });

        AnalyzeRelic.getInstance().updatePerformData(ppreUpdateTime, pcurrentUpdateTime, observable);
    }

    /** delete from table */
    public void deleteTable(String tableName) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "delete from " + tableName;
        database.execSQL(sql);
        database.close();
    }

    public void deleteTable(String tableName, long startTime, long endTime) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "delete from " + tableName
                + " where " + DBHelper.END_TIME
                + " < " + endTime
                + " and " + DBHelper.END_TIME
                + " > " + startTime;
        database.execSQL(sql);
        database.close();
    }

    /** User Perform : 统计自增+限数判断 */
    private void atomicPerformIncrease() {
        int num = numOfPerform.incrementAndGet();
        if (num >= NUM_TO_UPDATE) {
            //TODO:update -> read data; turn into json
            queryTable(DBHelper.TABLE_USER_PERFORM);
        }
    }

}
