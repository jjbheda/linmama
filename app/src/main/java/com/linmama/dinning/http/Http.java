package com.linmama.dinning.http;

import android.text.TextUtils;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.url.UrlHelper;
import com.linmama.dinning.BuildConfig;
import com.linmama.dinning.utils.NetworkUtil;
import com.linmama.dinning.utils.SpUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.linmama.dinning.utils.SpUtils.get;

public class Http {

    private static HttpService httpService;
    private static Retrofit retrofit;
    private static String SUBMIT_ADVICE = "http://work.xcxid.com/api/submitAdvice/";
    private static String CHECK_APP_VERSION = "http://work.xcxid.com/api/checkAppVersion/";
//    private static String USER_SERVER = "http://work.xcxid.com/api/getUserServer/";

    /**
     * @return retrofit的底层利用反射的方式, 获取所有的api接口的类
     */
    public static HttpService getHttpService() {
        if (httpService == null) {
            httpService = getRetrofit().create(HttpService.class);
        }
        return httpService;
    }

    private static Interceptor addQueryParameterInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        // TODO Provide your custom parameter here
                        // .addQueryParameter("phoneSystem", "")
                        // .addQueryParameter("phoneModel", "")
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
    }

    private static Interceptor addHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String originalUrl = originalRequest.url().toString();
                Request.Builder requestBuilder = originalRequest.newBuilder();
                if (!TextUtils.isEmpty((String) get("token", "")) && !originalUrl.equals(SUBMIT_ADVICE) &&
                        !originalUrl.equals(CHECK_APP_VERSION)) {
                    requestBuilder
                            .header("Authorization", "shop_token " + get("token", ""));
                }
                requestBuilder
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    /**
     * 设置缓存
     */
    private static Interceptor addCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtil.isNetworkAvailable(XcxidApplication.getInstance())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetworkUtil.isNetworkAvailable(XcxidApplication.getInstance())) {
                    int maxAge = 0;
                    // 有网络时, 设置缓存超时时间0个小时, 意思就是不读取缓存数据, 只对get有用, post没有缓冲
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            // 清除头信息, 因为服务器如果不支持, 会返回一些干扰信息, 不清除下面无法生效
                            .removeHeader("Retrofit")
                            .build();
                } else {
                    // 无网络时，设置超时为1天  只对get有用,post没有缓冲
                    int maxStale = 60 * 60 * 24;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" +
                                    maxStale)
                            .removeHeader("nyn")
                            .build();
                }
                return response;
            }
        };
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (Http.class) {
                if (retrofit == null) {
                    // Setting the size and route of cache
                    File cacheFile = new File(XcxidApplication.getInstance().getCacheDir(), "cache");
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

                    OkHttpClient.Builder builder = new OkHttpClient
                            .Builder()
                            // Adding query parameter interceptor.
                            .addInterceptor(addQueryParameterInterceptor())
                            // Filtering token.
                            .addInterceptor(addHeaderInterceptor())
                            // Adding cache
                            .cache(cache)
                            .connectTimeout(60L, TimeUnit.SECONDS)
                            .readTimeout(60L, TimeUnit.SECONDS)
                            .writeTimeout(60L, TimeUnit.SECONDS);
                    if (BuildConfig.DEBUG) {
                        // A log interceptor, logs all information.
                        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                        // Setting the level at which this interceptor logs.{NONE，BASIC, HEADERS, BODY}
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        // Adding logging interceptor.
                        builder = builder.addInterceptor(loggingInterceptor);
                    }
                    OkHttpClient client = builder.build();

                    String server = (String) SpUtils.get(Constants.USER_SERVER, "");
                    if (TextUtils.isEmpty(server)) {
                        server = UrlHelper.BASE_URL;
                    }
                    // Retrive Retrofit instance.
                    retrofit = new Retrofit
                            .Builder()
                            .baseUrl(server)
                            .client(client)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

}
