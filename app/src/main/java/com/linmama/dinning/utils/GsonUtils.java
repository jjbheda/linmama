package com.linmama.dinning.utils;

import com.google.gson.Gson;

/**
 * Created by jingkang on 2017/3/10
 */

public class GsonUtils {
    private static Gson gson = new Gson();

    public static <T> T toBean(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
