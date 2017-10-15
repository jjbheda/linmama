package com.xcxid.dinning.setting.shopstatus;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/16
 */

public class StoreStatusModifyModel extends BaseModel {

    public void openOrClose(@NonNull final String opFlag, @NonNull final StoreStatusModifyHint hint) {
        if (hint == null)
            throw new RuntimeException("StoreStatusModifyHint cannot be null.");

        httpService.openOrClose(opFlag)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successStoreStatusModify(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failStoreStatusModify(e.getMessage());
                    }
                });
    }

    public interface StoreStatusModifyHint {
        void successStoreStatusModify(DataBean bean);

        void failStoreStatusModify(String failMsg);
    }
}
