package com.opengl.deng.testnewrelic.analyzesdk.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.opengl.deng.testnewrelic.analyzesdk.bean.UserEventBean;
import com.opengl.deng.testnewrelic.analyzesdk.bean.UserPerformBean;

import java.util.List;

/**
 * @Description Operate database
 * Created by deng on 2018/6/20.
 */
public class DBOperator {

    /** dbHelper类 */
    private DBHelper dbHelper;
    public void initDb(Context context) {
        dbHelper = new DBHelper(context);
    }

    /** 单例模式 */
    private static DBOperator instance = null;
    private DBOperator(){}
    public static DBOperator getInstance(Context context) {
        if (instance == null) {
            instance = new DBOperator();
            instance.initDb(context);
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

    /** delete from table */
    public void deleteTable(String tableName) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "delete from " + tableName;
        database.execSQL(sql);
        database.close();
    }

}
