package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.CancelBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/6
 */

public class CancelOrderModel extends BaseModel {
    public void cancelOrder(@NonNull final String orderId, @NonNull final String reason,
                            @NonNull final CancelHint hint) {
        if (hint == null)
            throw new RuntimeException("CancelHint cannot be null.");

        httpService.cancelOrder(orderId, reason)
                .compose(new CommonTransformer<CancelBean>())
                .subscribe(new CommonSubscriber<CancelBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(CancelBean result) {
                        hint.successCancel(orderId, result);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failCancel(e.getMessage());
                    }
                });
    }

    public interface CancelHint {
        void successCancel(String orderId, CancelBean result);

        void failCancel(String str);
    }
}
