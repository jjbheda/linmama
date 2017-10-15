package com.linmama.dinning.login;

import android.support.annotation.NonNull;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.http.HttpService;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.bean.UserServerBean;
import com.linmama.dinning.except.ApiException;

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
