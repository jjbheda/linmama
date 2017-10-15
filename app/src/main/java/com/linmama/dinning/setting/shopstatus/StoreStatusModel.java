package com.linmama.dinning.setting.shopstatus;

import android.support.annotation.NonNull;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.StoreSettingsBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/16
 */

public class StoreStatusModel extends BaseModel {

    public void getStoreStatus(@NonNull final StoreStatusHint hint) {
        if (hint == null)
            throw new RuntimeException("StoreStatusHint cannot be null.");

        httpService.getStoreSettings()
                .compose(new CommonTransformer<StoreSettingsBean>())
                .subscribe(new CommonSubscriber<StoreSettingsBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(StoreSettingsBean bean) {
                        hint.successStoreStatus(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failStoreStatus(e.getMessage());
                    }
                });
    }

    public interface StoreStatusHint {
        void successStoreStatus(StoreSettingsBean bean);

        void failStoreStatus(String failMsg);
    }
}
