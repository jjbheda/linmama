package com.linmama.dinning.setting.shopstatus;

import android.support.annotation.NonNull;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.except.ApiException;

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
