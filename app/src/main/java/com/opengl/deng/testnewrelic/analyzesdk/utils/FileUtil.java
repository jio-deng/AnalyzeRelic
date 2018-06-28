package com.opengl.deng.testnewrelic.analyzesdk.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;

/**
 * @Description Created by deng on 2018/6/27.
 */
public class FileUtil {
    private static final String TAG = "FileUtils";

    private static String SDCardRoot, cacheFilePath;
    private final static String ENV_SECONDARY_STORAGE = "SECONDARY_STORAGE";


    public static String getLogPath() {
        return cacheFilePath + "dzm" + File.separator + "log" + File.separator;
    }

    /** 崩溃日志目录 */
    public static String getCrashPath() {
        return cacheFilePath + "analyze" + File.separator + "crash" + File.separator;
    }

    public static void init(Context context) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 24) {
            File[] externalFileDirs = context.getExternalFilesDirs(null);
            File[] exiernalCacheDirs = context.getExternalCacheDirs();
            if (externalFileDirs.length > 1) {
                if (externalFileDirs[1] != null) {
                    SDCardRoot = externalFileDirs[1].getAbsolutePath() + File.separator;
                    cacheFilePath = exiernalCacheDirs[1].getAbsolutePath() + File.separator;
                } else if (externalFileDirs[0] != null) {
                    SDCardRoot = externalFileDirs[0].getAbsolutePath() + File.separator;
                    cacheFilePath = exiernalCacheDirs[0].getAbsolutePath() + File.separator;
                } else {
                    SDCardRoot = context.getFilesDir().getAbsolutePath() + File.separator;
                    cacheFilePath = context.getCacheDir() + File.separator;
                }
            } else if (externalFileDirs.length == 1) {
                if (exiernalCacheDirs[0] != null) {
                    SDCardRoot = externalFileDirs[0].getAbsolutePath() + File.separator;
                    cacheFilePath = exiernalCacheDirs[0].getAbsolutePath() + File.separator;
                } else {
                    SDCardRoot = context.getFilesDir().getAbsolutePath() + File.separator;
                    cacheFilePath = context.getCacheDir() + File.separator;
                }
            } else {
                SDCardRoot = context.getFilesDir().getAbsolutePath() + File.separator;
                cacheFilePath = context.getCacheDir() + File.separator;
            }
        } else if (Build.VERSION.SDK_INT >= 24) {
            File[] externalFileDirs = context.getExternalFilesDirs(null);
            File[] exiernalCacheDirs = context.getExternalCacheDirs();
            if (externalFileDirs.length > 0) {
                SDCardRoot = externalFileDirs[0].getAbsolutePath() + File.separator;
            } else {
                SDCardRoot = context.getFilesDir().getAbsolutePath() + File.separator;
            }
            if (exiernalCacheDirs.length > 0) {
                cacheFilePath = exiernalCacheDirs[0].getAbsolutePath() + File.separator;
            } else {
                cacheFilePath = context.getCacheDir() + File.separator;
            }
        } else {
            SDCardRoot = System.getenv(ENV_SECONDARY_STORAGE);
            if (SDCardRoot != null) {
                SDCardRoot = SDCardRoot + File.separator + "Android" + File.separator + "data" + File.separator + context.getPackageName()
                        + File.separator + "files" + File.separator;
                cacheFilePath = SDCardRoot + File.separator + "Android" + File.separator + "data" + File.separator + context.getPackageName()
                        + File.separator + "cache" + File.separator;
            } else {
                SDCardRoot = context.getFilesDir().getAbsolutePath() + File.separator;
                cacheFilePath = context.getCacheDir() + File.separator;
            }


            File fileRoot = new File(SDCardRoot);
            File cacheRoot = new File(cacheFilePath);
            if (!fileRoot.exists()) {
                boolean createResult = fileRoot.mkdirs();
                if (!createResult) {
                    Log.e(TAG, "创建文件夹失败");
                }
            }

            if (!cacheRoot.exists()) {
                boolean createResult = cacheRoot.mkdirs();
                if (!createResult) {
                    Log.e(TAG, "创建缓存文件夹失败");
                }
            }
        }

        Log.i(TAG, "init: " + SDCardRoot);
        Log.i(TAG, "init: " + cacheFilePath);

        File file = new File(SDCardRoot + "iCity");
        File cacheFile = new File(cacheFilePath + "iCity");
        createDir(file, "创建文件夹失败");
        createDir(cacheFile, "创建缓存文件夹失败");

        createDir(new File(getCrashPath()), "创建崩溃文件夹失败");

    }

    private static void createDir(File fileImage, String logInfo) {
        if (!fileImage.exists()) {
            boolean result = fileImage.mkdir();
            if (!result)
                Log.e("fileUtil", logInfo);
        }
    }

    public static boolean deleteFile(File file) {
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
