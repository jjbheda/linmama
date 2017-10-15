package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.TakingOrderBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

public class TakingOrderModel extends BaseModel {

    public void getTakingOrder(int page, @NonNull final TakingOrderHint hint) {

        if (hint == null)
            throw new RuntimeException("TakingOrderHint cannot be null.");

        httpService.getReceivedOrder(page)
                .compose(new CommonTransformer<TakingOrderBean>())
                .subscribe(new CommonSubscriber<TakingOrderBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(TakingOrderBean bean) {
                        hint.successInfo(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failInfo(e.getMessage());
                    }
                });
    }

    public interface TakingOrderHint {
        void successInfo(TakingOrderBean bean);

        void failInfo(String failMsg);
    }

}
