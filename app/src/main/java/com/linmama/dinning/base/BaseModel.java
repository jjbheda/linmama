package com.linmama.dinning.base;

import com.linmama.dinning.http.HttpService;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.http.Http;

public class BaseModel implements IModel {
    public static HttpService httpService;

    //初始化httpService
    static {
        httpService = Http.getHttpService();
    }
}
