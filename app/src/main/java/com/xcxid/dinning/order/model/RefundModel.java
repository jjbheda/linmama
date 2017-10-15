package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/12
 */

public class RefundModel extends BaseModel {

    public void refund(@NonNull String refund_id, @NonNull String operation_password, @NonNull final RefundHint hint) {
        if (null == hint)
            throw new RuntimeException("RefundHint cannot be null.");

        httpService.refund(refund_id, operation_password)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successRefund(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failRefund(e.getMessage());
                    }
                });
    }
    public interface RefundHint {
        void successRefund(DataBean bean);

        void failRefund(String failMsg);
    }
}
