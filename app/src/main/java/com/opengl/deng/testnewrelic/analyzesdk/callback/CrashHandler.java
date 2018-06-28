package com.opengl.deng.testnewrelic.analyzesdk.callback;

import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.opengl.deng.testnewrelic.analyzesdk.AnalyzeRelic;
import com.opengl.deng.testnewrelic.analyzesdk.utils.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description collect crash info
 * Created by deng on 2018/6/27.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";

    public static final String FILE_NAME = "analyze_relic_crash.txt";

    private Thread.UncaughtExceptionHandler mDefaultHandler;   //系统默认的崩溃处理
    private boolean isAllowLog = true;

    /** 单例模式 */
    private static CrashHandler ourInstance = new CrashHandler();
    private CrashHandler() {}
    public static CrashHandler getInstance() {
        return ourInstance;
    }

    /** 将CrashHandler设置为系统默认处理器 */
    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        //检查是否有崩溃日志：如果有，上传+删除
        final String crashLog = readFromCrashFile();
        if (!TextUtils.isEmpty(crashLog)) {
            //延时处理：当前service为null！  处理内容：update + delete
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AnalyzeRelic.getInstance().updateCrashLog(crashLog);
                }
            }).start();
        }
    }

    /**
     * 设置是否允许收集崩溃日志
     * @param flag allow log
     */
    public void setAllowLog(boolean flag) {
        isAllowLog = flag;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (isAllowLog && !handleException(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 处理崩溃信息 : save into file
     * @param throwable throwable
     * @return whether this process is completely done
     */
    private boolean handleException(Throwable throwable) {
        return throwable != null && saveCrashIntoFile(throwable);
    }

    /**
     * 将崩溃信息存入文件
     * @param throwable throwable
     * @return whether saving is successful
     */
    private boolean saveCrashIntoFile(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        //添加日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        sb.append(date).append("\n");
        //添加错误信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.flush();
        printWriter.close();
        String result = writer.toString();
        sb.append(result).append("\n");
        return writeToFile(sb.toString());
    }

    /**
     * 将数据写入文件
     * @param s crash log
     * @return isSuccess
     */
    private boolean writeToFile(String s) {
        File crashFile = FileUtil.makeFilePath(FileUtil.getCrashPath(), FILE_NAME);
        if (crashFile == null) {
            Log.e(TAG, "writeToFile: carshFile is null!");
            return false;
        }

        try {
            FileWriter writer = new FileWriter(crashFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(s);
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "writeToFile: error when opening crashFile");
            return false;
        }

        return true;
    }

    /**
     * 读取崩溃文件中的数据
     * @return 崩溃文件中的数据
     */
    private String readFromCrashFile() {
        StringBuilder crashLog = new StringBuilder();
        File crashFile = FileUtil.makeFilePath(FileUtil.getCrashPath(), FILE_NAME);
        if (crashFile == null) {
            Log.e(TAG, "readFile: carshFile is null!");
            return crashLog.toString();
        }

        try {
            FileReader reader = new FileReader(crashFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                crashLog.append(line);
            }
            bufferedReader.close();
            reader.close();
            return crashLog.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
