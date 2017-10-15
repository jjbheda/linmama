package com.xcxid.dinning.setting.shopstatus;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.AppVersionBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/30
 */

public class CheckAppVersionModel extends BaseModel {

    public void getAppVersion(@NonNull String url, @NonNull final CheckAppVersionHint hint) {
        if (null == hint)
            throw new RuntimeException("CheckAppVersionHint cannot be null.");

        httpService.getCheckAppVersion(url)
                .compose(new CommonTransformer<AppVersionBean>())
                .subscribe(new CommonSubscriber<AppVersionBean>(XcxidApplication.getInstance()) {
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
