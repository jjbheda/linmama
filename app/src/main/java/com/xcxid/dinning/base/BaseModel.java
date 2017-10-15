package com.xcxid.dinning.base;

import com.xcxid.dinning.http.Http;
import com.xcxid.dinning.http.HttpService;
import com.xcxid.dinning.mvp.IModel;

public class BaseModel implements IModel {
    protected static HttpService httpService;

    //初始化httpService
    static {
        httpService = Http.getHttpService();
    }
}
