package com.xcxid.dinning.except;

import android.text.TextUtils;

import com.xcxid.dinning.utils.GsonUtils;
import com.xcxid.dinning.utils.RegexUtils;

import retrofit2.HttpException;
import retrofit2.Response;

public class ExceptionEngine implements ErrorType {

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException he = (HttpException) e;
            Response response;
            if (null != he.response()) {
                response = he.response();
                int errorCode = response.code();
                String errorData = response.errorBody().source().toString();
                if (!TextUtils.isEmpty(errorData)) {
                    String errorInfo = RegexUtils.subString(errorData);
                    if (!TextUtils.isEmpty(errorInfo)) {
                        ErrorBean errorBean = null;
                        try {
                            errorBean = GsonUtils.toBean(errorInfo, ErrorBean.class);
                        } catch (Exception e1) {
                            ex = new ApiException(e, errorCode, getErrorMsg(errorCode));
                        }

                        if (null != errorBean && null != errorBean.getErrors_info()) {
                            ex = new ApiException(e, errorCode, errorBean.getErrors_info().toString());
                        } else {
                            ex = new ApiException(e, errorCode, getErrorMsg(errorCode));
                        }
                    } else {
                        ex = new ApiException(e, errorCode, getErrorMsg(errorCode));
                    }
                } else {
                    ex = new ApiException(e, errorCode, getErrorMsg(errorCode));
                }
            } else {
                ex = new ApiException(e, ErrorType.OTHER_ERROR, getErrorMsg(ErrorType.OTHER_ERROR));
            }
        } else if (e instanceof ServerException) {
            ServerException se = (ServerException) e;
            ex = new ApiException(e, se.code, se.message);
        } else if (e instanceof Exception) {
            ex = new ApiException(e, ErrorType.OTHER_ERROR, "Network Error");
        } else {
            ex = new ApiException(e, ErrorType.OTHER_ERROR, getErrorMsg(ErrorType.OTHER_ERROR));
        }
        return ex;
    }

    private static String getErrorMsg(int errorCode) {
        if (errorCode == BAD_REQUEST) {
            return "Bad Request";
        } else if (errorCode == UNAUTHORIZED) {
            return "Unauthorized";
        } else if (errorCode == FORBIDDEN) {
            return "Forbidden";
        } else if (errorCode == NOT_FOUND) {
            return "Not Found";
        } else if (errorCode == METHOD_NOT_ALLOWED) {
            return "Method Not Allowed";
        } else if (errorCode == NOT_ACCEPTABLE) {
            return "Not Acceptable";
        } else if (errorCode == REQUEST_TIMEOUT) {
            return "Request Timeout";
        } else if (errorCode == INTERNAL_SERVER_ERROR) {
            return "Internal Server Error";
        } else if (errorCode == NOT_IMPLEMENTED) {
            return "Not Implemented";
        } else if (errorCode == BAD_GATEWAY) {
            return "Bad Gateway";
        } else if (errorCode == SERVICE_UNAVAILABLE) {
            return "Service Unavailable";
        } else if (errorCode == GATEWAY_TIMEOUT) {
            return "Gateway Timeout";
        } else if (errorCode == HTTP_VERSION_NOT_SUPPORTED) {
            return "Http Version Not Supported";
        }
        return "Network Error";
    }
}

