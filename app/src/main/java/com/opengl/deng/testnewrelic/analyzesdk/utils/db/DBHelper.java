package com.opengl.deng.testnewrelic.analyzesdk.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Description dbhelper
 * Created by deng on 2018/6/19.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "analyzesdk";
    protected static final String TABLE_USER_EVENT = "user_event";
    protected static final String TABLE_USER_PERFORM = "user_perform";

    protected static final String CATEGORY = "category";
    protected static final String NAME = "name";
    protected static final String MESSAGE = "message";
    protected static final String START_TIME = "start_time";
    protected static final String END_TIME = "end_time";
    protected static final String DURATION = "duration";
    protected static final String SURFACE = "surface";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //用户事件表
        db.execSQL("CREATE TABLE " + TABLE_USER_EVENT
            + "(_id integer primary key autoincrement , "
            + CATEGORY + " TEXT , "
            + NAME + " TEXT , "
            + MESSAGE + " TEXT , "
            + START_TIME + " LONG)");

        //用户行为表
        db.execSQL("CREATE TABLE " + TABLE_USER_PERFORM
            + "(_id integer primary key autoincrement , "
            + SURFACE + " TEXT , "
            + START_TIME + " LONG , "
            + END_TIME + " LONG , "
            + DURATION + " LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
