package com.xcxid.dinning.login;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.bean.UserServerBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.http.HttpService;
import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jingkang on 2017/3/31
 */

public class UserServerModel implements IModel {

    public void getUserServer(@NonNull final String usercode, @NonNull final UserServerHint hint) {

        if (hint == null)
            throw new RuntimeException("UserServerHint cannot be null.");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://work.xcxid.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        HttpService httpService = retrofit.create(HttpService.class);
        httpService.getUserServer(usercode)
                .compose(new CommonTransformer<UserServerBean>())
                .subscribe(new CommonSubscriber<UserServerBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(UserServerBean bean) {
                        hint.successServer(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failServer(e.getMessage());
                    }
                });
    }

    public interface UserServerHint {
        void successServer(UserServerBean bean);

        void failServer(String failMsg);
    }
}
