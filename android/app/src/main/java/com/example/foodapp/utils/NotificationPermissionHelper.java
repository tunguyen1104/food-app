package com.example.foodapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.foodapp.consts.Constants;

public class NotificationPermissionHelper {

    public static boolean hasPostNotificationPermission(Activity act) {
        return android.os.Build.VERSION.SDK_INT < 33 ||
               ContextCompat.checkSelfPermission(act, Manifest.permission.POST_NOTIFICATIONS)
                       == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestIfNeeded(Activity act) {
        if (!hasPostNotificationPermission(act)) {
            ActivityCompat.requestPermissions(
                    act, new String[]{Manifest.permission.POST_NOTIFICATIONS}, Constants.REQUEST_CODE_POST_NOTIFICATION);
        }
    }
}
