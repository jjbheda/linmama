package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/6
 */

public class OKOrderModel extends BaseModel {
    public void okOrder(@NonNull final String orderId, @NonNull String password,
                        @NonNull final OKHint hint) {
        if (hint == null)
            throw new RuntimeException("OKHint cannot be null.");

        httpService.confirmPayment(orderId, password)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean result) {
                        hint.successOK(orderId);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOK(e.getMessage());
                    }
                });
    }

    public interface OKHint {
        void successOK(String orderId);

        void failOK(String str);
    }
}
