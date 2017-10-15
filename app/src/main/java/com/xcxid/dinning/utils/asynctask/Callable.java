package com.xcxid.dinning.utils.asynctask;

/**
 * Created by jingkang on 2017/3/25
 * 回调接口,回调方法运行于异步线程
 */

public interface Callable<T> {
    public T call() throws Exception;
}
