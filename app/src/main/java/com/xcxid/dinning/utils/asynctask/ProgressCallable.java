package com.xcxid.dinning.utils.asynctask;

/**
 * Created by jingkang on 2017/3/25
 * 被观察者
 */

public interface ProgressCallable<T> {
    /**
     * 注册观察者对象
     * @param pProgressListener
     * @return
     * @throws Exception
     */
    public T call(final IProgressListener pProgressListener) throws Exception;
}
