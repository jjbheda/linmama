package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

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
