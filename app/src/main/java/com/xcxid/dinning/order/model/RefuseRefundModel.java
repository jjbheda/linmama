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

public class RefuseRefundModel extends BaseModel {

    public void refuseRefund(@NonNull String refund_id, @NonNull String reason, @NonNull final RefuseRefundHint hint) {
        if (null == hint)
            throw new RuntimeException("RefuseRefundHint cannot be null.");

        httpService.refusedRefund(refund_id, reason)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successRefuseRefund(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failRefuseRefund(e.getMessage());
                    }
                });
    }

    public interface RefuseRefundHint {
        void successRefuseRefund(DataBean bean);

        void failRefuseRefund(String failMsg);
    }
}
