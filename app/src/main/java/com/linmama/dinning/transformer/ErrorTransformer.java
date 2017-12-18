package com.linmama.dinning.transformer;

import android.text.TextUtils;

import com.linmama.dinning.url.UrlHelper;
import com.linmama.dinning.base.BaseHttpResult;
import com.linmama.dinning.except.ErrorType;
import com.linmama.dinning.except.ExceptionEngine;
import com.linmama.dinning.except.ServerException;
import com.linmama.dinning.utils.LogUtils;

import java.util.Map;

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
                    } else  if (httpResult.getData()!=null && httpResult.getData() instanceof Map && ((Map)httpResult.getData()).containsKey("errors_info")) {
                        errorInfo = (String) ((Map)httpResult.getData()).get("errors_info");
                    }  else {
                        throw new ServerException(ErrorType.HTTP_ERROR, "服务器连接异常");
                    }
                    if (!TextUtils.isEmpty(errorInfo)) {
                        throw new ServerException(ErrorType.HTTP_ERROR, errorInfo);
                    } else {
                        throw new ServerException(ErrorType.HTTP_ERROR, "服务器连接异常");
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
