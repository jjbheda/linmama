package com.xcxid.dinning.transformer;

import android.text.TextUtils;

import com.xcxid.dinning.base.BaseHttpResult;
import com.xcxid.dinning.except.ErrorType;
import com.xcxid.dinning.except.ExceptionEngine;
import com.xcxid.dinning.except.ServerException;
import com.xcxid.dinning.url.UrlHelper;
import com.xcxid.dinning.utils.LogUtils;

import rx.Observable;
import rx.functions.Func1;

public class ErrorTransformer<T> implements Observable.Transformer<BaseHttpResult<T>, T> {

    private static ErrorTransformer errorTransformer = null;

    @Override
    public Observable<T> call(Observable<BaseHttpResult<T>> responseObservable) {

        return responseObservable.map(new Func1<BaseHttpResult<T>, T>() {
            @Override
            public T call(BaseHttpResult<T> httpResult) {

                if (httpResult == null)
                    throw new ServerException(ErrorType.EMPTY_BEAN, "Parsed Object cannot be null. ");

                LogUtils.e("ErrorTransformer", httpResult.toString());

                if (httpResult.getStatus().equals(UrlHelper.STATIC_FAIL)) {
                    String errorInfo;
                    if (null != httpResult.getErrors_info()) {
                        errorInfo = httpResult.getErrors_info().toString();
                    } else {
                        throw new ServerException(ErrorType.HTTP_ERROR, "Network Error.");
                    }
                    if (!TextUtils.isEmpty(errorInfo)) {
                        throw new ServerException(ErrorType.HTTP_ERROR, errorInfo);
                    } else {
                        throw new ServerException(ErrorType.HTTP_ERROR, "Network Error.");
                    }
                }

                return httpResult.getData();
            }
        })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(Throwable throwable) {
                        //ExceptionEngine为处理异常的驱动器throwable
                        //throwable.printStackTrace();
                        return Observable.error(ExceptionEngine.handleException(throwable));
                    }
                });

    }

    /**
     * @return 线程安全, 双层校验
     */
    public static <T> ErrorTransformer<T> getInstance() {

        if (errorTransformer == null) {
            synchronized (ErrorTransformer.class) {
                if (errorTransformer == null) {
                    errorTransformer = new ErrorTransformer<>();
                }
            }
        }
        return errorTransformer;

    }
}
