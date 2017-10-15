package com.xcxid.dinning;

import android.app.Application;

/**
 * Created by jingkang on 2017/2/27
 */

public class XcxidApplication extends Application {
    private static XcxidApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static XcxidApplication getInstance() {
        return mInstance;
    }
}
