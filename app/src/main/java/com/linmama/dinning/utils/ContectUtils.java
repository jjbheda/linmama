package com.linmama.dinning.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class ContectUtils {
    private static final String TAG = "ContectUtils";
    private static final int MY_PERMISSION_CODE = 1003;

    public static void onCall(Activity activity, String paramString) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_CODE);
            Toast.makeText(activity, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.parse("tel:" + paramString);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        activity.startActivity(intent);
    }

    public static void sendMessage(Activity paramActivity, String paramString1, String paramString2) {}
}
