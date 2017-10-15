package com.linmama.dinning.utils.asynctask;

/**
 * Created by jingkang on 2017/3/25
 * 回调接口,回调方法运行于主线程
 */

public interface CallEarliest<T> {
    public void onCallEarliest() throws Exception;
}
