package com.opengl.deng.testnewrelic.analyzesdk.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

/**
 * @Description check permission
 * Created by deng on 2018/6/26.
 */
public class PermissionUtil {
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String[] GET_PHONE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};
    public static final String[] GET_READ_WRITE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static boolean getPermission(Activity context, String permission, int requestCode) {
        int hasPermission = ActivityCompat.checkSelfPermission(context, permission);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    public static boolean getPermission(Activity context, int requestCode, String... permission) {
        ArrayList<String> neededPermission = new ArrayList<>();
        for (String need : permission) {
            int hasPermission = ActivityCompat.checkSelfPermission(context, need);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                neededPermission.add(need);
            }
        }

        if (neededPermission.size() > 0) {
            ActivityCompat.requestPermissions(context, neededPermission.toArray(new String[neededPermission.size()]), requestCode);
            return false;
        }
        return true;
    }

    public static boolean checkPermission(Activity context, String permission) {
        int hasPermission = ActivityCompat.checkSelfPermission(context, permission);
        return hasPermission == PackageManager.PERMISSION_GRANTED;
    }

}
