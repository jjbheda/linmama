package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.NonPayOrderBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/11
 */

public class NonPayOrderModel extends BaseModel {
    public void getNonPayOrder(@NonNull final NonPayOrderHint nonPayOrderHint) {

        if (nonPayOrderHint == null)
            throw new RuntimeException("NonPayOrderHint cannot be null");

        httpService.getNonPayOrder()
                .compose(new CommonTransformer<NonPayOrderBean>())
                .subscribe(new CommonSubscriber<NonPayOrderBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(NonPayOrderBean nonPayOrderBean) {
                        nonPayOrderHint.successNonPayOrder(nonPayOrderBean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        nonPayOrderHint.failNonPayOrder(e.getMessage());
                    }
                });
    }

    public interface NonPayOrderHint {
        void successNonPayOrder(NonPayOrderBean nonPayOrderBean);

        void failNonPayOrder(String str);
    }
}
