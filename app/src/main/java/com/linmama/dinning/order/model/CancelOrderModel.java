package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.bean.CancelBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

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
