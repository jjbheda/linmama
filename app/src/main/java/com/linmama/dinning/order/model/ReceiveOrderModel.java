package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/6
 */

public class ReceiveOrderModel extends BaseModel {
    public void receivingOrder(@NonNull final String orderId,
                               @NonNull final ReceiveHint hint) {
        if (hint == null)
            throw new RuntimeException("ReceiveHint cannot be null.");

//        httpService.receivingOrder(orderId)
//                .compose(new CommonTransformer<DataBean>())
//                .subscribe(new CommonSubscriber<DataBean>(LmamaApplication.getInstance()) {
//                    @Override
//                    public void onNext(DataBean bean) {
//                        hint.successReceive(orderId);
//                    }
//
//                    @Override
//                    public void onError(ApiException e) {
//                        super.onError(e);
//                        hint.failReceive(e.getMessage());
//                    }
//                });
    }

    public interface ReceiveHint {
        void successReceive(String orderId);

        void failReceive(String failMsg);
    }
}
