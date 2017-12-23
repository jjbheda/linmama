package com.linmama.dinning.base;

import android.widget.Toast;

import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.except.ErrorType;
import com.linmama.dinning.except.ServerException;

import rx.Subscriber;

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            onError(apiException);
        } else {
            try {
                throw new NullPointerException(e.getStackTrace().toString());
            } catch(Exception e3) {
                System.out.println("Caught inside demoproc.");
                throw e3; // rethrow the exception
            }
        }
    }

    /**
     * @param e 错误的一个回调
     */
    protected abstract void onError(ApiException e);

}
