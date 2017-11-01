package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.NonPayOrderBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/11
 */

public class NonPayOrderModel extends BaseModel {
    public void getNonPayOrder(@NonNull final NonPayOrderHint nonPayOrderHint) {

        if (nonPayOrderHint == null)
            throw new RuntimeException("NonPayOrderHint cannot be null");

        httpService.getNonPayOrder()
                .compose(new CommonTransformer<NonPayOrderBean>())
                .subscribe(new CommonSubscriber<NonPayOrderBean>(LmamaApplication.getInstance()) {
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
