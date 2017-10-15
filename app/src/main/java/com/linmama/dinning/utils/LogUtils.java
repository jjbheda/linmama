package com.linmama.dinning.utils;

import android.util.Log;

import com.linmama.dinning.BuildConfig;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class LogUtils {
    public static final boolean isDebug = BuildConfig.DEBUG;

    /**
     * 打印一个debug等级的 log
     */
    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d("xcxid_" + tag, msg);
        }
    }

    /**
     * 打印一个error等级的 log
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e("xcxid_" + tag, msg);
        }
    }

    /**
     * 打印一个error等级的 log
     */
    public static void e(Class cls, String msg) {
        if (isDebug) {
            Log.e("xcxid_" + cls.getSimpleName(), msg);
        }
    }
}
