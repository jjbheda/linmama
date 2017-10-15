package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.NewOrderBean;
import com.linmama.dinning.except.ApiException;

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
