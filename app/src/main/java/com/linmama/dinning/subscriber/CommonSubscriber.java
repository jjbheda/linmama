package com.linmama.dinning.subscriber;

import android.content.Context;

import com.linmama.dinning.base.BaseSubscriber;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.NetworkUtil;
import com.linmama.dinning.utils.ViewUtils;

public abstract class CommonSubscriber<T> extends BaseSubscriber<T> {

    private Context context;

    public CommonSubscriber(Context context) {
        this.context = context;
    }

    private static final String TAG = "CommonSubscriber";

    @Override
    public void onStart() {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            LogUtils.e(TAG, "网络不可用");
            ViewUtils.showToast(context, "网络不可用");
        } else {
            LogUtils.e(TAG, "网络可用");
        }
    }

    @Override
    protected void onError(ApiException e) {
        LogUtils.e(TAG, "错误信息为 " + "code:" + e.getCode() + "   message:" + e.getMessage());
    }

    @Override
    public void onCompleted() {
        LogUtils.e(TAG, "成功了");
    }

}
