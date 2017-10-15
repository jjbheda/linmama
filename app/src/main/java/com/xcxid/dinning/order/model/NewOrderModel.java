package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.NewOrderBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

public class NewOrderModel extends BaseModel {

    public void getNewOrder(int page, @NonNull final NewOrderHint hint) {

        if (hint == null)
            throw new RuntimeException("NewOrderHint cannot be null.");

        httpService.getNewOrder(page)
                .compose(new CommonTransformer<NewOrderBean>())
                .subscribe(new CommonSubscriber<NewOrderBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(NewOrderBean bean) {
                        hint.successNewOrder(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failNewOrder(e.getMessage());
                    }
                });
    }

    public interface NewOrderHint {
        void successNewOrder(NewOrderBean bean);

        void failNewOrder(String failMsg);
    }

}
