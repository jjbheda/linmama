package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.subscriber.CommonSubscriber;

/**
 * Created by jingkang on 2017/3/10
 */
public class CompleteOrderModel extends BaseModel {
    public void completeWarn(@NonNull final String orderId, @NonNull final CompleteHint hint) {

        if (hint == null)
            throw new RuntimeException("CompleteHint cannot be null.");

        httpService.completeOrder(orderId)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successComplete(orderId);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failComplete(e.getMessage());
                    }
                });
    }

    public interface CompleteHint {
        void successComplete(String orderId);

        void failComplete(String str);
    }
}
