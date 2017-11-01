package com.linmama.dinning;

import android.app.Application;

/**
 * Created by jingkang on 2017/2/27
 */

public class LmamaApplication extends Application {
    private static LmamaApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static LmamaApplication getInstance() {
        return mInstance;
    }
}
