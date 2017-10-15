package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/6
 */

public class ReceiveOrderModel extends BaseModel {
    public void receivingOrder(@NonNull final String orderId,
                               @NonNull final ReceiveHint hint) {
        if (hint == null)
            throw new RuntimeException("ReceiveHint cannot be null.");

        httpService.receivingOrder(orderId)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successReceive(orderId);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failReceive(e.getMessage());
                    }
                });
    }

    public interface ReceiveHint {
        void successReceive(String orderId);

        void failReceive(String failMsg);
    }
}
