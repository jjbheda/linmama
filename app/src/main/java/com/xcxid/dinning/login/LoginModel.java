package com.xcxid.dinning.login;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.LoginBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

public class LoginModel extends BaseModel {

    public void login(@NonNull String username, @NonNull String pwd, @NonNull final LoginHint hint) {

        if (hint == null)
            throw new RuntimeException("LoginHint cannot be null.");
        httpService.login(username, pwd)
                .compose(new CommonTransformer<LoginBean>())
                .subscribe(new CommonSubscriber<LoginBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(LoginBean bean) {
                        hint.successLogin(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failLogin(e.getMessage());
                    }
                });
    }

    //通过接口产生信息回调
    public interface LoginHint {
        void successLogin(LoginBean loginBean);

        void failLogin(String failMsg);
    }

}
