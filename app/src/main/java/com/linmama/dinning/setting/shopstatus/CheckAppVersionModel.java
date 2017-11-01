package com.linmama.dinning.setting.shopstatus;

import android.support.annotation.NonNull;

import com.linmama.dinning.bean.AppVersionBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/30
 */

public class CheckAppVersionModel extends BaseModel {

    public void getAppVersion(@NonNull String url, @NonNull final CheckAppVersionHint hint) {
        if (null == hint)
            throw new RuntimeException("CheckAppVersionHint cannot be null.");

        httpService.getCheckAppVersion(url)
                .compose(new CommonTransformer<AppVersionBean>())
                .subscribe(new CommonSubscriber<AppVersionBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(AppVersionBean bean) {
                        hint.successCheckAppVersion(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failCheckAppVersion(e.getMessage());
                    }
                });
    }

    public interface CheckAppVersionHint {
        void successCheckAppVersion(AppVersionBean bean);

        void failCheckAppVersion(String failMsg);
    }
}
